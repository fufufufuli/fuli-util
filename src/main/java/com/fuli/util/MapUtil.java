package com.fuli.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author fuli
 */
public class MapUtil {

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    /**
     * Map集合对象转化成 JavaBean集合对象
     *
     * @param clazz   JavaBean实例对象
     * @param mapList Map数据集对象
     */
    public static <T> List<T> map2Java(Class<T> clazz, List<Map> mapList) {
        if (mapList == null || mapList.isEmpty()) {
            return null;
        }
        List<T> objectList = new ArrayList<>();
        mapList.forEach(map -> objectList.add(map2Java(clazz, map)));
        return objectList;
    }

    /**
     * Map对象转化成 JavaBean对象
     *
     * @param map Map对象
     */
    public static <T> T map2Java(Class<T> clazz, Map map) {
        try {
            T obj = clazz.newInstance();
            doCopy(map, obj);
            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MapException("根据class创建实例化bean失败", e);
        }
    }

    private static <T> void doCopy(Map map, T obj) {
        try {
            PropertyDescriptor[] propertyDescriptors = getPropertyDescriptor(obj.getClass());
            if (isNotEmpty(propertyDescriptors)) {
                for (PropertyDescriptor pd : propertyDescriptors) {
                    if (map.containsKey(pd.getName())) {
                        pd.getWriteMethod().invoke(obj, map.get(pd.getName()));
                    }
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new MapException("JavaBean对象转化成Map出错", e);
        }
    }

    public static void copyPropertiesToBean(Map<String, Object> data, Object target) {
        Preconditions.checkArgument(Objects.nonNull(data));
        Preconditions.checkArgument(Objects.nonNull(target));

        doCopy(data, target);
    }

    private static boolean isNotEmpty(PropertyDescriptor[] propertyDescriptors) {
        return propertyDescriptors != null && propertyDescriptors.length > 0;
    }

    /**
     * JavaBean对象转化成Map对象
     */
    public static Map<String, Object> java2Map(Object javaBean) {
        Map<String, Object> map = Maps.newHashMap();
        toMap(map, javaBean, false);
        return map;
    }

    /**
     * JavaBean对象转化成Map对象
     */
    public static Map<String, String> java2MapStr(Object javaBean) {
        Map<String, String> map = Maps.newHashMap();
        toMap(map, javaBean, true);
        return map;

    }

    private static void toMap(Map map, Object javaBean, boolean isString) {
        try {
            PropertyDescriptor[] propertyDescriptors = getPropertyDescriptor(javaBean.getClass());
            if (isNotEmpty(propertyDescriptors)) {
                for (PropertyDescriptor pd : propertyDescriptors) {
                    if (!"class".equals(pd.getName())) {
                        map.put(pd.getName(), isString ? String.valueOf(pd.getReadMethod().invoke(javaBean))
                                : pd.getReadMethod().invoke(javaBean));
                    }
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new MapException("JavaBean对象转化成Map出错", e);
        }
    }

    /**
     * 将多个map封装到一个map中
     */
    public static <K, V> Map<K, V> merge(List<Map<K, V>> maps) {
        Map<K, V> map = new HashMap<>();
        maps.forEach(m -> m.keySet().forEach(s -> map.put(s, m.get(s))));
        return map;
    }


    public static Map<String, String> trans(Map<String, ?> map) {
        Map<String, String> result = new HashMap<>(16);
        map.forEach((k, v) -> result.put(k, v == null ? null : v.toString()));
        return result;
    }


    private static PropertyDescriptor[] getPropertyDescriptor(Class clazz) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        return beanInfo.getPropertyDescriptors();
    }

}
