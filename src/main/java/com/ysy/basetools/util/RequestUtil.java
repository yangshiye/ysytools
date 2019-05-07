package com.ysy.basetools.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guoqiang on 2017/5/12.
 */
public class RequestUtil {
    public static Map<String, String> getParamMap(Map<String, String[]> map) {
        if (map == null) {
            return ListUtil.emptyMap();
        }
        Map<String, String> result = new HashMap<String, String>(map.size());
        for (String key : map.keySet()) {
            String value = null;
            String[] obj = map.get(key);
            if (obj != null && obj.length > 0) {
                value = obj[0];
            }
            result.put(key, value);
        }
        return result;
    }
}
