package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;
import com.ysy.basetools.model.V2;

import java.lang.reflect.Array;

/**
 * Created by guoqiang on 2017/10/27.
 */
public class ArraySerializerFactory extends SerializerFactory {
    private final GqSe gqSe;

    @Override
    public void doWriteObj(Object obj, Output output, GqSeContext context, int depth) {
        Class<?> clazz = obj.getClass();
        V2<Class, Integer> info = getDimension(clazz);
        Class componentType = info.getV1();
        context.writeClass(output, componentType);
        int dimension = info.getV2();
        output.writeInt(dimension, true);//写入维度
        writeDimension(obj, dimension, output, context, depth);
    }

    private void writeDimension(Object obj, int dimension, Output output, GqSeContext context, int depth) {
        if (obj instanceof int[]) {
            output.writeInt(((int[]) obj).length, true);
            output.writeInts((int[]) obj, true);
        } else if (obj instanceof long[]) {
            output.writeInt(((long[]) obj).length, true);
            output.writeLongs((long[]) obj, true);
        } else if (obj instanceof boolean[]) {
            output.writeInt(((boolean[]) obj).length, true);
            output.writeBooleans((boolean[]) obj);
        } else if (obj instanceof char[]) {
            output.writeInt(((char[]) obj).length, true);
            output.writeChars((char[]) obj);
        } else if (obj instanceof float[]) {
            output.writeInt(((float[]) obj).length, true);
            output.writeFloats((float[]) obj);
        } else if (obj instanceof short[]) {
            output.writeInt(((short[]) obj).length, true);
            output.writeShorts((short[]) obj);
        } else if (obj instanceof byte[]) {
            output.writeInt(((byte[]) obj).length, true);
            output.writeBytes((byte[]) obj);
        } else if (obj instanceof double[]) {
            output.writeInt(((double[]) obj).length, true);
            output.writeDoubles((double[]) obj);
        } else if (obj instanceof String[]) {
            output.writeInt(((String[]) obj).length, true);
            output.writeStrings((String[]) obj);
        } else {
            Object[] arr = (Object[]) obj;
            int len = arr.length;
            output.writeInt(len, true);
            if (dimension == 1) {
                for (Object anArr : arr) {
                    this.gqSe.writeObj(anArr, output, context, depth + 1);
                }
            } else {
                int newDimension = dimension - 1;
                for (Object anArr : arr) {
                    writeDimension(anArr, newDimension, output, context, depth + 1);
                }
            }
        }
    }

    @Override
    public Object readObj(Input input, GqSeContext context) {
        Class componentType = context.readClass(input, gqSe);
        int dimension = input.readInt(true);
        Object obj = readObjArr(dimension, componentType, input, context);
        return obj;
    }

    private Object readObjArr(int dimension, Class componentType, Input input, GqSeContext context) {
        int len = input.readInt(true);
        if (dimension == 1) {
            if (componentType == int.class) {
                return input.readInts(len, true);
            } else if (componentType == long.class) {
                return input.readLongs(len, true);
            } else if (componentType == boolean.class) {
                return input.readBooleans(len);
            } else if (componentType == char.class) {
                return input.readChars(len);
            } else if (componentType == float.class) {
                return input.readFloats(len);
            } else if (componentType == short.class) {
                return input.readShorts(len);
            } else if (componentType == byte.class) {
                return input.readBytes(len);
            } else if (componentType == double.class) {
                return input.readDoubles(len);
            } else if (componentType == String.class) {
                return input.readStrings(len);
            }
        }
        Object[] obj = createArr(componentType, dimension, len);

        if (dimension == 1) {
            for (int i = 0; i < len; i++) {
                obj[i] = this.gqSe.deserialize(input, context);
            }
        } else {
            for (int i = 0; i < len; i++) {
                obj[i] = readObjArr(dimension - 1, componentType, input, context);
            }
        }
        return obj;
    }

    private Object[] createArr(Class componentType, int dimension, int len) {
        int[] arrDimension = new int[dimension];
        arrDimension[0] = len;
        return (Object[]) Array.newInstance(componentType, arrDimension);
    }

    private V2<Class, Integer> getDimension(Class type) {
        int dimension = 0;
        while (type != null && type.isArray()) {
            dimension++;
            type = type.getComponentType();
        }
        return new V2<Class, Integer>(type, dimension);
    }

    public ArraySerializerFactory(GqSe gqSe) {
        this.gqSe = gqSe;
    }
}
