package com.ysy.basetools.gqse;

import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;
import com.ysy.basetools.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by guoqiang on 2017/10/19.
 */
public class GqSeUtil {
    private static final Logger logger = LoggerFactory.getLogger(GqSeUtil.class);
    private static final GqSe DEFAULT_GQSE = GqSeImpl.getGQSE();
    private static final ConcurrentMap<Integer, GqSe> map = new ConcurrentHashMap<Integer, GqSe>(64);
    private static final ThreadLocal<GqSeContext> contextMap = new ThreadLocal<GqSeContext>();

    public static byte[] serialize(Object obj) {
        GqSeContext context = getContext();
        byte[] bytes;
        try {
            bytes = serialize(obj, context);
        } catch (Throwable e) {
            logger.error("serialize is error,obj.class="+ ReflectUtil.getClass(obj),new Exception());
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            context.clear();
        }
        return bytes;
    }

    public static byte[] serialize(Object obj, GqSeContext context) {
        return serialize(DEFAULT_GQSE, obj, context);
    }

    public static byte[] serialize(GqSe gq, Object obj, GqSeContext context) {
        Output output = new Output(1024);
        if (context.needGqSeId()) {
            output.writeBytes(DEFAULT_GQSE.getGqSeIdBytes());
        }
        return gq.serialize(obj, output, context);
    }

    public static <T> T deserialize(byte[] bytes) {
        GqSeContext context = getContext();
        T t;
        try {
            t = deserialize(bytes, context);
        } finally {
            context.clear();
        }
        return t;
    }

    public static <T> T deserialize(byte[] bytes, GqSeContext context) {
        Input input = new Input(bytes);
        GqSe se = DEFAULT_GQSE;
        if (context.needGqSeId()) {
            int id = input.readInt(true);
            se = getGqSe(id);
            if (se == null) {
                throw new RuntimeException("can not find gqse,id=" + id);
            }
        }
        return se.deserialize(input, context);
    }

    public static GqSe getGqSe(int id) {
        if (id == 0) {
            return DEFAULT_GQSE;
        }
        return map.get(id);
    }

    public static void addGqSe(int id, GqSe gqSe) {
        if (id < 1 || gqSe == null) {
            throw new RuntimeException("add id or gqse is error,id=" + id + ",gqse=" + gqSe);
        }
        if (map.putIfAbsent(id, gqSe) != null) {
            throw new RuntimeException("has id=" + id);
        }
    }

    private static GqSeContext getContext() {
        GqSeContext context = contextMap.get();
        if (context == null) {
            context = GqSeContext.buildDefaultContext();
            contextMap.set(context);
        }
        return context;
    }
}
