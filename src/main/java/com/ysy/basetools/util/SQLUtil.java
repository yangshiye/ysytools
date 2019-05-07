package com.ysy.basetools.util;

import com.ysy.basetools.map.SMap;
import com.ysy.basetools.model.V2;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Created by guoqiang on 2016/4/16.
 */
public final class SQLUtil {
    private static final String PARAM = "param";
    private static final String INFO = "info";


    public static int updateAndBackup(DataSource dataSource, String tableName,
                                      String updateSqlPre, String sqlWhere) {
        String selectSql = "select * from " + tableName + " where " + sqlWhere;
        String updateSql = "update " + tableName + " set " + updateSqlPre + " where " + sqlWhere;
//        queryAndBackup(dataSource, tableName, selectSql, updateSql);
        return update(dataSource, updateSql);
    }

    public static int deleteAndBackup(DataSource dataSource, String tableName, String sqlWhere) {
        String selectSql = "select * from " + tableName + " where " + sqlWhere;
        String updateSql = "delete from " + tableName + " where " + sqlWhere;
//        queryAndBackup(dataSource, tableName, selectSql, updateSql);
        return update(dataSource, updateSql);
    }
//
//    public static int insert(DataSource dataSource, String tableName, String dbFields, String values, boolean autoKey) {
//        String insertSql = "insert into " + tableName + "(" + dbFields + ") values(" + values + ")";
//        String rowKey = insertBackSql(tableName, insertSql);
//        try {
//            V2<Integer, List<Map<String, Object>>> res = insertCon(dataSource, insertSql, autoKey);
//            insertBackResult(rowKey, res.getV1(), res.getV2(), null);
//            return res.getV1();
//        } catch (Exception e) {
//            insertBackResult(rowKey, 0, null, e);
//            ExceptionUtil.throwRunTime(e);
//        }
//        return -1;
//    }
//
//    public static String insertBackSql(String tableName, String sql) {
//        String rowKey = HBaseUtil.getRowKey(tableName + "_" + TimeUtil.format(new Date(), "yyyyMMdd"));
//        String nowTime = TimeUtil.getNowTimeAllStr();
//        HPut put = new HPut(rowKey);
//        put.addValue(PARAM, "time", nowTime);
//        put.addValue(PARAM, "insertSql", sql);
//        try {
////            BACKUP_TABLES.get().directCommit(ListUtil.createList(put.toPutVo()));
//        } catch (Exception e) {
//            ExceptionUtil.throwRunTime(e);
//        }
//        return rowKey;
//    }
//
//    public static void insertBackResult(String rowKey, int count, List<Map<String, Object>> result, Exception ex) {
//        String nowTime = TimeUtil.getNowTimeAllStr();
//        HPut put = new HPut(rowKey);
//        put.addValue(PARAM, "overTime", nowTime);
//        put.addValue(PARAM, "insertCount", count);
//        if (result != null) {
//            put.addValue(PARAM, "insertRes", JSONUtil.json(result));
//        }
//        if (ex != null) {
//            put.addValue(PARAM, "ex", StrUtil.toString(ex));
//        }
//        try {
////            BACKUP_TABLES.get().directCommit(ListUtil.createList(put.toPutVo()));
//        } catch (Exception e) {
//            ExceptionUtil.throwRunTime(e);
//        }
//    }

//    public static List<SMap> queryAndBackup(DataSource dataSource, String rowKeyPre, String selectSql) {
//        return queryAndBackup(dataSource, rowKeyPre, selectSql, null);
//    }
//
//    public static List<SMap> queryAndBackup(DataSource dataSource, String rowKeyPre, String selectSql, String updateSql) {
//        List<SMap> list = query(dataSource, selectSql);
//        if (ListUtil.isEmpty(list)) {
//            return list;
//        }
//        List<PutVo> putVos = new ArrayList<PutVo>(list.size());
//        int i = 0;
//        String rowKey = HBaseUtil.getRowKey(rowKeyPre + "_" + TimeUtil.format(new Date(), "yyyyMMdd"));
//        String nowTime = TimeUtil.getNowTimeAllStr();
//        for (Map map : list) {
//            HPut put = new HPut(rowKey + "_" + i);
//            put.addValue(INFO, "data", JSONUtil.json(map));
//            put.addValue(PARAM, "time", nowTime);
//            put.addValue(PARAM, "selectSql", selectSql);
//            if (updateSql != null) {
//                put.addValue(PARAM, "updateSql", updateSql);
//            }
//            putVos.add(put.toPutVo());
//            i++;
//        }
//        try {
//            BACKUP_TABLES.get().directCommit(putVos);
//        } catch (Exception e) {
//            ExceptionUtil.throwRunTime(e);
//        }
//        return list;
//    }

    public static int update(DataSource dataSource, String sql) throws RuntimeException {
        try {
            return updateCon(dataSource.getConnection(), sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<SMap> query(DataSource dataSource, String sql) throws RuntimeException {
        try {
            return query(dataSource.getConnection(), sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int updateCon(Connection con, String sql) throws RuntimeException {
        try {
            return con.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static V2<Integer, List<Map<String, Object>>> insertCon(DataSource dataSource, String sql, boolean autoKey)
            throws RuntimeException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            Statement k = connection.createStatement();
            int count;
            if (autoKey) {
                count = k.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                count = k.executeUpdate(sql);
            }
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(count);
            try {
                ResultSet rs = k.getGeneratedKeys();
                if (rs != null) {
                    ResultSetMetaData rData = rs.getMetaData();
                    if (rData != null) {
                        final int columnCount = rData.getColumnCount();
                        while (rs.next()) {
                            Map<String, Object> temp = new HashMap<String, Object>(columnCount);
                            for (int i = 1; i <= columnCount; i++) {
                                String label = rData.getColumnLabel(i);
                                temp.put(label, rs.getObject(i));
                            }
                            list.add(temp);
                        }
                    }
                }
            } catch (Exception e) {
                list.add(ListUtil.createMapSO("ex", StrUtil.getCurStack(e)));
            }

            return new V2<Integer, List<Map<String, Object>>>(count, list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static List<SMap> query(Connection con, String sql) throws RuntimeException {
        List<SMap> list = new LinkedList<SMap>();
        try {
            ResultSet result = con.createStatement().executeQuery(sql);
            ResultSetMetaData meta = result.getMetaData();
            int count = meta.getColumnCount();
            SMap<String, String> names = new SMap<String, String>();
            for (int i = 1; i <= count; i++) {
                String lable = meta.getColumnLabel(i);
                String typeName = meta.getColumnTypeName(i);
                String className = meta.getColumnClassName(i);
                names.put(lable, typeName + "=>" + className);
            }
            while (result.next()) {
                SMap map = new SMap();
                for (String column : names.keySet()) {
                    Object value = result.getObject(column);
                    if (value instanceof Boolean) {
                        value = result.getInt(column);
                    }
                    map.put(column, value);
                }
                list.add(map);
            }
            if (list.isEmpty()) {
                list.add(names);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    private SQLUtil() {

    }
}
