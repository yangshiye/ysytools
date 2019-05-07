package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by lixiaojian on 2017/10/30.
 */
public class BigDecimalSerializerFactory extends SerializerFactory<BigDecimal> {

    @Override
    public void doWriteObj(BigDecimal obj, Output output, GqSeContext context, int depth) {
        int scale = obj.scale();
        BigInteger bi = obj.unscaledValue();
        byte[] bytes = bi.toByteArray();
        output.writeInt(scale, true);
        output.writeByteArr(bytes);
    }

    @Override
    public BigDecimal readObj(Input input, GqSeContext context) {
        int scale = input.readInt(true);
        BigInteger bi = new BigInteger(input.readByteArr());
        return new BigDecimal(bi, scale);
    }

}
