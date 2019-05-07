package com.ysy.basetools.properties;

import com.ysy.basetools.map.SMap;
import com.ysy.basetools.util.*;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by guoqiang on 2017/8/1.
 */
public class PropertiesTool {

    public static <T> T genByClass(String name, Class<T> clazz) {
        return genByClass(name, null, clazz);
    }

    public static <T> T genByClass(String name, String key, Class<T> clazz) {
        SMap<String, String> map = PropertiesUtil.getMap(name);
        PropertiesNode node = PropertiesUtil.getPropertiesNode(map, key);
        return PropertiesTool.genByClass(node, clazz);
    }

    public static <T> T genByClass(PropertiesNode node, Type orgClazz) {
        if (node == null) {//不存在对象时直接创建一个默认值数据返回
            if (orgClazz instanceof Class) {
                return (T) ReflectUtil.newInstance((Class) orgClazz);
            } else {
                return null;
            }
        }
        Type[] atas = null;
        String nodeName = node.getName();
        String nodeValue = node.getValue();
        SMap<String, PropertiesNode> children = node.getChildren();
        Class clazz;
        if (orgClazz == null) {//没有执行类信息
            if (StrUtil.nEmpty(nodeValue)) {
                clazz = ReflectUtil.getClassByName(nodeValue);
            } else {
                clazz = (Class<T>) String.class;
            }
        } else if (orgClazz instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) orgClazz;
            Type k = pType.getRawType();
            atas = pType.getActualTypeArguments();
            if (k instanceof Class) {
                clazz = (Class) k;
            } else {
                clazz = null;
            }
        } else if (orgClazz instanceof Class) {
            clazz = (Class) orgClazz;
        } else {
            clazz = null;
        }
        if (clazz != null) {
            if (clazz.isInterface() || (Modifier.isAbstract(clazz.getModifiers()) && !clazz.isPrimitive())) {//类信息是接口或者抽象类
                if (StrUtil.nEmpty(nodeValue)) {
                    clazz = ReflectUtil.getClassByName(nodeValue);
                } else if (clazz.isAssignableFrom(ArrayList.class)) {
                    clazz = (Class<T>) ArrayList.class;
                } else if (clazz.isAssignableFrom(LinkedHashSet.class)) {
                    clazz = (Class<T>) LinkedHashSet.class;
                } else if (clazz.isAssignableFrom(LinkedHashMap.class)) {
                    clazz = (Class<T>) LinkedHashMap.class;
                } else {
                    return null;//没有该信息直接返回
                }
            }
        }
        Object result;
        if (clazz == null || clazz == Object.class) {
            result = nodeValue;
        } else if (isDirectType(clazz)) {
            result = getDirectValue(clazz, nodeValue);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            result = ReflectUtil.newInstance(clazz);
            if (result != null) {
                Collection list = (Collection) result;
                List<String> dd = ListUtil.sortList(children.keySet());
                Type genType = null;
                if (atas != null && atas.length > 0) {
                    genType = atas[0];
                }
                for (String key : dd) {
                    PropertiesNode child = children.get(key);
                    Object ele = genByClass(child, genType);
                    list.add(ele);
                }
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            result = ReflectUtil.newInstance(clazz);
            if (result != null) {
                Map map = (Map) result;
                for (String key : children.keySet()) {
                    String temp = "";
                    if (node.getName() != null) {
                        temp = key.substring(node.getName().length() + 1);
                    } else {
                        temp = key;
                    }
                    PropertiesNode child = children.get(key);
                    Type genType = null;
                    if (atas != null && atas.length > 1) {
                        genType = atas[1];
                    }
                    if (isDirectType(genType) && child.getValue() == null) {
                        Map tempMap = getDirectMap(child, nodeName, genType);
                        if (ListUtil.nEmpty(tempMap)) {
                            map.putAll(tempMap);
                        }
                    } else {
                        map.put(temp, genByClass(child, genType));
                    }
                }
            }
        } else {
            result = ReflectUtil.newInstance(clazz);
            for (String key : children.keySet()) {
                String temp = "";
                if (node.getName() != null) {
                    temp = key.substring(node.getName().length() + 1);
                } else {
                    temp = key;
                }
                Type fieldClass = ReflectUtil.getFieldGenericType(result, temp);
                PropertiesNode child = children.get(key);
                if (fieldClass != null) {
                    Object tempObj = genByClass(child, fieldClass);
                    ReflectUtil.setFieldValue(result, temp, tempObj);
                }
            }
        }
        return (T) result;

    }


