package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author yanlin.hou@ucarinc.com
 * @description:
 * @className: UrlSerializerFactory
 * @date October 31,2017
 */
public class UrlSerializerFactory extends SerializerFactory<URL> {

    @Override
    public void doWriteObj(URL obj, Output output, GqSeContext context, int depth) {
        output.writeString(obj.toString());
    }

    @Override
    public URL readObj(Input input, GqSeContext context) {
        String url = input.readString();
        try {
            URL result = new URL(url);
            return result;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
//
//    public static void main(String[] args) {
//        try {
//            URL url = new URL("http://www.example.com/docs/resource1.html");
//            Output output = new Output(1024);
//            UrlSerializerFactory urlSerializerFactory = new UrlSerializerFactory();
//            urlSerializerFactory.writeObj(url, output, null);
//            Input in = new Input(output.toBytes());
//            url = urlSerializerFactory.readObj(in, null);
//            System.out.println(url);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//    }
}
