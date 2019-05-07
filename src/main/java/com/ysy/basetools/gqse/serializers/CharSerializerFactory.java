package com.ysy.basetools.gqse.serializers;

import com.ysy.basetools.gqse.GqSeContext;
import com.ysy.basetools.gqse.io.Input;
import com.ysy.basetools.gqse.io.Output;

/**
 * Created by hanfeng.sheng on 2017/10/24.
 */
public class CharSerializerFactory extends SerializerFactory<Character> {
//
//    public static void main(String[] args) {
//        CharSerializerFactory charFactory = new CharSerializerFactory();
//        Output output = new Output(4096);
//        charFactory.writeObj('a', output, null);
//        Input input = new Input(output.toBytes());
//        Character c = charFactory.readObj(input, null);
//        System.out.println(c);
//    }

    @Override
    public void doWriteObj(Character obj, Output output, GqSeContext context, int depth) {
        output.writeChar(obj);
    }

    @Override
    public Character readObj(Input input, GqSeContext context) {
        return input.readChar();
    }
}
