package com.fuli.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * @author fuli
 */
public class CommonUtil {

    public static boolean isEmpty(Collection<?> data) {
       return Objects.isNull(data)||data.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> data) {
        return !isEmpty(data);
    }

    public static boolean isEmpty(Object[] data) {
        return Objects.isNull(data)||data.length==0;
    }
    public static boolean isNotEmpty(Object[] data) {
        return !isEmpty(data);
    }
    /**
     * 只有继承了AbstractMap的类才实现了Collection接口
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
    public static String splice(Map<String, ?> params, String on, String keyValueSeparator) {
        StringBuilder sb = new StringBuilder();
        if (isNotEmpty(params)) {
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
