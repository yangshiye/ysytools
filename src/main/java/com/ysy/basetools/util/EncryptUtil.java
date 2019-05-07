package com.ysy.basetools.util;


import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

/**
 * Created by guoqiang on 2016/5/31.
 */
public final class EncryptUtil {

    public static void main(String args[]) {
        System.out.println(superMd5("123QWW", "ABCDEF"));
        System.out.println(superMd5("123qwe", "ABCDEF"));
    }

    /**
     * 超级MD5加密
     *
     * @param orgText    需要加密的字符串
     * @param encryptStr 秘钥
     * @return
     */
    public static String superMd5(String orgText, String encryptStr) {
        if (orgText == null || encryptStr == null) {
            return null;
        }
        String str1 = getMD5Str(orgText);
        String str2 = aes128Encrypt(str1, encryptStr);
        String str3 = getMD5Str(str2);
        return str3;
    }

    /**
     * AES128加密
     */
    public static String aes128Encrypt(String sourceText, String key) {
        if (sourceText == null) {
            return null;
        } else {
            return AESUtil.encrypt(sourceText, key);
        }
    }

    /**
     * AES128解密
     */
    public static String aes128Decrypt(String cipherText, String key) {
        if (StrUtil.isBlank(cipherText)) {
            return cipherText;
        } else {
            return AESUtil.decrypt(cipherText, key);
        }
    }

    /**
     * 将一个long类型数字加密
     *
     * @param value
     * @return
     */
    public static String encryptForBase64(long value) {
        return encrypt(Base64.encodeBase64String(NumUtil.toBytes(value)));
    }

    /**
     * 将encryptForBase64加密的数据解密
     *
     * @param str
     * @return
     */
    public static Long base64DecodeToLong(String str) {
        byte[] bytes = Base64.decodeBase64(decode(str));
        return NumUtil.parseFromBytes(bytes);
    }

    /**
     * md5加密
     *
     * @param str
     * @return
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            return str;
        }

        byte[] byteArray = messageDigest.digest();

        StringBuilder md5StrBuff = new StringBuilder();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString().toUpperCase();
    }

    public static String superEncrypt(String src) {
        src = encrypt(src);
        src = getMD5Str(src);
        src = encrypt(src);
        src = getMD5Str(src);
        return src;
    }


    public static String decode(String src) {
        StringBuilder builder = new StringBuilder(src.length());
        for (char c : src.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                if (c == 'A') {
                    c = 'Z';
                } else {
                    c--;
                }
            } else if (c >= 'a' && c <= 'z') {
                if (c == 'z') {
                    c = 'a';
                } else {
                    c++;
                }
            } else if (c >= '0' && c <= '9') {
                c = (char) ('0' + '9' - c);
            }
            builder.append(c);
        }
        return builder.reverse().toString();
    }

    /**
     * 自我加密算法
     *
     * @param src
     * @return
     */
    public static String encrypt(String src) {
        StringBuilder builder = new StringBuilder(src.length());
        for (char c : src.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                if (c == 'Z') {
                    c = 'A';
                } else {
                    c++;
                }
            } else if (c >= 'a' && c <= 'z') {
                if (c == 'a') {
                    c = 'z';
                } else {
                    c--;
                }
            } else if (c >= '0' && c <= '9') {
                c = (char) ('0' + '9' - c);
            }
            builder.append(c);
        }
        return builder.reverse().toString();
    }

    private EncryptUtil() {

    }
}
