package com.fuli.util;

import java.util.Iterator;
import java.util.Map;

/**
 * @author fuli
 */
public class CommonUtil {
    public static String splice(Map<String, ?> params, String on, String keyValueSeparator) {
        StringBuilder sb = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            Iterator<? extends Map.Entry<String, ?>> iterator = params.entrySet().iterator();
            Map.Entry<String, ?> next;
            while (iterator.hasNext()) {
                next = iterator.next();
                if (sb.length() != 0) {
                    sb.append(on);
                }
                sb.append(next.getKey()).append(keyValueSeparator).append(next.getValue());
            }
        }
        return sb.toString();
    }

    public static String toLowerCaseFirstOne(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    public static String toUpperCaseFirstOne(String str) {
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
