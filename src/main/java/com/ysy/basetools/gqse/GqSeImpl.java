package com.ysy.basetools.gqse;

import com.ysy.basetools.gqse.constant.Constant;
import com.ysy.basetools.gqse.creators.CommonCreatorImpl;
import com.ysy.basetools.gqse.creators.Creator;
import com.ysy.basetools.gqse.exceptions.GqSeEx;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;
import com.ysy.basetools.gqse.serializers.*;
import com.ysy.basetools.log.LogUtil;
import com.ysy.basetools.map.SMap;
import com.ysy.basetools.model.Res;
import com.ysy.basetools.util.ReflectUtil;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by guoqiang on 2017/10/18.
 */
public class GqSeImpl implements GqSe {
    private static final Logger logger = LogUtil.getLog(GqSeImpl.class);
    private final int gqSeId;
    private final byte[] gqSeIdBytes;
    private final SerializerFactory DEFAULT_SERIALIZER_FACTORY = new DefaultSerializerFactory(this);
    private final ConcurrentMap<Integer, SerializerFactory> idMap = new ConcurrentHashMap<Integer, SerializerFactory>();
    private final ConcurrentMap<String, Integer> typeMap = new ConcurrentHashMap<String, Integer>();
    private final ConcurrentMap<String, Class> classMap = new ConcurrentHashMap<String, Class>();
    private final Creator DEFAULT_CREATOR = new CommonCreatorImpl();
    private static GqSe GQSE;

    @Override
    public <T> T createObj(Class type, GqSeContext context) {
        T t = (T) DEFAULT_CREATOR.create(type, context);
        return t;
    }

    @Override
    public byte[] serialize(Object obj, Output output, GqSeContext context) {
        this.writeObj(obj, output, context, 0);
        return output.toBytes();
    }

    @Override
    public void writeObj(Object obj, Output output, GqSeContext context, int depth) {
        if (context == null || output == null) {
            throw new RuntimeException("数据错误context=null||output==null");
        }
        Class clazz = ReflectUtil.getClass(obj);
        context.appSeInfo(clazz);
        try {
            int factoryId = getIdByObj(obj);
            output.writeInt(factoryId, true);
            SerializerFactory serializerFactory = getSerializerFactoryById(factoryId);
            serializerFactory.writeObj(obj, output, context, depth);
        } catch (Throwable e) {
            if (e instanceof GqSeEx) {
                throw (GqSeEx) e;
            } else {
                logger.error("", e);
                throw new RuntimeException("se obj is error,info=" + context.getSeInfo(), e);
            }
        } finally {
            context.removeSeInfo();
        }
    }

    @Override
    public <T> T deserialize(Input input, GqSeContext context) {
        int factoryId = input.readInt(true);
        SerializerFactory serializerFactory = getSerializerFactoryById(factoryId);
        return (T) serializerFactory.readObj(input, context);
    }

    @Override
    public Class getClassByName(String name) {
        Class clazz = classMap.get(name);
        if (clazz == null) {
            clazz = ReflectUtil.getClassByName(name);
            if (clazz == null) {
                throw new RuntimeException("getclass is error ,name=" + name);
            }
            classMap.put(name, clazz);
        }
        return clazz;
    }

    public SerializerFactory getSerializerFactoryById(int factoryId) {
        SerializerFactory factory = idMap.get(factoryId);
        if (factory != null) {
            return factory;
        }
        return DEFAULT_SERIALIZER_FACTORY;
    }

    private int getIdByObj(Object obj) {
        if (obj == null) {
            return Constant.NULL_ID;
        }
        Class type = obj.getClass();
        Integer id = typeMap.get(type.getName());
        if (id != null) {
            return id;
        } else if (type.isEnum()) {
            return Constant.ENUM_ID;
        } else if (type.isArray()) {
            if (type.getComponentType() == byte.class) {
                return Constant.ONE_BYTE_ARRAY_ID;
            } else {
                return Constant.ARRAY_ID;
            }
        } else if (Collection.class.isAssignableFrom(type)) {
            return Constant.COMMON_COLLECTION;
        } else if (Map.class.isAssignableFrom(type)) {
            return Constant.COMMON_MAP;
        }
        return Constant.OBJ_ID;
    }

