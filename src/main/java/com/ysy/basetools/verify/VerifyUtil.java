package com.ysy.basetools.verify;


import com.ysy.basetools.util.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by guoqiang on 2016/8/8.
 */
public class VerifyUtil {
    public static enum ContrastNumType {GE, GT, LT, LE}

    public static VerifierResult check(Object o, String... arr) {
        return check(o, "", ListUtil.createSet(arr));
    }

    public static VerifierResult check(Object obj, String fieldPath, Set<String> set) {
        if (obj == null) {
            return VerifierResult.err("检查对象是null");
        }
        VerifierResult result = new VerifierResult(VerifierResult.SUCCESS, "检查成功");
        try {
            Class clazz = obj.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                String childFieldPath;
                if (StrUtil.isNotEmpty(fieldPath)) {
                    childFieldPath = fieldPath + "." + field.getName();
                } else {
                    childFieldPath = field.getName();
                }
                if (set != null && set.contains(childFieldPath)) {
                    continue;
                }
                result.setField(field);
                Object value = null;
                value = ReflectUtil.getFieldValue(obj, field);
                Verify verify = field.getAnnotation(Verify.class);
                if (verify != null) {
                    toCheck(result, verify, field, value, clazz, childFieldPath, set, obj);
                }
                if (result.isError()) {
                    return result;
                }
            }
        } catch (Exception e) {
            result.error(null, "检查错误 = " + obj).setE(e);
        }
        return result;
    }

    private static void toCheck(VerifierResult result, Verify verify, Field field, Object value, Class clazz, String childFieldPath, Set<String> set, Object obj) {
        checkSimple(result, verify, field, value, clazz, childFieldPath, set, obj);

        if (result.isSuccess() && verify.minSize() != -1) {
            if (value == null) {
                result.error(field, "field = " + field + " is null can not check minSize");
            } else if (value instanceof Collection && ((Collection) value).size() < verify.minSize()) {
                result.error(field, "field = " + field + " minSize =" + verify.minSize() + ", size = " + ((Collection) value).size());
            } else if (value instanceof Map && ((Map) value).size() < verify.minSize()) {
                result.error(field, "field = " + field + " minSize =" + verify.minSize() + ", size = " + ((Map) value).size());
            } else if (value instanceof String && ((String) value).length() < verify.minSize()) {
                result.error(field, "field = " + field + " minSize =" + verify.minSize() + ", size = " + ((String) value).length());
            } else if (value instanceof StringBuilder && ((StringBuilder) value).length() < verify.minSize()) {
                result.error(field, "field = " + field + " minSize =" + verify.minSize() + ", size = " + ((StringBuilder) value).length());
            } else if (value instanceof StringBuffer && ((StringBuffer) value).length() < verify.minSize()) {
                result.error(field, "field = " + field + " minSize =" + verify.minSize() + ", size = " + ((StringBuffer) value).length());
            } else if (clazz.isArray()) {
                Object temp = ReflectUtil.getFieldValue(value, "length");
                if (temp instanceof Integer && ((Integer) temp) < verify.minSize()) {
                    result.error(field, "field = " + field + " minSize =" + verify.minSize() + ", size = " + temp);
                }
            }
        }
        if (result.isSuccess() && verify.maxSize() != -1) {
            if (value instanceof Collection && ((Collection) value).size() > verify.maxSize()) {
                result.error(field, "field = " + field + " maxSize =" + verify.maxSize() + ", size = " + ((Collection) value).size());
            } else if (value instanceof Map && ((Map) value).size() > verify.maxSize()) {
                result.error(field, "field = " + field + " maxSize =" + verify.maxSize() + ", size = " + ((Map) value).size());
            } else if (value instanceof String && ((String) value).length() > verify.maxSize()) {
                result.error(field, "field = " + field + " maxSize =" + verify.maxSize() + ", size = " + ((String) value).length());
            } else if (value instanceof StringBuilder && ((StringBuilder) value).length() > verify.maxSize()) {
                result.error(field, "field = " + field + " maxSize =" + verify.maxSize() + ", size = " + ((StringBuilder) value).length());
            } else if (value instanceof StringBuffer && ((StringBuffer) value).length() > verify.maxSize()) {
                result.error(field, "field = " + field + " maxSize =" + verify.maxSize() + ", size = " + ((StringBuffer) value).length());
            } else if (clazz.isArray()) {
                Object temp = ReflectUtil.getFieldValue(value, "length");
                if (temp instanceof Integer && ((Integer) temp) > verify.maxSize()) {
                    result.error(field, "field = " + field + " maxSize =" + verify.maxSize() + ", size = " + temp);
                }
            }
        }
        if (result.isSuccess() && verify.instanceFrom() != Object.class) {
            if (!verify.instanceFrom().isAssignableFrom(clazz)) {
                result.error(field, verify.instanceFrom() + " is not assignableFrom " + clazz);
            }
        }
        if (result.isSuccess() && verify.eleInstanceFrom() != Object.class) {
            if (value instanceof Map) {
                for (Object tempValue : ((Map) value).values()) {
                    if (tempValue != null && !verify.eleInstanceFrom().isAssignableFrom(tempValue.getClass())) {
                        result.error(field, verify.eleInstanceFrom() + " is not assignableFrom " + tempValue.getClass());
                        break;
                    }
                }
            } else if (value instanceof Collection) {
                for (Object tempValue : ((Collection) value)) {
                    if (tempValue != null && !verify.eleInstanceFrom().isAssignableFrom(tempValue.getClass())) {
                        result.error(field, verify.eleInstanceFrom() + " is not assignableFrom " + tempValue.getClass());
                        break;
                    }
                }
            } else if (clazz.isArray()) {
                int size = ArrayUtil.getSize(value);
                for (int i = 0; i < size; i++) {
                    Object tempValue = ArrayUtil.getObjByIdx(value, i);
                    if (tempValue != null && !verify.eleInstanceFrom().isAssignableFrom(tempValue.getClass())) {
                        result.error(field, verify.eleInstanceFrom() + " is not assignableFrom " + tempValue.getClass());
                        break;
                    }
                }
            }
        }
        if (result.isSuccess() && verify.keyInstanceFrom() != Object.class && value instanceof Map) {
            for (Object tempKey : ((Map) value).keySet()) {
                if (tempKey != null && !verify.keyInstanceFrom().isAssignableFrom(tempKey.getClass())) {
                    result.error(field, verify.keyInstanceFrom() + " is not assignableFrom " + tempKey.getClass());
                    break;
                }
            }
        }
        if (result.isSuccess() && verify.verifies() != null && verify.verifies().length > 0) {
            for (Class<? extends Verifier> verifierClass : verify.verifies()) {
                if (verifierClass != null) {
                    Verifier verifier = ReflectUtil.newInstance(verifierClass);
                    if (verifier != null) {
                        VerifierResult tempVerifierResult = verifier.verify(value, obj, field);
                        result.error(field, "field = " + field + " verifies is error , msg = " + tempVerifierResult.getMsg());
                    } else {
                        result.error(field, "field = " + field + " verifier can not newInstance, verifierClass = " + verifierClass);
                    }
                } else {
                    result.error(field, "field = " + field + " verifierClass = null");
                }
            }
        }
        if (result.isSuccess() && verify.deepVerify()) {
            if (value != null) {
                VerifierResult tempVerifierResult = check(value, childFieldPath, set);
                if (tempVerifierResult.isError()) {
                    result.error(field, "field = " + field + " deepVerify is error , msg = " + tempVerifierResult.getMsg());
                }
            }
        }
        if (result.isSuccess() && verify.deepVerifyElement()) {
            if (value != null) {
                if (value instanceof Collection) {
                    for (Object ele : ((Collection) value)) {
                        if (ele == null && verify.deepVerifyNotNull()) {
                            result.error(field, "field = " + field + " deepVerifyNotNull");
                        } else {
                            VerifierResult tempVerifierResult = check(ele, childFieldPath, set);
                            if (tempVerifierResult.isError()) {
                                result.error(field, "field = " + field + " deepVerifyElement is error , msg = " + tempVerifierResult.getMsg());
                            }
                        }
                    }
                } else if (clazz.isArray()) {
                    Object[] arr = (Object[]) obj;
                    for (Object ele : arr) {
                        if (ele == null && verify.deepVerifyNotNull()) {
                            result.error(field, "field = " + field + " deepVerifyNotNull");
                        } else {
                            VerifierResult tempVerifierResult = check(ele, childFieldPath, set);
                            if (tempVerifierResult.isError()) {
                                result.error(field, "field = " + field + " deepVerifyElement is error , msg = " + tempVerifierResult.getMsg());
                            }
                        }
                    }
                } else {
                    result.error(field, "field = " + field + " deepVerifyElement is error , value is not a Collection ");
                }
            }
        }
        checkMap(result, verify, field, value, clazz, childFieldPath, set, obj);
    }

    private static void checkMap(VerifierResult VerifierResult, Verify verify, Field field, Object value, Class clazz, String childFieldPath, Set<String> set, Object obj) {
        if (VerifierResult.isSuccess() && verify.deepVerifyMapKey()) {
            if (value != null) {
                if (value instanceof Map) {
                    for (Object ele : ((Map) value).values()) {
                        if (ele == null && verify.deepVerifyNotNull()) {
                            VerifierResult.error(field, "field = " + field + " deepVerifyNotNull");
                        } else {
                            VerifierResult tempVerifierResult = check(ele, childFieldPath, set);
                            if (tempVerifierResult.isError()) {
                                VerifierResult.error(field, "field = " + field + " deepVerifyElement is error , msg = " + tempVerifierResult.getMsg());
                            }
                        }
                    }
                } else {
                    VerifierResult.error(field, "field = " + field + " deepVerifyElement is error , value is not a map ");
                }
            }
        }
        if (VerifierResult.isSuccess() && verify.deepVerifyMapValue()) {
            if (value != null) {
                if (value instanceof Map) {
                    for (Object ele : ((Map) value).keySet()) {
                        if (ele == null && verify.deepVerifyNotNull()) {
                            VerifierResult.error(field, "field = " + field + " deepVerifyNotNull");
                        } else {
                            VerifierResult tempVerifierResult = check(ele, childFieldPath, set);
                            if (tempVerifierResult.isError()) {
                                VerifierResult.error(field, "field = " + field + " deepVerifyElement is error , msg = " + tempVerifierResult.getMsg());
                            }
                        }
                    }
                } else {
                    VerifierResult.error(field, "field = " + field + " deepVerifyElement is error , value is not a map ");
                }
            }
        }

    }

    private static void checkSimple(VerifierResult VerifierResult, Verify verify, Field field, Object value, Class clazz, String childFieldPath, Set<String> set, Object obj) {
        if (VerifierResult.isSuccess() && verify.isNum()) {
            Double d = NumUtil.parseDouble(value);
            if (d == null) {
                VerifierResult.error(field, "field " + field + ",value" + " is not num!");
            }
        }
        if (VerifierResult.isSuccess() && StrUtil.isNotEmpty(verify.ge())) {
            if (!checkRange(field, value, verify.ge(), ContrastNumType.GE)) {
                VerifierResult.error(field, "field " + field + "," + value + " not ge " + verify.ge());
            }
        }
        if (VerifierResult.isSuccess() && StrUtil.isNotEmpty(verify.gt())) {
            if (!checkRange(field, value, verify.gt(), ContrastNumType.GT)) {
                VerifierResult.error(field, "field " + field + "," + value + " not gt " + verify.gt());
            }
        }
        if (VerifierResult.isSuccess() && StrUtil.isNotEmpty(verify.le())) {
            if (!checkRange(field, value, verify.le(), ContrastNumType.LE)) {
                VerifierResult.error(field, "field " + field + "," + value + " not le " + verify.le());
            }
        }
        if (VerifierResult.isSuccess() && StrUtil.isNotEmpty(verify.lt())) {
            if (!checkRange(field, value, verify.lt(), ContrastNumType.LT)) {
                VerifierResult.error(field, "field " + field + "," + value + " not lt() " + verify.lt());
            }
        }
        if (VerifierResult.isSuccess() && verify.notNull()) {
            if (value == null) {
                VerifierResult.error(field, "field = " + field + " is null");
            }
        }
        if (VerifierResult.isSuccess() && verify.nul()) {
            if (value != null) {
                VerifierResult.error(field, "field = " + field + " is not null");
            }
        }
        if (VerifierResult.isSuccess() && verify.empty()) {
            if (value instanceof Collection && !((Collection) value).isEmpty()) {
                VerifierResult.error(field, "field = " + field + " is not empty");
            } else if (value instanceof Map && !((Map) value).isEmpty()) {
                VerifierResult.error(field, "field = " + field + " is not empty");
            } else if (value instanceof String && !((String) value).isEmpty()) {
                VerifierResult.error(field, "field = " + field + " is not empty");
            } else if (value instanceof StringBuilder && ((StringBuilder) value).length() != 0) {
                VerifierResult.error(field, "field = " + field + " is not empty");
            } else if (value instanceof StringBuffer && ((StringBuffer) value).length() != 0) {
                VerifierResult.error(field, "field = " + field + " is not empty");
            } else if (clazz.isArray()) {
                Object temp = ReflectUtil.getFieldValue(value, "length");
                if (temp instanceof Integer && ((Integer) temp) != 0) {
                    VerifierResult.error(field, "field = " + field + " is not empty");
                }
            } else if (value != null) {
                VerifierResult.error(field, "field = " + field + " is not empty");
            }
        }
        if (VerifierResult.isSuccess() && verify.notEmpty()) {
            if (value == null) {
                VerifierResult.error(field, "field = " + field + " is null");
            } else if (value instanceof Collection && ((Collection) value).isEmpty()) {
                VerifierResult.error(field, "field = " + field + " isEmpty");
            } else if (value instanceof Map && ((Map) value).isEmpty()) {
                VerifierResult.error(field, "field = " + field + " isEmpty");
            } else if (value instanceof String && ((String) value).isEmpty()) {
                VerifierResult.error(field, "field = " + field + " isEmpty");
            } else if (value instanceof StringBuilder && ((StringBuilder) value).length() == 0) {
                VerifierResult.error(field, "field = " + field + " isEmpty");
            } else if (value instanceof StringBuffer && ((StringBuffer) value).length() == 0) {
                VerifierResult.error(field, "field = " + field + " isEmpty");
            } else if (clazz.isArray()) {
                Object temp = ReflectUtil.getFieldValue(value, "length");
                if (temp instanceof Integer && ((Integer) temp) == 0) {
                    VerifierResult.error(field, "field = " + field + " isEmpty");
                }
            }
        }
    }


    private static boolean checkRange(Field field, Object value, String contrast, ContrastNumType type) {
        if (type == null) {
            throw new NullPointerException("type can not be null");
        }
        if (StrUtil.isEmpty(contrast)) {
            throw new RuntimeException("contrast can not be empty");
        }
        if (value == null) {
            return true;//空无法判断大小范围
        }
        Class clazz = field.getType();
        if (clazz == int.class || clazz == Integer.class) {
            return NumberVerifyUtil.check((Integer) value, NumUtil.parseInt(contrast), type);
        } else if (clazz == long.class || clazz == Long.class) {
            return NumberVerifyUtil.check((Long) value, NumUtil.parseLong(contrast), type);
        } else if (clazz == short.class || clazz == Short.class) {
            return NumberVerifyUtil.check((Short) value, NumUtil.parseShort(contrast), type);
        } else if (clazz == byte.class || clazz == Byte.class) {
            return NumberVerifyUtil.check((Byte) value, NumUtil.parseByte(contrast), type);
        } else if (clazz == double.class || clazz == Double.class) {
            return NumberVerifyUtil.check((Double) value, NumUtil.parseDouble(contrast), type);
        } else if (clazz == float.class || clazz == Float.class) {
            return NumberVerifyUtil.check((Float) value, NumUtil.parseFloat(contrast), type);
        } else if (value instanceof Date) {
            return NumberVerifyUtil.check(((Date) value).getTime(), TimeUtil.parseDate(contrast).getTime(), type);
        } else {
            throw new RuntimeException("field = " + field + " is can not checkRange");
        }
    }
}
