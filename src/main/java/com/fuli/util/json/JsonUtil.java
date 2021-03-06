package com.fuli.util.json;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fuli.util.Commons;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.fuli.util.json.JsonException.CONVERT_ERROR;

/**
 * @author fuli
 */
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final XmlMapper XML_MAPPER = new XmlMapper();
    private static final JavaTimeModule JAVA_TIME_MODULE = new JavaTimeModule();

    static {
        OBJECT_MAPPER.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        OBJECT_MAPPER.registerModule(JAVA_TIME_MODULE);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        XML_MAPPER.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        XML_MAPPER.registerModule(JAVA_TIME_MODULE);
    }

    /**
     * @param obj the object need to transfer into json string.
     * @return json string.
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            throw new JsonException("object to json error", e);
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
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new JsonException(e.getLocalizedMessage());
        }
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param json            json
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类型
     */
    public static <T> T fromJson(String json, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new JsonException(CONVERT_ERROR, e);
        }
    }


    public static <T> T convert(Object o, Class<T> tClass) {
        return OBJECT_MAPPER.convertValue(o, tClass);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String json) {
        return fromJson(json, Map.class);
    }

    /**
     * 根据key获取json对应value
     */
    public static String getValue(String jsonString, String key) {
        try {
            JsonNode value = OBJECT_MAPPER.readTree(jsonString).findValue(key);
            return value == null ? null : value.asText();
        } catch (IOException e) {
            throw new JsonException(CONVERT_ERROR, e);
        }
    }

    /**
     * 根据key获取json对应values
     */
    public static List<String> getValues(String json, String key) {
        return getValues(json, key, String.class);
    }

    /**
     * 根据key获取json对应value
     */
    public static <T> T getValue(String json, String key, Class<T> clazz) {
        try {
            JsonNode value = OBJECT_MAPPER.readTree(json).findValue(key);
            return value == null ? null : fromJson(value.toString(), clazz);
        } catch (IOException e) {
            throw new JsonException(CONVERT_ERROR, e);
        }
    }

    /**
     * 根据key获取json对应values
     */
    public static <T> List<T> getValues(String json, String key, Class<T> clazz) {
        if (Commons.isEmpty(key)) return fromJson(json, List.class, clazz);
        try {
            List<JsonNode> values = OBJECT_MAPPER.readTree(json).findValues(key);
            return Commons.isEmpty(values) ? null : fromJson(values.toString(), List.class, clazz);
        } catch (IOException e) {
            throw new JsonException(CONVERT_ERROR, e);
        }
    }

    /**
     * xml转json
     */
    public static String xmlToJson(String xml) {
        try {
            return toJson(XML_MAPPER.readTree(xml.replace("\n", "")
                    .replace("(?s)<\\!\\-\\-.+?\\-\\->", "")));
        } catch (IOException e) {
            throw new JsonException(CONVERT_ERROR, e);
        }
    }

}
