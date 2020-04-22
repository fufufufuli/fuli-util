package com.fuli.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author fuli
 */
public class Commons {

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isEmpty(Collection<?> data) {
        return Objects.isNull(data) || data.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> data) {
        return !isEmpty(data);
    }

    public static boolean isEmpty(Object[] data) {
        return Objects.isNull(data) || data.length == 0;
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

    /**
     * 用法同Guava的Joiner
     */
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
        return Character.isLowerCase(str.charAt(0)) ? str : Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    public static String toUpperCaseFirstOne(String str) {
        return Character.isUpperCase(str.charAt(0)) ? str : Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static <T> List<T> toBean(Class<T> clazz, List<Map<String, ?>> data) {
        Preconditions.checkArgument(Commons.isEmpty(data));
        List<T> tar = Lists.newArrayList();
        data.forEach(map -> tar.add(toBean(clazz, map)));
        return tar;
    }

    public static <T> T toBean(Class<T> clazz, Map<String, ?> data) {
        Preconditions.checkArgument(Objects.nonNull(data));
        T tar = newInstance(clazz);
        doCopy(data, tar);
        return tar;
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new CommonsException("根据class创建实例化bean失败", e);
        }
    }

    private static <T> void doCopy(Map<String, ?> data, T tar) {
        try {
            PropertyDescriptor[] descriptors = getDescriptors(tar.getClass());
            for (PropertyDescriptor pd : descriptors) {
                if (data.containsKey(pd.getName())) {
                    pd.getWriteMethod().invoke(tar, data.get(pd.getName()));
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new CommonsException("JavaBean对象转化成Map出错", e);
        }
    }


    public static Map<String, ?> toMap(Object bean) {
        Preconditions.checkArgument(Objects.nonNull(bean));
        Map<String, ?> map = Maps.newHashMap();
        buildMap(map, bean);
        return map;
    }

    /**
     * JavaBean对象转化成Map对象
     */
    public static Map<String, String> toMapStr(Object bean) {
        Preconditions.checkArgument(Objects.nonNull(bean));
        Map<String, String> map = Maps.newHashMap();
        buildMap(map, bean);
        return map;
    }

    @SuppressWarnings("rawtypes,unchecked")
    private static void buildMap(Map map, Object javaBean) {
        try {
            PropertyDescriptor[] descriptors = getDescriptors(javaBean.getClass());
            for (PropertyDescriptor pd : descriptors) {
                if ("class".equals(pd.getName())) continue;
                map.put(pd.getName(), pd.getReadMethod().invoke(javaBean));
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new CommonsException("JavaBean对象转化成Map出错", e);
        }
    }

    public static <K, V> Map<K, V> merge(List<Map<K, V>> maps) {
        Map<K, V> map = Maps.newHashMap();
        maps.forEach(m -> m.keySet().forEach(s -> map.put(s, m.get(s))));
        return map;
    }


    public static Map<String, String> trans(Map<String, ?> map) {
        Map<String, String> result = Maps.newHashMap();
        map.forEach((k, v) -> result.put(k, v == null ? null : v.toString()));
        return result;
    }

    private static PropertyDescriptor[] getDescriptors(Class<?> clazz) throws IntrospectionException {
        return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
    }
}
