package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author yanlin.hou@ucarinc.com
 * @description:
 * @className: UriSerializerFactory
 * @date October 31,2017
 */
public class UriSerializerFactory extends SerializerFactory<URI> {


    @Override
    public void doWriteObj(URI obj, Output output, GqSeContext context, int depth) {
        output.writeString(obj.toString());
    }

    @Override
    public URI readObj(Input input, GqSeContext context) {
        String uri = input.readString();
        try {
            URI result = new URI(uri);
            return result;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
