package com.xxxx.admin.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageReusltUtil {

    public static Map<String, Object> getResult(Long total, List<?> records) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("count", total);
        result.put("data", records);
        result.put("code", 0);
        result.put("msg", "");
        return result;
    }
}
