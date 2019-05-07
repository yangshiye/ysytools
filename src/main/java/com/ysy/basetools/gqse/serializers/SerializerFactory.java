package com.ysy.basetools.gqse.serializers;


import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.exceptions.GqSeEx;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by guoqiang on 2017/10/20.
 * 序列化工厂（定义各种即将序列化的对象类型）
 */
public abstract class SerializerFactory<T> {
    private static final Logger logger = LoggerFactory.getLogger(SerializerFactory.class);

    /**
     * 序列化（供内部调用）：
     * param:要序列化的对象；输出流（序列化后的字节流存放于此）；上下文
     */
    public abstract void doWriteObj(T obj, Output output, GqSeContext context, int depth);

    /**
     * 序列化（供内部调用）：
     * param:要序列化的对象；输出流（序列化后的字节流存放于此）；上下文
     */
    public void writeObj(T obj, Output output, GqSeContext context, int depth) {
        if (context.getMaxSeObjDepth() != null && context.getMaxSeObjDepth() < depth) {
            logger.error("se obj is too deep,depth=" + depth);
            throw new GqSeEx("se obj is too deep,depth=" + depth);
        }
        this.doWriteObj(obj, output, context, depth);
    }

    /**
     * 反序列化（供内部调用）：
     * param:输入流（即将序列后的字节流存放于此）；上下文
     */
    public abstract T readObj(Input input, GqSeContext context);

}
