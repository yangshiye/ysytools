package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.exceptions.GqSeEx;
import com.ysy.basetools.gqse.field.*;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;
import com.ysy.basetools.log.LogUtil;
import com.ysy.basetools.util.ReflectUtil;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guoqiang on 2017/10/19.
 */
public class CommonSerializer<T> implements Serializer<T> {
    private static final Logger logger = LogUtil.getLog(CommonSerializer.class);

    private final Class type;
    private final GqSe gqSe;
    private final Map<String, FieldOper> map = new HashMap<String, FieldOper>();

    @Override
    public void writeObj(Object doWriteObj, Output output, GqSeContext context, int depth) {
        if (doWriteObj == null) {
            return;
        }
        Output.Data fieldSizeData = output.appendData();
        int fieldSize = 0;
        for (FieldOper fieldOper : map.values()) {
            Object value = fieldOper.getValue(doWriteObj);
            //当value是自己的父引用时则代表循环引用
            if (value == doWriteObj) {
                throw new GqSeEx("obj ref is loop,class=" + ReflectUtil.getClass(doWriteObj)
                        + ",field=" + fieldOper.getFullName());
            } else if (value != null) {
                output.writeString(fieldOper.getFullName());
                Output.Data fieldLenData = output.appendData();
                int total = output.getTotal();
                try {
                    context.appSeInfo(fieldOper.getFullName());
                    gqSe.writeObj(value, output, context, depth + 1);
                } finally {
                    context.removeSeInfo();
                }
                int fieldLen = output.getTotal() - total;
                fieldLenData.setInt(fieldLen);
                fieldSize++;
            }
        }
        fieldSizeData.setInt(fieldSize);
    }

    @Override
    public <V> V readObj(Input input, GqSeContext context) {
        V v = gqSe.createObj(type, context);
        int fieldSize = input.readInt(true);
        Map<String, FieldOper> temp = new HashMap<String, FieldOper>(map);
        for (int i = 0; i < fieldSize; i++) {
            String fieldName = input.readString();
            int fieldLen = input.readInt(true);
            FieldOper field = temp.remove(fieldName);
            if (field != null) {
                Object obj = gqSe.deserialize(input, context);
                //TODO 数据格式不不兼容则自动转换 目前不打开
//                if (context.isConvertOpen()) {
//                    Class filedClass = field.getField().getType();
//                    //当类型不兼容时进行转换
//                    if (obj != null && filedClass != null && !filedClass.isAssignableFrom(obj.getClass())) {
//                        try {
//                            obj = ConvertUtil.convert(obj, filedClass);
//                        } catch (Exception e) {
//                            throw new GqSeEx("convert is err,field=" + field.getField(), e);
//                        }
//                    }
//                }
                field.setValue(v, obj);
            } else {
                input.skip(fieldLen);
            }
        }
        if (!temp.isEmpty()) {
            for (FieldOper field : temp.values()) {
                field.setDefaultValue(v);
            }
        }
        return v;
    }

    public CommonSerializer(GqSe gqSe, Class type) {
        this.type = type;
        this.gqSe = gqSe;
        this.init();
    }

    private void init() {
        int i = 0;
        for (Class clazz = type; clazz != null; clazz = clazz.getSuperclass(), i++) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers()) &&
                        field.getAnnotation(NoSerialize.class) == null) {
                    String fullName;
                    if (i == 0) {
                        fullName = field.getName();
                    } else {
                        fullName = i + "." + field.getName();
                    }
                    this.addFiled(field, fullName);
                }
            }
        }
    }

    private void addFiled(Field field, String fullName) {
        FieldOper oper;

        if (field.getType() == int.class) {
            oper = new IntFieldOper(fullName, field);
        } else if (field.getType() == long.class) {
            oper = new LongFieldOper(fullName, field);
        } else if (field.getType() == byte.class) {
            oper = new ByteFieldOper(fullName, field);
        } else if (field.getType() == double.class) {
            oper = new DoubleFieldOper(fullName, field);
        } else if (field.getType() == boolean.class) {
            oper = new BooleanFieldOper(fullName, field);
        } else if (field.getType() == short.class) {
            oper = new ShortFieldOper(fullName, field);
        } else if (field.getType() == char.class) {
            oper = new CharFieldOper(fullName, field);
        } else if (field.getType() == float.class) {
            oper = new FloatFieldOper(fullName, field);
        } else {
            oper = new ObjFieldOper(fullName, field);
        }
        this.map.put(oper.getFullName(), oper);
    }
}
