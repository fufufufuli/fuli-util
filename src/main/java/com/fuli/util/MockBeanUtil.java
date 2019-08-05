package com.fuli.util;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fuli
 */
public class MockBeanUtil {
    /**
     * 自动封装 mock bean
     */
    public static <T> T mockBean(Class<T> clz) {
        Method[] methods = clz.getDeclaredMethods();
        try {
            T t = clz.getDeclaredConstructor().newInstance();
            for (Method method : methods) {
                String name = method.getName();
                if (name.contains("set")) {
                    String fieldName = CommonUtil.toLowerCaseFirstOne(name.substring(3));
                    Field field = clz.getDeclaredField(fieldName);
                    method.invoke(t, makeFiled(fieldName, field, method.getParameterTypes()[0]));
                }
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置字段
     * todo 按照字段名，数据类型 自定义mock 值
     *
     * @param clz
     * @param <T>
     * @return
     */
    private static <T> T makeFiled(String name, Field field, Class<T> clz) throws ClassNotFoundException {
        Object obj;
        if (String.class.equals(clz)) {
            // todo 按照字段名，数据类型 自定义mock 值
            obj = "张三";
        } else if (int.class.equals(clz) || Integer.class.equals(clz)) {
            obj = 123;
        } else if (Long.class.equals(clz) || long.class.equals(clz)) {
            obj = 123;
        } else if (double.class.equals(clz) || Double.class.equals(clz)) {
            obj = 123.00;
        } else if (clz.isArray()) {
            Class<?> componentType = clz.getComponentType();
            String className = componentType.getName();
            Object param1 = makeFiled(className, field, componentType);
            Object param2 = makeFiled(className, field, componentType);
            Object[] objects = (Object[]) Array.newInstance(Class.forName(className), 10);
            objects[0] = param1;
            objects[1] = param2;
            obj = objects;
        } else if (Map.class.equals(clz) || field.getType().isPrimitive()) {
            Map<Object, Object> map = Maps.newHashMap();
            map.put("key", "value");
            obj = map;
        } else if (List.class.equals(clz)) {
            List list = new ArrayList();
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                Class<?> componentType = (Class<?>) pt.getActualTypeArguments()[0];
                String className = componentType.getName();
                Object param1 = makeFiled(className, field, componentType);
                Object param2 = makeFiled(className, field, componentType);
                list.add(param1);
                list.add(param2);
                obj = list;
            } else {
                obj = list;
            }
        } else if (clz.isEnum()) {
            //todo 枚举不处理
            obj = null;
        } else {
            return mockBean(clz);
        }
        return (T) obj;
    }


}
