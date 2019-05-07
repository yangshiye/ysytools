package com.ysy.basetools.gqse.convert.impl;

import com.ysy.basetools.gqse.convert.Converter;
import com.ysy.basetools.gqse.exceptions.GqSeEx;

/**
 * Created by Administrator on 2018/12/7.
 */
public class CharacterConvert implements Converter<Character> {
    @Override
    public Character convert(Object obj) throws GqSeEx {
        if (obj == null) {
            return null;
        } else if (obj instanceof Character) {
            return (Character) obj;
        } else if (obj instanceof String) {
            if (((String) obj).length() == 1) {
                return ((String) obj).charAt(0);
            }
            throw new GqSeEx("val=" + obj + " can not convert Character");
        }
        throw new GqSeEx("type[" + obj.getClass() + "] can not convert Character");
    }
}