    private void initFactory() {
        addFactory(Constant.OBJ_ID, DEFAULT_SERIALIZER_FACTORY, Object.class);
        addFactory(Constant.NULL_ID, new NullSerializerFactory());//null
//        addFactory(Constant.REFERENCE_ID, new RenSerializerFactory());//内部引用类型
        addFactory(Constant.INTEGER_ID, new IntSerializerFactory(), Integer.class, int.class);//
        addFactory(Constant.LONG_ID, new LongSerializerFactory(), Long.class, long.class);
        addFactory(Constant.BYTE_ID, new ByteSerializerFactory(), Byte.class, byte.class);
        addFactory(Constant.SHORT_ID, new ShortSerializerFactory(), Short.class, short.class);
        addFactory(Constant.DOUBLE_ID, new DoubleSerializerFactory(), Double.class, double.class);
        addFactory(Constant.FLOAT_ID, new FloatSerializerFactory(), Float.class, float.class);
        addFactory(Constant.CHARACTER_ID, new CharSerializerFactory(), Character.class, char.class);
        addFactory(Constant.BOOLEAN_ID, new BooleanSerializerFactory(), Boolean.class, boolean.class);
        addFactory(Constant.STRING_ID, new StrSerializerFactory(), String.class);
        addFactory(Constant.ENUM_ID, new EnumSerializerFactory(this));
        addFactory(Constant.DATE_ID, new DateSerializerFactory(), Date.class);//日期类型
        addFactory(Constant.ARRAY_ID, new ArraySerializerFactory(this));//1维数组
        addFactory(Constant.ONE_BYTE_ARRAY_ID, new OneByteArraySerializerFactory(), byte[].class);//1维字节数组
        addFactory(Constant.COMMON_COLLECTION, new CommonCollectionSerializerFactory(this), Collection.class);
        addFactory(Constant.COMMON_MAP, new CommonMapSerializerFactory(this), Map.class);
        addFactory(Constant.STRINGBUFF_ID, new StringBufferSerializerFactory(), StringBuffer.class);
        addFactory(Constant.STRINGBUILDER_ID, new StringBuilderSerializerFactory(), StringBuilder.class);
        addFactory(Constant.SMAP_ID, new SMapSerializerFactory(this), SMap.class);
        addFactory(Constant.RES_ID, DEFAULT_SERIALIZER_FACTORY, Res.class);
        addFactory(Constant.STACK_ELE_TRACE_ID, new StackTraceElementSerializerFactory(), StackTraceElement.class);
        addFactory(Constant.HASHSET_ID, new HashSetSerializerFactory(this), HashSet.class);
        addFactory(Constant.HASHMAP_ID, new HashMapSerializerFactory(this), HashMap.class);
        addFactory(Constant.HASHTABLE_ID, new HashTableSerializerFactory(this), Hashtable.class);
        addFactory(Constant.UUID_ID, new UuidSerializerFactory(), UUID.class);
        addFactory(Constant.BIGINTEGER_ID, new BigIntegerSerializerFactory(), BigInteger.class);
        addFactory(Constant.URI_ID, new UriSerializerFactory(), URI.class);
        addFactory(Constant.URL_ID, new UrlSerializerFactory(), URL.class);
        addFactory(Constant.CONCUR_HASHMAP_ID, new ConcurrentHashMapSerializerFactory(this), ConcurrentHashMap.class);

        addFactory(Constant.SQL_DATE_ID, new SQLDateSerializerFactory(), java.sql.Date.class);
        addFactory(Constant.TIMESTAMP_ID, new TimestampSerializerFactory(), Timestamp.class);
        addFactory(Constant.BIGDECIMAL_ID, new BigDecimalSerializerFactory(), BigDecimal.class);
        addFactory(Constant.CLASS_ID, new ClassSerializerFactory(this), Class.class);
        addFactory(Constant.FIELD_ID, new FieldSerializerFactory(this), Field.class);
        addFactory(Constant.METHOD_ID, new MethodSerializerFactory(this), Method.class);
        addFactory(Constant.PACKAGE_ID, new PackageSerializerFactory(), Package.class);
        addFactory(Constant.ARRAYLIST_ID, new ArrayListSerializerFactory(this), ArrayList.class);
        addFactory(Constant.LINKEDLIST_ID, new LinkedListSerializerFactory(this), LinkedList.class);

    }

    public void addFactory(int id, SerializerFactory factory, Class... types) {
        if (factory != null) {
            idMap.putIfAbsent(id, factory);
            this.addTypes(id, types);
        }
    }

    public void addTypes(int id, Class... types) {
        if (types != null) {
            for (Class type : types) {
                typeMap.putIfAbsent(type.getName(), id);
            }
        }
    }

    public void addTypes(int id, String... types) {
        if (types != null) {
            for (String type : types) {
                typeMap.putIfAbsent(type, id);
            }
        }
    }

    public static GqSe getGQSE() {
        GqSe se = GQSE;
        if (se == null) {
            synchronized (GqSeImpl.class) {
                se = GQSE;
                if (se == null) {
                    se = new GqSeImpl(0);
                    GQSE = se;
                }
            }
        }
        return se;
    }

    @Override
    public int getGqSeId() {
        return gqSeId;
    }

    @Override
    public byte[] getGqSeIdBytes() {
        return gqSeIdBytes;
    }

    private GqSeImpl(int gqSeId) {
        this.gqSeId = gqSeId;
        this.gqSeIdBytes = Output.intToBytes(gqSeId, true);
        this.initClass();
        this.initFactory();
    }

    private void initClass() {
        classMap.put("int", int.class);
        classMap.put("long", long.class);
        classMap.put("byte", byte.class);
        classMap.put("short", short.class);
        classMap.put("float", float.class);
        classMap.put("double", double.class);
        classMap.put("char", char.class);
        classMap.put("boolean", boolean.class);
    }
}
