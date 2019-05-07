package com.ysy.basetools.properties;

import com.ysy.basetools.log.LogUtil;
import com.ysy.basetools.map.SMap;
import com.ysy.basetools.util.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by guoqiang on 2015/8/24.
 */
public class PropertiesUtil {
    private static final Logger logger = LogUtil.LOG;
    private static final Map<String, Properties> PROPERTIES_MAP = new ConcurrentHashMap<String, Properties>(64);

    public static Map<String, Object> createByClass(String name) {
        return createByClass(name, HashMap.class);
    }

    public static <T> T createByClass(String name, Class<T> clazz) {
        return createByClass(name, null, clazz);
    }

    public static <T> List<T> createListByClass(String name, String key, Class<T> clazz) {
        SMap<String, String> map = getMap(name);
        PropertiesNode node = getPropertiesNode(map, key);
        List<T> list = new ArrayList<T>(node.getChildren().size());
        for (PropertiesNode temp : node.getChildren().values()) {
            list.add(createObjByNode(temp, clazz));
        }
        return list;
    }

    public static <T> T createByClass(String name, String key, Class<T> clazz) {
        SMap<String, String> map = getMap(name);
        PropertiesNode node = getPropertiesNode(map, key);
        return createObjByNode(node, clazz);
    }

    public static <T> T createObjByNode(PropertiesNode node, Class<T> orgClazz) {
        String nodeName = node.getName();
        String nodeValue = node.getValue();
        SMap<String, PropertiesNode> children = node.getChildren();
        Class<T> clazz = null;
        if (StrUtil.isNotEmpty(nodeValue) && (orgClazz == null || orgClazz.isInterface()
                || (!orgClazz.isPrimitive() && Modifier.isAbstract(orgClazz.getModifiers())))) {
                clazz = ReflectUtil.getClassByName(nodeValue);
        } else if (orgClazz != null && (orgClazz.isInterface() || Modifier.isAbstract(orgClazz.getModifiers()))) {
            if (orgClazz.isAssignableFrom(ArrayList.class)) {
                clazz = (Class<T>) ArrayList.class;
            } else if (orgClazz.isAssignableFrom(LinkedHashSet.class)) {
                clazz = (Class<T>) LinkedHashSet.class;
            } else if (orgClazz.isAssignableFrom(LinkedHashMap.class)) {
                clazz = (Class<T>) LinkedHashMap.class;
            } else {
                clazz = orgClazz;
            }
        } else {
            clazz = orgClazz;
        }
        Object result = null;
        String valueName = null;
        String value = nodeValue;
        if (nodeName != null) {
            valueName = node.getName() + ".value";
        }
        if (orgClazz == null && clazz != null && children.get(valueName) != null) {
            value = children.get(valueName).getValue();
        }
        if (clazz == null || clazz == String.class || clazz == Object.class) {
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
            if (!value.isEmpty()) {
                result = value.charAt(0);
            }
        } else if (clazz == BigDecimal.class) {
            result = new BigDecimal(value);
        } else if (clazz == BigInteger.class) {
            result = new BigInteger(value);
        } else if (clazz == Date.class) {
            result = TimeUtil.parseDate(value);
        } else if (clazz == java.sql.Date.class) {
            result = new java.sql.Date(TimeUtil.parseDate(value).getTime());
        } else if (Collection.class.isAssignableFrom(clazz)) {
            result = ReflectUtil.newInstance(clazz);
            if (result != null) {
                List<String> dd = ListUtil.sortList(children.keySet());
                for (String key : dd) {
                    PropertiesNode child = children.get(key);
                    Object ele = createObjByNode(child, null);
                    ((Collection) result).add(ele);
                }
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            result = ReflectUtil.newInstance(clazz);
            if (result != null) {
                for (String key : children.keySet()) {
                    String temp = "";
                    if (node.getName() != null) {
                        temp = key.substring(node.getName().length() + 1);
                    } else {
                        temp = key;
                    }
                    PropertiesNode child = children.get(key);
                    ((Map) result).put(temp, createObjByNode(child, null));
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
                Class fieldClass = ReflectUtil.getFieldClass(result, temp);
                PropertiesNode child = children.get(key);
                if (fieldClass != null) {
                    Object tempObj = createObjByNode(child, fieldClass);
                    ReflectUtil.setFieldValue(result, temp, tempObj);
                }
            }
        }
        return (T) result;

    }

    public static PropertiesNode getPropertiesNode(SMap<String, String> map, String key) {
        PropertiesNode node = new PropertiesNode(key, map.get(key));
        String prefix = "";
        if (StrUtil.isNotEmpty(key)) {
            prefix = key + ".";
        }
        for (String childKey : map.keySet()) {
            if (childKey.startsWith(prefix)) {
                String childKeyTemp = childKey.substring(prefix.length());
                PropertiesNode t = node;
                String childName = key;
                String[] arr = childKeyTemp.split("[.]");
                for (String s : arr) {
                    if (StrUtil.isNotEmpty(childName)) {
                        childName = childName + "." + s;
                    } else {
                        childName = s;
                    }
                    PropertiesNode t1 = t.getChild(childName);
                    if (t1 == null) {
                        t1 = new PropertiesNode(childName, map.get(childName));
                        t.put(childName, t1);
                    }
                    t = t1;
                }
            }
        }
        return node;
    }

    public static <T> T loadByClass(String name, Class<T> clazz) {
        SMap<String, String> map = getMap(name);
        return loadByClassForMap(clazz, map);
    }

    public static <T> T loadByClass(String name, String key, Class<T> clazz) {
        SMap<String, String> map = getMap(name);
        if (map.isEmpty()) {
            return null;
        }
        String newClass = map.get(key);
        if (StrUtil.isNotEmpty(newClass)) {
            clazz = ReflectUtil.getClassByName(newClass);
            if (clazz == null) {
                logger.error("can not find class = " + newClass
                        + ",name = " + name + ",key = " + key);
                return null;
            }
        }
        final String keyPrefix = key + ".";
        SMap<String, String> objMap = new SMap<String, String>(map.size());
        for (String temp : map.keySet()) {
            if (temp.startsWith(keyPrefix)) {
                objMap.put(temp.substring(keyPrefix.length()), map.get(temp));
            }
        }
        return loadByClassForMap(clazz, objMap);
    }

    private static <T> T loadByClassForMap(Class<T> clazz, SMap<String, String> map) {
        T t = null;
        try {
            t = clazz.newInstance();
            for (String key : map.keySet()) {
                String value = map.get(key);
                setValue(t, key, value);
            }
        } catch (Exception e) {
            logger.error("加载数据错误,class = " + clazz + ",map=" + map, e);
        }
        return t;
    }

    private static void setValue(Object t, String key, String value) throws IllegalAccessException, InstantiationException {
        if (StrUtil.isEmpty(value)) {
            return;
        }
        int len = key.indexOf('.');
        if (len > -1) {
            String currentKey = key.substring(0, len);
            Class clazz = ReflectUtil.getFieldClass(t, currentKey);
            if (clazz != null) {
                Object obj = ReflectUtil.getFieldValue(t, currentKey);
                if (obj == null) {
                    obj = clazz.newInstance();
                    ReflectUtil.setFieldValue(t, currentKey, obj);
                }
                setValue(obj, key.substring(1 + len), value);
            }
        } else {
            Class clazz = ReflectUtil.getFieldClass(t, key);
            if (clazz == String.class) {
                ReflectUtil.setFieldValue(t, key, value);
            } else if (clazz == Long.class || clazz == long.class) {
                ReflectUtil.setFieldValue(t, key, NumUtil.parseLong(value));
            } else if (clazz == Integer.class || clazz == int.class) {
                ReflectUtil.setFieldValue(t, key, NumUtil.parseInt(value));
            } else if (clazz == Short.class || clazz == short.class) {
                ReflectUtil.setFieldValue(t, key, NumUtil.parseShort(value));
            } else if (clazz == Byte.class || clazz == byte.class) {
                ReflectUtil.setFieldValue(t, key, NumUtil.parseByte(value));
            } else if (clazz == Double.class || clazz == double.class) {
                ReflectUtil.setFieldValue(t, key, NumUtil.parseDouble(value));
            } else if (clazz == Float.class || clazz == float.class) {
                ReflectUtil.setFieldValue(t, key, NumUtil.parseFloat(value));
            } else if (clazz == Boolean.class || clazz == boolean.class) {
                ReflectUtil.setFieldValue(t, key, "true".equalsIgnoreCase(value));
            } else if (clazz == Character.class || clazz == char.class) {
                ReflectUtil.setFieldValue(t, key, value.charAt(0));
            } else if (clazz == BigDecimal.class) {
                ReflectUtil.setFieldValue(t, key, new BigDecimal(value));
            } else if (clazz == BigInteger.class) {
                ReflectUtil.setFieldValue(t, key, new BigInteger(value));
            } else if (clazz == Date.class) {
                ReflectUtil.setFieldValue(t, key, TimeUtil.parseDate(value));
            } else if (clazz == java.sql.Date.class) {
                ReflectUtil.setFieldValue(t, key, new java.sql.Date(TimeUtil.parseDate(value).getTime()));
            }
        }
    }

    public static String getVal(String name, String valName) {
        return getMap(name, null).get(valName);
    }

    public static String getVal(InputStream is, String valName) {
        try {
            Properties p = doLoad(is, "UTF-8");
            if (p != null) {
                return p.getProperty(valName);
            }
            return null;
        } catch (IOException e) {
            logger.error("load properties valName = " + valName + " is error", e);
            return null;
        }
    }


    public static SMap<String, String> getMap(String name) {
        return getMap(name, null);
    }

    public static SMap<String, String> getMap(String name, String code) {
        Properties p = doLoad(name, code);
        SMap<String, String> map = new SMap<String, String>(p.size());
        for (String key : p.stringPropertyNames()) {
            map.put(key, p.getProperty(key));
        }
        return map;
    }

    private static Properties doLoad(String name, String code) {
        if (PROPERTIES_MAP.get(name) == null) {
            synchronized (PropertiesUtil.class) {
                if (PROPERTIES_MAP.get(name) == null) {
                    Properties properties = null;
                    try {
                        ClassLoader cl = PropertiesUtil.class.getClassLoader();
                        String path = name;
                        if (name.startsWith("/") || name.startsWith("\\")) {
                            if (cl.getResource(name) == null) {
                                path = name.substring(1);
                            }
                        } else if (cl.getResource("/" + name) != null) {
                            path = "/" + name;
                        }
                        InputStream is = cl.getResourceAsStream(path);
                        if (is != null) {
                            properties = doLoad(is, code);
                            logger.info("load properties = " + name + " is success,value = " + properties);
                        } else {
                            logger.error("load properties is error. name = " + name + " ,path=" + path);
                        }
                    } catch (Exception e) {
                        logger.error("load properties = " + name + " is error", e);
                    }
                    if (properties == null) {
                        properties = new Properties();
                    }
                    PROPERTIES_MAP.put(name, properties);
                }
            }
        }
        return PROPERTIES_MAP.get(name);
    }

    public static Properties doLoad(InputStream is, String code) throws IOException {
        if (is != null) {
            Properties properties = new Properties();
            if (code == null) {
                code = "UTF-8";
            }
            properties.load(new InputStreamReader(is, code));
            return properties;
        }
        return null;
    }

    private PropertiesUtil() {

    }
}