    private static Map getDirectMap(PropertiesNode child, String nodeName, Type type) {
        int idx = 0;
        if (nodeName != null) {
            idx = nodeName.length() + 1;
        }
        List<PropertiesNode> nodes = new LinkedList<PropertiesNode>();
        nodes.add(child);
        ListIterator<PropertiesNode> it = nodes.listIterator();
        while (it.hasNext()) {
            PropertiesNode temp = it.next();
            if (temp.getValue() == null) {
                it.remove();
            }
            for (PropertiesNode node : temp.getChildren().values()) {
                it.add(node);
                it.previous();
            }
        }
        Map map = new HashMap(nodes.size());
        for (PropertiesNode node : nodes) {
            String key = node.getName().substring(idx);
            map.put(key, getDirectValue(type, node.getValue()));
        }
        return map;
    }

    private static Object getDirectValue(Type clazz, String value) {
        Object result = null;
        if (clazz == Object.class || clazz == String.class || clazz == null) {
            result = value;
        } else if (clazz == Long.class || clazz == long.class) {
            result = NumUtil.parseLong(value);
        } else if (clazz == Integer.class || clazz == int.class) {
            result = NumUtil.parseInt(value);
        } else if (clazz == Short.class || clazz == short.class) {
            result = NumUtil.parseShort(value);
        } else if (clazz == Byte.class || clazz == byte.class) {
            result = NumUtil.parseByte(value);
        } else if (clazz == Double.class || clazz == double.class) {
            result = NumUtil.parseDouble(value);
        } else if (clazz == Float.class || clazz == float.class) {
            result = NumUtil.parseFloat(value);
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            result = "true".equalsIgnoreCase(value);
        } else if (clazz == Character.class || clazz == char.class) {
            if (value != null) {
                result = value.charAt(0);
            } else {
                result = null;
            }
        } else if (clazz == BigDecimal.class) {
            result = new BigDecimal(value);
        } else if (clazz == BigInteger.class) {
            result = new BigInteger(value);
        } else if (clazz == Date.class) {
            result = TimeUtil.parseDate(value);
        } else if (clazz == java.sql.Date.class) {
            result = new java.sql.Date(TimeUtil.parseDate(value).getTime());
        }
        return result;
    }

    private static boolean isDirectType(Type type) {
        if (type == String.class) {
            return true;
        } else if (type == Long.class || type == long.class) {
            return true;// NumUtil.parseLong(nodeValue);
        } else if (type == Integer.class || type == int.class) {
            return true;// NumUtil.parseInt(nodeValue);
        } else if (type == Short.class || type == short.class) {
            return true;// NumUtil.parseShort(nodeValue);
        } else if (type == Byte.class || type == byte.class) {
            return true;// NumUtil.parseByte(nodeValue);
        } else if (type == Double.class || type == double.class) {
            return true;// NumUtil.parseDouble(nodeValue);
        } else if (type == Float.class || type == float.class) {
            return true;// NumUtil.parseFloat(nodeValue);
        } else if (type == Boolean.class || type == boolean.class) {
            return true;// "true".equalsIgnoreCase(nodeValue);
        } else if (type == Character.class || type == char.class) {
            return true;// null;
        } else if (type == BigDecimal.class) {
            return true;// new BigDecimal(nodeValue);
        } else if (type == BigInteger.class) {
            return true;// new BigInteger(nodeValue);
        } else if (type == Date.class) {
            return true;// TimeUtil.parseDate(nodeValue);
        } else if (type == java.sql.Date.class) {
            return true;// new java.sql.DateSerializerFactory(TimeUtil.parseDate(nodeValue).getTime());
        }
        return false;
    }
}
