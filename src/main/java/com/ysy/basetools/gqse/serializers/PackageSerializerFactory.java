package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by lixiaojian on 2017/10/30.
 */
public class PackageSerializerFactory extends SerializerFactory<Package> {
//
//    public static void main(String[] args) {
//        Package a = Thread.currentThread().getClass().getPackage();
//        System.out.println(JSONUtil.json(a));
//
//        PackageSerializerFactory psf = new PackageSerializerFactory();
//        Output output = new Output(1024);
//        psf.writeObj(a,output,null);
//        Input in = new Input(output.toBytes());
//        System.out.println(JSONUtil.json(psf.readObj(in,null)));
//
//    }

    @Override
    public void doWriteObj(Package obj, Output output, GqSeContext context, int depth) {
        output.writeString(obj.getName());
    }

    @Override
    public Package readObj(Input input, GqSeContext context) {
        return Package.getPackage(input.readString());
    }
}
