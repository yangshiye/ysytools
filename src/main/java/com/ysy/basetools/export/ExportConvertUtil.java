package com.ysy.basetools.export;

import com.ysy.basetools.enums.EnumUtil;
import com.ysy.basetools.enums.IEnum;
import com.ysy.basetools.enums.ValidStatusEnum;
import com.ysy.basetools.util.ListUtil;
import com.ysy.basetools.util.ReflectUtil;
import com.ysy.basetools.util.StrUtil;
import com.ysy.basetools.util.TimeUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Administrator on 2018/8/16.
 */
public class ExportConvertUtil {

    private static class Test1{
        @Column
        private int kk=321;
    }
    private static class Test2 extends Test1{
        @Columns(columns = {@Column(title = "状态码"), @Column(title = "状态描述",cellConvert = ValidStatusEnum.class)})
        private int id = 1;
        @Column(cellConvert = DateCellConvert.class,extParam = "yy/MM/dd HH:mm:ss")
        private Date date = new Date();
    }

    public static void main(String[] args) {
//        String a = "32237062100207";
//        long i = 032237062100207L;
//        System.out.println(a + " => " + i);
        List<List<String>> list = getData(Test2.class, ListUtil.createList(new Test2()));
        System.out.println(list);
    }

    public static <T> List<List<String>> getData(Class<T> clazz, List<T> list) {
        List<List<String>> result = new LinkedList<>();
        List<ColumnModel> columnModels = getTitles(clazz);
        List<String> head = new LinkedList<>();
        for (ColumnModel model : columnModels) {
            head.add(getTitle(model));
        }
        result.add(head);
        getData(list, clazz, columnModels, result);
        return result;
    }

    private static List<ColumnModel> getTitles(Class clazz) {
        List<ColumnModel> list = new LinkedList<>();
        List<Field> fields = ReflectUtil.getAllIFieldList(clazz);
        for (Field field : fields) {
            Columns columns = field.getAnnotation(Columns.class);
            Column column = field.getAnnotation(Column.class);
            if (columns != null && !columns.ignore()) {
                for (Column obj : columns.columns()) {
                    list.add(new ColumnModel(obj, obj.sortVal(), field));
                }
            } else if (column != null && !column.ignore()) {
                list.add(new ColumnModel(column, column.sortVal(), field));
            }
        }
        Collections.sort(list);
        return list;//排序
    }

    private static String getCellVal(Object obj, ColumnModel model) {
        if (obj != null) {
            Field field = model.field;
            Column column = model.column;
            Object val = ReflectUtil.getFieldValue(obj, field);
            Class convertClazz = column.cellConvert();
            Object newVal = val;
            if (convertClazz == null || convertClazz == Object.class || convertClazz.isInterface()) {
                newVal = val;
            } else if (IEnum.class.isAssignableFrom(convertClazz)) {
                newVal = EnumUtil.getName(convertClazz, val);
            } else if (CellConvert.class.isAssignableFrom(convertClazz)) {
                CellConvert cellConvert = model.cellConvert;
                if (cellConvert == null) {
                    cellConvert = (CellConvert) ReflectUtil.newInstance(convertClazz);
                    model.cellConvert = cellConvert;
                }
                newVal = cellConvert.convert(val, obj, column);
            }
            if (newVal == null) {
                return null;
            } else if (newVal instanceof Date) {
                return TimeUtil.f4YMDHMS((Date) newVal);
            } else {
                return newVal.toString();
            }
        }
        return null;
    }

    private static void getData(List list, Class clazz, List<ColumnModel> columns, List<List<String>> result) {
        for (Object obj : list) {
            if (obj != null) {
                if (clazz.isAssignableFrom(obj.getClass())) {
                    List<String> columnValues = new ArrayList<>(columns.size());
                    for (ColumnModel model : columns) {
                        columnValues.add(getCellVal(obj, model));
                    }
                    result.add(columnValues);
                } else {
                    throw new RuntimeException(obj.getClass() + " is not be cast " + clazz);
                }
            } else {
                result.add(ListUtil.emptyList());
            }
        }
    }

    private static String getTitle(ColumnModel model) {
        String title = model.column.title();
        if (StrUtil.empty(title)) {
            return model.field.getName();
        }
        return title;
    }

    private static class ColumnModel implements Comparable<ColumnModel> {
        private Column column;
        private int idx;
        private Field field;
        private CellConvert cellConvert;

        public ColumnModel(Column column, int idx, Field field) {
            this.column = column;
            this.idx = idx;
            this.field = field;
        }

        @Override

        public int compareTo(ColumnModel o) {
            return this.idx - o.idx;
        }
    }
}
