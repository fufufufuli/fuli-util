package com.fuli.pudding.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: fuli
 * @Date: 2019/4/23 15:42
 */
public class JSONUtil {
    private static final  ObjectMapper mapper = new ObjectMapper();
    private static final  XmlMapper xmlMapper = new XmlMapper();
    private static final JavaTimeModule javaTimeModule = new JavaTimeModule();

    static {
        mapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        mapper.registerModule(javaTimeModule);
        xmlMapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        xmlMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        xmlMapper.registerModule(javaTimeModule);
    }

    /**
     * 把Java对象转为JSON字符串
     *
     * @param obj the object need to transfer into json string.
     * @return json string.
     */
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new JSONException("把对象转换为JSON时出错了", e);
        }
    }

    /**
     * 把JSON转换为Java对象
     *
     * @param json  the json string
     * @param clazz will convert into class
     * @param <T>   return type
     * @return java object.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new JSONException(e.getLocalizedMessage());
        }
    }

    /**
     * 获取泛型的Collection Type
     * @param jsonStr         json字符串
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类型
     */
    public static <T> T fromJson(String jsonStr, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return mapper.readValue(jsonStr, javaType);
        } catch (IOException e) {
            throw new JSONException("convert json error:" + e.getLocalizedMessage());
        }
    }

    /**
     * 把Object对象转换为Class类型的对象
     */
    public static <T> T convert(Object o, Class<T> tClass) {
        return mapper.convertValue(o, tClass);
    }


    public static Map<String, Object> toMap(String json) {
        return fromJson(json, Map.class);
    }

    /**
     * 根据key获取json对应value
     */
    public static String getValue(String jsonString, String key) throws IOException {
        return mapper.readTree(jsonString).findValue(key).asText();
    }

    /**
     * 根据key获取json对应values
     */
    public static List<String> getValues(String jsonString, String key) throws IOException {
        return getValues(jsonString, key, String.class);
    }

    /**
     * 根据key获取json对应value
     */
    public static <T> T getValue(String jsonString, String key, Class<T> clazz) throws IOException {
        return fromJson(mapper.readTree(jsonString).findValue(key).toString(), clazz);
    }

    /**
     * 根据key获取json对应values
     */
    public static <T> List<T> getValues(String jsonString, String key, Class<T> clazz) throws IOException {
        List<JsonNode> values = mapper.readTree(jsonString).findValues(key);
        return fromJson(values.toString(), List.class, clazz);
    }

    /**
     * xml转json
     */
    public static String xmlToJson(String xmlString) throws IOException {
        xmlString = xmlString.replace("\n", "").replace("(?s)<\\!\\-\\-.+?\\-\\->", "");
        JsonNode jsonNode = xmlMapper.readTree(xmlString);
        return toJson(jsonNode);
    }
}
