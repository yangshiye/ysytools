package com.ysy.basetools.enums;

/**
 * Created by guoqiang on 2017/6/17.
 */
public interface IntStrEnum extends ISEnum {

    int getIntCode();


    default boolean eq(Integer code) {
        if (code == null) {
            return false;
        }
        return code == this.getIntCode();
    }

    default Integer getCode() {
        return getIntCode();
    }

    /**
     private final int code;
     private final String name;

     IntStrEnum(int code, String name) {
        this.code = code;
        this.name = name;
     }

     @Override
     public int getIntCode() {
        return code;
     }

     @Override
     public String getName() {
        return name;
     }



    * */

}
