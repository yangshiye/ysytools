package com.ysy.basetools.enums;

import com.ysy.basetools.model.V4;
import com.ysy.basetools.util.CommonUtil;
import com.ysy.basetools.util.ListUtil;
import com.ysy.basetools.util.NumUtil;
import com.ysy.basetools.util.ReflectUtil;

import java.util.*;

/**
 * Created by guoqiang on 2017/6/17.
 * 枚举工具类
 */
public class EnumUtil {
    private static final Map<Class<? extends IEnum>, V4<Map, Map, Map, Map>> map = new HashMap<>(128);

    public static <C> boolean anyEqCode(C code, IEnum<C, ?>... enums) {
        if (enums != null) {
            for (IEnum<C, ?> iEnum : enums) {
                if (iEnum.eq(code)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <C> boolean anyNotEqCode(C code, IEnum<C, ?>... enums) {
        return !anyEqCode(code, enums);
    }

    public static <C> boolean same(IEnum<C, ?> iEnum, C c) {
        if (iEnum == null) {
            return false;
        }
        boolean flag = CommonUtil.eq(iEnum.getCode(), c);
        if (flag) {
            return true;
        } else if (ReflectUtil.isDecimal(iEnum.getCode())) {
            return NumUtil.eq((Number) iEnum.getCode(), NumUtil.pDouble(c));
        } else if (ReflectUtil.isIntNum(iEnum.getCode())) {
            return NumUtil.eq((Number) iEnum.getCode(), NumUtil.pLong(c));
        }
        return false;
    }

    public static <C, D> List<IEnum<C, D>> getList(Class<? extends IEnum<C, D>> clazz) {
        return new ArrayList<IEnum<C, D>>(doGetCD(clazz).values());
    }

    public static <C, D> List<IEnum<C, D>> getList(String clazzName) {
        Class clazz = ReflectUtil.getClassByName(clazzName);
        return new ArrayList<IEnum<C, D>>(doGetCD(clazz).values());
    }

    public static <C, D> D getName(Class<? extends IEnum<C, D>> clazz, C c) {
        IEnum<C, D> commonEnum = getEnum(clazz, c);
        if (commonEnum != null) {
            return commonEnum.getName();
        }
        return null;
    }

    public static <C, D> D getName(String className, C c) {
        Class<? extends IEnum<C, D>> clazz = (Class<? extends IEnum<C, D>>) ReflectUtil.getClassByName(className);
        return (D) getName(clazz, c);
    }

    public static <C, D> String getName4Str(String className, String c) {
        Class<? extends IEnum<C, D>> clazz = (Class<? extends IEnum<C, D>>) ReflectUtil.getClassByName(className);
        IEnum<C, D> i = getEnumByCodeStr(clazz, c);
        if (i != null) {
            return String.valueOf(i.getName());
        }
        return null;
    }

    public static <C, D> D getName(IEnum<C, D> iEnum, C c) {
        if (iEnum != null) {
            Class<? extends IEnum<C, D>> clazz = (Class<? extends IEnum<C, D>>) iEnum.getClass();
            return getName(clazz, c);
        }
        return null;
    }

    public static <T extends IEnum<C, D>, C, D> C getCode(Class<T> clazz, D d) {
        IEnum<C, D> i = getEnumByName(clazz, d);
        if (i != null) {
            return i.getCode();
        }
        return null;
    }

    public static <T extends IEnum<C, D>, C, D> T getEnum(Class<T> clazz, C c) {
        return getEnumByCode(clazz, c);
    }

    public static <T extends IEnum<C, D>, C, D> T getEnumByCode(Class<T> clazz, C c) {
        T result = null;
        Map<C, IEnum<C, D>> map = doGetCD(clazz);
        if (map != null) {
            result = (T) map.get(c);
        }
        return result;
    }


    public static <T extends IEnum<C, D>, C, D> T getEnumByName(Class<T> clazz, D d) {
        T result = null;
        Map<D, IEnum<C, D>> map = doGetDC(clazz);
        if (map != null) {
            result = (T) map.get(d);
        }
        return result;
    }

    public static <T extends IEnum<C, D>, C, D> T getEnumByCodeStr(Class<T> clazz, String code) {
        T result = null;
        Map<String, IEnum<C, D>> map = doGetCDStr(clazz);
        if (map != null) {
            result = (T) map.get(code);
        }
        return result;
    }

    public static <T extends IEnum<C, D>, C, D> T getEnumByNameStr(Class<T> clazz, String name) {
        T result = null;
        Map<String, IEnum<C, D>> map = doGetDCStr(clazz);
        if (map != null) {
            result = (T) map.get(name);
        }
        return result;
    }

    private static <C, D> V4<Map<C, IEnum<C, D>>, Map<D, IEnum<C, D>>, Map<String, IEnum<C, D>>, Map<String, IEnum<C, D>>> doGet(Class<? extends IEnum<C, D>> clazz) {
        V4<Map<C, IEnum<C, D>>, Map<D, IEnum<C, D>>, Map<String, IEnum<C, D>>, Map<String, IEnum<C, D>>> result;
        if (clazz == null || !clazz.isEnum()) {
            result = new V4<>(ListUtil.<C, IEnum<C, D>>emptyMap(),
                    ListUtil.<D, IEnum<C, D>>emptyMap(), ListUtil.emptyMap(), ListUtil.emptyMap());
            map.put(clazz, (V4) result);
        } else {
            V4 temp = map.get(clazz);
            if (temp == null) {
                synchronized (clazz) {
                    temp = map.get(clazz);
                    if (temp == null) {
                        Object[] arr = clazz.getEnumConstants();
                        if (arr == null) {
                            arr = new Object[0];
                        }
                        Map<C, IEnum<C, D>> cd = new LinkedHashMap<C, IEnum<C, D>>(arr.length);
                        Map<D, IEnum<C, D>> dc = new LinkedHashMap<D, IEnum<C, D>>(arr.length);
                        Map<String, IEnum<C, D>> cdStr = new LinkedHashMap<String, IEnum<C, D>>(arr.length);
                        Map<String, IEnum<C, D>> dcStr = new LinkedHashMap<String, IEnum<C, D>>(arr.length);
                        for (Object obj : arr) {
                            if (obj instanceof IEnum) {
                                IEnum<C, D> ienum = (IEnum<C, D>) obj;
                                cd.put(ienum.getCode(), ienum);
                                dc.put(ienum.getName(), ienum);
                                cdStr.put(String.valueOf(ienum.getCode()), ienum);
                                dcStr.put(String.valueOf(ienum.getName()), ienum);
                            }
                        }
                        temp = new V4(cd, dc, cdStr, dcStr);
                    }
                }
            }
            result = temp;
            map.put(clazz, (V4) result);
        }
        return result;
    }


    private static <C, D> Map<C, IEnum<C, D>> doGetCD(Class<? extends IEnum<C, D>> clazz) {
        V4<Map<C, IEnum<C, D>>, Map<D, IEnum<C, D>>, Map<String, IEnum<C, D>>, Map<String, IEnum<C, D>>> v4 = doGet(clazz);
        if (v4 == null) {
            return (Map<C, IEnum<C, D>>) ListUtil.emptyList();
        }
        return v4.getV1();
    }

    private static <C, D> Map<D, IEnum<C, D>> doGetDC(Class<? extends IEnum<C, D>> clazz) {
        V4<Map<C, IEnum<C, D>>, Map<D, IEnum<C, D>>, Map<String, IEnum<C, D>>, Map<String, IEnum<C, D>>> v4 = doGet(clazz);
        if (v4 == null) {
            return (Map<D, IEnum<C, D>>) ListUtil.emptyList();
        }
        return v4.getV2();
    }

    private static <C, D> Map<String, IEnum<C, D>> doGetCDStr(Class<? extends IEnum<C, D>> clazz) {
        V4<Map<C, IEnum<C, D>>, Map<D, IEnum<C, D>>, Map<String, IEnum<C, D>>, Map<String, IEnum<C, D>>> v4 = doGet(clazz);
        if (v4 == null) {
            return (Map<String, IEnum<C, D>>) ListUtil.emptyList();
        }
        return v4.getV3();
    }

    private static <C, D> Map<String, IEnum<C, D>> doGetDCStr(Class<? extends IEnum<C, D>> clazz) {
        V4<Map<C, IEnum<C, D>>, Map<D, IEnum<C, D>>, Map<String, IEnum<C, D>>, Map<String, IEnum<C, D>>> v4 = doGet(clazz);
        if (v4 == null) {
            return (Map<String, IEnum<C, D>>) ListUtil.emptyList();
        }
        return v4.getV4();
    }

}
