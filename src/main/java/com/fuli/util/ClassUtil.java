package com.fuli.util;

import com.google.common.collect.Maps;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

/**
 * 根据包路径获取class
 * @author fuli
 */
public class ClassUtil {

    /**
     * 获取枚举
     */
    public static Map<String, Class<? extends Enum>> getEnum(String... packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends Enum>> types = reflections.getSubTypesOf(Enum.class);
        Map<String, Class<? extends Enum>> result = Maps.newHashMap();
        types.forEach(value->result.put(value.getSimpleName(),value));
        return result;
    }

}
