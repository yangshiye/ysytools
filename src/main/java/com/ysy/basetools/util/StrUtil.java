package com.ysy.basetools.util;

import com.alibaba.fastjson.JSON;
import com.ysy.basetools.json.JSONUtil;
import com.ysy.basetools.json.ObjFieldJSONUtil;
import com.ysy.basetools.log.LogUtil;
import com.ysy.basetools.thread.ThreadUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by guoqiang on 2015/6/29.
 */
public final class StrUtil {

    public static String objToStr(Object o){
        return ObjFieldJSONUtil.json(o);
    }

    public static String getStrFormUTF8Bytes(byte []bytes){
        if(bytes==null){
            return null;
        }else{
            try {
                return new String(bytes,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                LogUtil.LOG.error("getStrFormUTF8Bytes is error,bytes=" + Arrays.toString(bytes), e);
                return null;
            }
        }
    }

    public static byte[] getUTF8Bytes(String s) {
        try {
            return s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtil.LOG.error("getUTF8Bytes is error,s=" + s, e);
            return null;
        }
    }

    public static String convertUnicode(String ori) {
        if (isEmpty(ori)) {
            return "";
        }
        char aChar;
        int len = ori.length();
        StringBuilder outBuffer = new StringBuilder(len);
        for (int x = 0; x < len; ) {
            aChar = ori.charAt(x++);
            if (aChar == '\\') {
                aChar = ori.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = ori.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);

        }
        return outBuffer.toString();
    }

    public static int length(String str) {
        if (str == null) {
            return 0;
        } else {
            return str.length();
        }
    }

    public static List<String> split(String src, String... splits) {
        List<String> list = new LinkedList<String>();
        if (src == null || src.length() < 1) {
            return list;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < src.length(); ) {
            boolean flag = true;
            if (splits != null) {
                for (String s : splits) {
                    if (s != null && src.startsWith(s, i)) {
                        i += s.length();
                        if (builder.length() > 0) {
                            list.add(builder.toString());
                            builder = new StringBuilder();
                        }
                        flag = false;
                        break;
                    }
                }
            }
            if (i < src.length() && flag) {
                builder.append(src.charAt(i));
                i++;
            }
        }
        if (builder.length() > 0) {
            list.add(builder.toString());
        }
        return list;
    }

    public static String getStatementParameter(String str, Object... objects) {
        if (!isBlank(str)) {
            if (objects != null && objects.length > 0) {
                for (int i = 0; i < objects.length; i++) {
                    str = str.replace("${" + i + "}", objects[i] == null ? ""
                            : objects[i].toString());
                }
            }
        }
        return str;
    }

    /**
     * 格式化参数方便打印日志
     *
     * @param objects
     * @return
     */
    public static String formatParam(Object... objects) {
        StringBuilder builder = new StringBuilder(1024);
        builder.append("param{");
        if (ListUtil.isNotEmpty(objects)) {
            for (int i = 0; i < objects.length; i += 2) {
                if (i != 0) {
                    builder.append(",");
                }
                Object o1 = objects[i];
                builder.append(o1).append("=");
                if ((i + 1) < objects.length) {
                    builder.append(objects[i + 1]);
                }
            }
        }
        builder.append("}");
        return builder.toString();
    }

    /**
     * 获取异常打印信息
     *
     * @param e
     * @return
     */
    public static String toString(Throwable e) {
        StringWriter sw = new StringWriter(1024);
        e.printStackTrace(new PrintWriter(sw));
        return sw.getBuffer().toString();
    }

    public static String toString(Object obj) {
        if (obj instanceof Date) {
            return TimeUtil.formatAllTime((Date) obj);
        } else if (obj instanceof Throwable) {
            return toString((Throwable) obj);
        } else if (obj instanceof StackTraceElement) {
            return obj.toString();
        } else {
            return JSONUtil.toString(obj);
        }
    }

    public static String fillBlank(Object o, int length) {
        StringBuilder s = new StringBuilder(length);
        s.append(String.valueOf(o));
        while (s.length() < length) {
            s.append(" ");
        }
        return s.toString();
    }

    public static String fillBlankPre(Object o, int length) {
        StringBuilder s = new StringBuilder(length);
        String str = String.valueOf(o);
        while (s.length() + str.length() < length) {
            s.append(" ");
        }
        s.append(str);
        return s.toString();
    }

    public static String toJSONString(Object o) {
        return JSONUtil.toString(o);
    }

    /**
     * build如果已经有数据则添加 逗号
     *
     * @param builder
     * @param value
     */
    public static void appendAndPreComma(StringBuilder builder, String value) {
        if (builder.length() > 1) {
            builder.append(",");
        }
        builder.append(value);
    }

    public static String formatJSON(String template, Object... objects) {
        return format(template, FormatType.JSON, objects);
    }

    public static String formatString(String template, Object... objects) {
        return format(template, FormatType.STRING, objects);
    }

    /**
     * 针对模板内容和后续参数进行格式化
     *
     * @param template
     * @param objects
     * @return
     */
    public static String format(String template, FormatType type, Object... objects) {
        StringBuilder builder = new StringBuilder(template.length() * 2);
        for (int i = 0; i < template.length(); i++) {
            char c = template.charAt(i);
            int next = i + 1;
            //如果是特殊字符并且后续还有数值那么直接输出后面的字符 并且索引向后+1
            if (c == '\\' && next < template.length()) {
                char nextC = template.charAt(next);
                builder.append(nextC);
                i = next;
                continue;
            } else if (c == '[') {//如果是遇到[ 开始那么则是需要进行后续参数的传入
                int index = template.indexOf(']', next);//必须后面有] 才是有值的范围
                if (index != -1) {
                    String temp = template.substring(next, index);
                    Integer objIndex = NumUtil.parseInt(temp);
                    if (objIndex != null) {
                        builder.append(toString(objIndex - 1, type, objects));
                        i = index;
                        continue;
                    }
                }
            }
            builder.append(c);
        }
        return builder.toString();
    }

    public static boolean isNotEmpty(String s) {
        return nEmpty(s);
    }

    public static boolean isEmpty(String s) {
        return empty(s);
    }

    public static boolean nEmpty(String s) {
        return s != null && s.length() > 0;
    }

    public static boolean empty(String s) {
        return !nEmpty(s);
    }

    public static boolean isBlank(String str) {
        if (isEmpty(str))
            return true;
        for (int i = 0; i < str.length(); i++)
            if (!Character.isWhitespace(str.charAt(i)))
                return false;

        return true;
    }

    private static String toString(int i, FormatType type, Object... objects) {
        String s = null;
        if (objects != null && i >= 0 && i < objects.length) {
            if (type == FormatType.JSON) {
                s = JSON.toJSONString(objects[i]);
            } else if (type == FormatType.STRING) {
                s = String.valueOf(objects[i]);
            }
        }
        return String.valueOf(s);
    }

    public static String getStackDesc(StackTraceElement[] stack, int start, int offset) {
        if (stack == null) {
            return "stack is null";
        }
        int len = stack.length;
        if (offset < 1 || len < 1 || start < 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder((50 * len) + 10);
        for (int i = start; i < offset && i < len; i++) {
            builder.append(stack[i]).append("\n");
        }
        return builder.toString();
    }

    public static String getStackDesc(StackTraceElement[] stack) {
        return getStackDesc(stack, 0, stack.length);
    }

    public static String getString(Object o) {
        if (o instanceof Date) {
            return TimeUtil.formatAllTime((Date) o);
        }
        return o == null ? null : o.toString();
    }

    public static String getNotNull(Object obj) {
        if (obj != null) {
            return obj.toString();
        }
        return "";
    }

    public static enum FormatType {
        JSON, STRING;
    }

    private StrUtil() {

    }

    /**
     * Description: 去掉字符串前后的空格
     *
     * @param value
     * @return
     */
    public static String toTrim(String value) {
        return null == value ? null : value.trim();
    }

    /**
     * Description: 只剩下中文英文字母及数字
     *
     * @param character
     * @return
     */
    public static String filterChineseAlphabetNumber(String character) {
        if (isEmpty(character)) return character;
        return character.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "");
    }

    public static String notNull(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 输出异常在当前项目的堆栈信息
     *
     * @param e
     * @return
     */
    public static String getCurStack(Throwable e) {
        if (e == null) {
            return "exception is null";
        }
        StackTraceElement[] stacks = e.getStackTrace();
        int offset = ThreadUtil.findCurStack(stacks);
        return e.getMessage() + "==>" + getStackDesc(stacks, 0, offset + 1);
    }

}
