/*
package com.fuli.disuse;

import com.google.common.collect.Maps;
import org.reflections.Reflections;

import java.util.Map;

*/
/**
 * 根据包路径获取class
 * @author fuli
 *//*

public class ClassUtil {

    */
/**
     * 获取包路径下的所有枚举
     *//*

    @SuppressWarnings("all")
    public static Map<String, Class<? extends Enum>> getEnum(final String... packageName) {
        Map<String, Class<? extends Enum>> result = Maps.newHashMap();
        new Reflections(packageName).getSubTypesOf(Enum.class)
                .forEach(value->result.put(value.getSimpleName(),value));
        return result;
    }

}
*/
