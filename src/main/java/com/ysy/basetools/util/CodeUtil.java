package com.ysy.basetools.util;


import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.util.*;

/**
 * Created by guoqiang on 2016/10/26.
 */
public class CodeUtil {
    public static final Map<Integer, Class> classMap = ListUtil.createMapCheck(
            Integer.class, Class.class,
            -7, Integer.class,
            -6, Integer.class,
            5, Integer.class,
            4, Integer.class,
            -5, Long.class,
            6, Double.class,
            8, Double.class,
            2, Double.class,
            3, Double.class,
            1, String.class,
            12, String.class,
            -1, String.class,
            91, Date.class,
            92, Date.class,
            93, Date.class
    );

    public static String genSetGet(Class clazz) {
        StringBuilder builder = new StringBuilder(2048);
        Collection<String> list = ReflectUtil.getInstanceSetMethod(clazz);
        for (String setMethod : list) {
            builder.append("MYSET").append(".").append(setMethod).append("(").append("MYGET").append(".");
            builder.append("get").append(setMethod.substring(3)).append("());\r\n");
        }
        return builder.toString();
    }

    private static String genSqlMapping(String tableName,
                                        boolean isFieldNameHump,
                                        List<ColumnType> list) throws Exception {
        StringBuilder builder = new StringBuilder(2048);
        builder.append("SELECT\r\n");
        for (int i = 0; i < list.size(); i++) {
            ColumnType type = list.get(i);
            builder.append(type.getName()).append(" AS ").append(getFieldName(type.getName(), isFieldNameHump));
            if (i < list.size() - 1) {
                builder.append(",\r\n");
            }
        }
        builder.append("\r\nfrom ").append(tableName);
        builder.append("\r\n\r\n=============================================\r\n\r\n");
        builder.append("insert into " + tableName + "(");
        for (int i = 0; i < list.size(); i++) {
            ColumnType type = list.get(i);
            builder.append(type.getName());
            if (i < list.size() - 1) {
                builder.append(",");
            }
        }
        builder.append(") \r\nvalues(");
        for (int i = 0; i < list.size(); i++) {
            ColumnType type = list.get(i);
            builder.append("#").append(getFieldName(type.getName(), isFieldNameHump)).append("#");
            if (i < list.size() - 1) {
                builder.append(",");
            }
        }
        builder.append(")\r\n\r\n");
        builder.append("\r\n\r\n=============================================\r\n\r\n");
        builder.append("UPDATE " + tableName + " SET \r\n");
        for (int i = 0; i < list.size(); i++) {
            ColumnType type = list.get(i);
            builder.append(type.getName()).append("=").append("#")
                    .append(getFieldName(type.getName(), isFieldNameHump)).append("#");
            if (i < list.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("\r\nWHERE ???");
        builder.append("\r\n\r\n=============================================\r\n\r\n");
        for (ColumnType type : list) {
            String name = getFieldName(type.getName(), isFieldNameHump);
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
            builder.append("desc.set").append(name).append("(src.get").append(name).append("());\r\n");
        }
        return builder.toString();
    }

    private static String genCode(String fullClassName,
                                  boolean isFieldNameHump,
                                  List<ColumnType> list) throws Exception {
        StringBuilder builder = new StringBuilder(2048);
        int packageNameIdx = fullClassName.lastIndexOf('.');
        String className = fullClassName;
        if (packageNameIdx > -1) {
            builder.append("package ").append(fullClassName.substring(0, packageNameIdx)).append(";\r\n\r\n");
            className = fullClassName.substring(packageNameIdx + 1);
        }
        builder.append("import java.io.Serializable;\r\n");
        Set<String> importSet = new HashSet<String>();
        for (ColumnType type : list) {
            if (classMap.containsKey(type.getType())) {
                type.setType(classMap.get(type.getType()));
            }
            importSet.add(type.getType().getName());
        }
        for (String s : importSet) {
            if (s.startsWith("java.lang.") && s.lastIndexOf('.') == "java.lang".length()) {
                continue;
            }
            builder.append("import ").append(s).append(";\r\n");
        }
        builder.append("\r\n");
        builder.append("public class ").append(className).append(" implements Serializable {\r\n\r\n");
        for (ColumnType t : list) {
            String fieldName = getFieldName(t.getName(), isFieldNameHump);
            String fieldFullClassName = t.getType().toString();
            String fieldClassName = fieldFullClassName;
            int idx = fieldFullClassName.lastIndexOf(".");
            if (idx > -1) {
                fieldClassName = fieldFullClassName.substring(idx + 1);
            }
            builder.append("    private ").append(fieldClassName).append(" ").append(fieldName)
                    .append(";//").append(t.getRemarks()).append("\r\n");
        }
        builder.append("\r\n}");
        return builder.toString();
    }


    private static String getFieldName(String name, boolean isFieldNameHump) {
        if (isFieldNameHump) {
            StringBuilder builder = new StringBuilder(name.length());
            boolean underline = false;
            for (char c : name.toCharArray()) {
                if (c == '_') {
                    underline = true;
                } else if (underline) {
                    underline = false;
                    builder.append(Character.toUpperCase(c));
                } else {
                    builder.append(c);
                }
            }
            return builder.toString();
        }
        return name;
    }

    private static List<ColumnType> getColumnType(Connection con, String tableName) throws Exception {
        try {
            List<ColumnType> list = getMetaData(con, tableName);
            return list;
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    private static List<ColumnType> getMetaData(Connection conn, String tableName) throws Exception {
        List<ColumnType> result = new ArrayList<ColumnType>();
        ResultSet rs;
        try {
            rs = conn.getMetaData().getColumns(null, "%", tableName, "%");
            while (rs.next()) {
                ColumnType columnType = new ColumnType();
                columnType.setName(rs.getString("COLUMN_NAME"));
                columnType.setRemarks(rs.getString("REMARKS"));
                Integer typeNum = rs.getInt("DATA_TYPE");
                Class type = classMap.get(typeNum);
                if (type == null) {
                    type = Object.class;
                }
                columnType.setType(type);
                result.add(columnType);
            }
        } finally {
            if (!conn.isClosed()) {
                conn.close();
            }
        }
        return result;
    }

    private static class ColumnType {
        private String name;
        private Class type;
        private String remarks;

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class getType() {
            return type;
        }

        public void setType(Class type) {
            this.type = type;
        }
    }

    private static Connection getConnection(String jdbc, String jdbcClassName, String user, String password) throws
            Exception {
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        Class c = ReflectUtil.getClassByName(jdbcClassName);
        if (c != null && Driver.class.isAssignableFrom(c)) {
            Driver driver = (Driver) c.newInstance();
            return driver.connect(jdbc, properties);
        }
        throw new Exception("DriverClass is not Driver class,class = " + c);
    }
}
