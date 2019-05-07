package com.ysy.basetools.util;

import java.util.UUID;

/**
 * Created by guoqiang on 2017/6/29.
 */
public class UUIDUtil {
    public static String newUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String args[]){
        try {
            throw new RuntimeException("123");
        }catch (Exception e){
            throw new RuntimeException("321",e);
        }finally {
            System.out.println("finally");
        }
    }
}
