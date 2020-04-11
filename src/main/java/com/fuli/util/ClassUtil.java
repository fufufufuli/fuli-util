package com.fuli.util;

import com.google.common.collect.Maps;
import org.reflections.Reflections;

import java.util.Map;

/**
 * 根据包路径获取class
 * @author fuli
 */
public class ClassUtil {

    /**
     * 获取包路径下的所有枚举
     */
    @SuppressWarnings("all")
    public static Map<String, Class<? extends Enum>> getEnum(String... packageName) {
        Reflections reflections = new Reflections(packageName);
        Map<String, Class<? extends Enum>> result = Maps.newHashMap();
        reflections.getSubTypesOf(Enum.class).forEach(value->result.put(value.getSimpleName(),value));
        return result;
    }

}
