package com.ysy.basetools.json.impl;


import com.ysy.basetools.json.JSONWriter;
import com.ysy.basetools.json.JSONWriterUtil;
import com.ysy.basetools.json.model.ReflectMap;

/**
 * Created by guoqiang on 2016/10/25.
 */
public class ArrayWriter implements JSONWriter {
    @Override
    public Class getFormatClass() {
        return Object[].class;
    }

    @Override
    public boolean isFormat(Object o) {
        if (o == null) {
            return false;
        }
        return o.getClass().isArray();
    }

    @Override
    public StringBuilder getJSONStr(Object o, StringBuilder builder, String path, ReflectMap map) {
        if (o instanceof int[]) {
            if (((int[]) o).length > 0) {
                builder.append("[");
                for (int i = 0; i < ((int[]) o).length; i++) {
                    if (i != 0) {
                        builder.append(",");
                    }
                    builder.append(((int[]) o)[i]);
                }
                builder.append("]");
                return builder;
            } else {
                return builder.append("[]");
            }
        } else if (o instanceof byte[]) {
            if (((byte[]) o).length > 0) {
                builder.append("[");
                for (int i = 0; i < ((byte[]) o).length; i++) {
                    if (i != 0) {
                        builder.append(",");
                    }
                    builder.append(((byte[]) o)[i]);
                }
                builder.append("]");
                return builder;
            } else {
                return builder.append("[]");
            }
        } else if (o instanceof short[]) {
            if (((short[]) o).length > 0) {
                builder.append("[");
                for (int i = 0; i < ((short[]) o).length; i++) {
                    if (i != 0) {
                        builder.append(",");
                    }
                    builder.append(((short[]) o)[i]);
                }
                builder.append("]");
                return builder;
            } else {
                return builder.append("[]");
            }
        } else if (o instanceof long[]) {
            if (((long[]) o).length > 0) {
                builder.append("[");
                for (int i = 0; i < ((long[]) o).length; i++) {
                    if (i != 0) {
                        builder.append(",");
                    }
                    builder.append(((long[]) o)[i]);
                }
                builder.append("]");
                return builder;
            } else {
                return builder.append("[]");
            }
        } else if (o instanceof float[]) {
            if (((float[]) o).length > 0) {
                builder.append("[");
                for (int i = 0; i < ((float[]) o).length; i++) {
                    if (i != 0) {
                        builder.append(",");
                    }
                    builder.append(((float[]) o)[i]);
                }
                builder.append("]");
                return builder;
            } else {
                return builder.append("[]");
            }
        } else if (o instanceof double[]) {
            if (((double[]) o).length > 0) {
                builder.append("[");
                for (int i = 0; i < ((double[]) o).length; i++) {
                    if (i != 0) {
                        builder.append(",");
                    }
                    builder.append(((double[]) o)[i]);
                }
                builder.append("]");
                return builder;
            } else {
                return builder.append("[]");
            }
        } else if (o instanceof boolean[]) {
            if (((boolean[]) o).length > 0) {
                builder.append("[");
                for (int i = 0; i < ((boolean[]) o).length; i++) {
                    if (i != 0) {
                        builder.append(",");
                    }
                    builder.append(((boolean[]) o)[i]);
                }
                builder.append("]");
                return builder;
            } else {
                return builder.append("[]");
            }
        } else if (o instanceof char[]) {
            if (((char[]) o).length > 0) {
                builder.append("[");
                for (int i = 0; i < ((char[]) o).length; i++) {
                    if (i != 0) {
                        builder.append(",");
                    }
                    builder.append("'").append(((char[]) o)[i]).append("'");
                }
                builder.append("]");
                return builder;
            } else {
                return builder.append("[]");
            }
        } else if (o instanceof Object[]) {
            if (((Object[]) o).length > 0) {
                builder.append("[");
                for (int i = 0; i < ((Object[]) o).length; i++) {
                    if (i != 0) {
                        builder.append(",");
                    }
                    String curPath = JSONWriterUtil.getPath(path, "[" + i + "]");
                    JSONWriterUtil.toJSONStr(((Object[]) o)[i], builder, curPath, map);//TODO
                }
                builder.append("]");
                return builder;
            } else {
                return builder.append("[]");
            }
        }
        return builder.append("null");
    }
}
