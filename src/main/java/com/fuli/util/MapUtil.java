package com.fuli.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static <T> List<T> map2Bean(Class<T> clazz, List<Map> mapList) {
        if (mapList == null || mapList.isEmpty()) {
            return null;
        }
        List<T> beanList = new ArrayList<>();
        mapList.forEach(map -> beanList.add(map2Bean(clazz, map)));
        return beanList;
    }

    /**
     * Map对象转化成 JavaBean对象
     *
     * @param map Map对象
     */
    public static <T> T map2Bean(Class<T> clazz, Map map) {
        try {
            // 获取javaBean属性
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            // 创建 JavaBean 对象
            T obj = clazz.newInstance();
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (isNotEmpty(propertyDescriptors)) {
                String propertyName; // javaBean属性名
                Object propertyValue; // javaBean属性值
                for (PropertyDescriptor pd : propertyDescriptors) {
                    propertyName = pd.getName();
                    if (map.containsKey(propertyName)) {
                        propertyValue = map.get(propertyName);
                        pd.getWriteMethod().invoke(obj, propertyValue);
                    }
                }
                return obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isNotEmpty(PropertyDescriptor[] propertyDescriptors) {
        return propertyDescriptors != null && propertyDescriptors.length > 0;
    }

    /**
     * JavaBean对象转化成Map对象
     *
     * @author jqlin
     */
    public static Map<String, Object> bean2Map(Object javaBean) {
        Map<String, Object> map = null;
        try {
            // 获取javaBean属性
            BeanInfo beanInfo = Introspector.getBeanInfo(javaBean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            map = new HashMap<>(16);
            if (isNotEmpty(propertyDescriptors)) {
                String propertyName; // javaBean属性名
                Object propertyValue; // javaBean属性值
                for (PropertyDescriptor pd : propertyDescriptors) {
                    propertyName = pd.getName();
                    if (!"class".equals(propertyName)) {
                        Method readMethod = pd.getReadMethod();
                        propertyValue = readMethod.invoke(javaBean);
                        map.put(propertyName, propertyValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * JavaBean对象转化成Map对象
     *
     * @author jqlin
     */
    public static Map<String, String> bean2MapStr(Object javaBean) {
        Map<String, String> map = null;
        try {
            // 获取javaBean属性
            BeanInfo beanInfo = Introspector.getBeanInfo(javaBean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            map = new HashMap<>(16);
            if (isNotEmpty(propertyDescriptors)) {
                String propertyName; // javaBean属性名
                String propertyValue; // javaBean属性值
                Method readMethod;
                for (PropertyDescriptor pd : propertyDescriptors) {
                    propertyName = pd.getName();
                    if (!"class".equals(propertyName)) {
                        readMethod = pd.getReadMethod();
                        propertyValue = String.valueOf(readMethod.invoke(javaBean));
                        map.put(propertyName, propertyValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
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

}