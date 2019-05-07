package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSe;
import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;
import com.ysy.basetools.map.SMap;

import java.util.Map;

/**
 * Created by changpan on 2017/10/30.
 */
public class SMapSerializerFactory extends SerializerFactory<SMap<?, ?>> {

    private final GqSe gqSe;

    @Override
    public void doWriteObj(SMap<?, ?> obj, Output output, GqSeContext context, int depth) {
        output.writeInt(obj.size(), true);
        for (Map.Entry temp : obj.entrySet()) {
            gqSe.writeObj(temp.getKey(), output, context, depth + 1);
            gqSe.writeObj(temp.getValue(), output, context, depth + 1);
        }
    }

    @Override
    public SMap<?, ?> readObj(Input input, GqSeContext context) {
        int len = input.readInt(true);
        SMap smap = new SMap();
        for (int i = 0; i < len; i++) {
            Object key = gqSe.deserialize(input, context);
            Object value = gqSe.deserialize(input, context);
            smap.put(key, value);
        }
        return smap;
    }

    public SMapSerializerFactory(GqSe gqSe) {
        this.gqSe = gqSe;
    }
}
