package com.ysy.basetools.gqse;

import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by guoqiang on 2017/10/19.
 */
public interface GqSe {
    /*序列化的版本Id*/
    int getGqSeId();

    byte[] getGqSeIdBytes();

    /*反序列化时创建的对象*/
    <T> T createObj(Class type, GqSeContext context);

    /**
     * 序列化（供外界调用）：
     * param:要序列化的对象；是否需要序列化的版本号；上下文
     */
    byte[] serialize(Object obj, Output output, GqSeContext context);

    /**
     * 序列化（供内部调用）：
     * param:要序列化的对象；输出流（序列化后的字节流存放于此）；上下文
     */
    void writeObj(Object obj, Output output, GqSeContext context, int depth);

    /**
     * 反序列化（供外界调用）：
     * param:输入流（即将序列后的字节流存放于此）；上下文
     */
    <T> T deserialize(Input input, GqSeContext context);

    Class getClassByName(String name);

}
