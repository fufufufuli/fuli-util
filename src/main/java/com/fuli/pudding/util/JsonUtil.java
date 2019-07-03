package com.fuli.pudding.util;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final XmlMapper xmlMapper = new XmlMapper();
    private static final JavaTimeModule javaTimeModule = new JavaTimeModule();

    static {
        //todo 过时方法替换
        mapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        mapper.registerModule(javaTimeModule);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //todo 过时方法替换
        xmlMapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        xmlMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        xmlMapper.registerModule(javaTimeModule);
    }

    /**
     * 把Java对象转为JSON字符串
     * @param obj the object need to transfer into json string.
     * @return json string.
     */
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.error("LK-PC0019a: to json exception.", e);
            throw new JsonException("把对象转换为JSON时出错了", e);
        }
    }

    /**
     * 把JSON转换为Java对象
     * @param json  the json string
     * @param clazz will convert into class
     * @param <T>   return type
     * @return java object.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("LK-PC00186: from json exception", e);
            throw new JsonException(e.getLocalizedMessage());
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
            throw new RuntimeException("convert json error:" + e.getLocalizedMessage());
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
    public static String getValue(String jsonString, String key) {
        try {
            JsonNode value = mapper.readTree(jsonString).findValue(key);
            if (value==null){
                return null;
            }else {
                return value.asText();
            }
        } catch (IOException e) {
            throw new JsonException(JsonException.GET_VALUE_ERROR, e);
        }
    }

    /**
     * 根据key获取json对应values
     */
    public static List<String> getValues(String jsonString, String key) {
        return getValues(jsonString, key, String.class);
    }

    /**
     * 根据key获取json对应value
     */
    public static <T> T getValue(String jsonString, String key, Class<T> clazz) {
        try {
            JsonNode value = mapper.readTree(jsonString).findValue(key);
            if (value == null) {
                return null;
            }
            return fromJson(value.toString(), clazz);
        } catch (IOException e) {
            throw new JsonException(JsonException.GET_VALUE_ERROR, e);
        }
    }

    /**
     * 根据key获取json对应values
     */
    public static <T> List<T> getValues(String jsonString, String key, Class<T> clazz) {
        try {
            if (StringUtils.isNotEmpty(key)) {
                List<JsonNode> values = mapper.readTree(jsonString).findValues(key);
                if (values == null) {
                    return null;
                }
                return fromJson(values.toString(), List.class, clazz);
            } else {
                return fromJson(jsonString, List.class, clazz);
            }
        } catch (IOException e) {
            throw new JsonException(JsonException.GET_VALUE_ERROR, e);
        }
    }

    /**
     * xml转json
     */
    public static String xmlToJson(String xmlString) {
        //剔除xml的注释信息
        xmlString = xmlString.replace("\n", "").replace("(?s)<\\!\\-\\-.+?\\-\\->", "");
        try {
            return toJson(xmlMapper.readTree(xmlString));
        } catch (IOException e) {
            throw new JsonException(JsonException.GET_VALUE_ERROR, e);
        }
    }


    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());

    static {
        YAML_MAPPER.findAndRegisterModules();
    }

    public static <T> T fromYaml(String yaml, Class<T> clazz) {
        try {
            return YAML_MAPPER.readValue(yaml, clazz);
        } catch (IOException e) {
            throw new JsonException(e.getLocalizedMessage());
        }
    }

    public static <T> T fromYaml(File yamlFile, Class<T> clazz) {
        try {
            return YAML_MAPPER.readValue(yamlFile, clazz);
        } catch (IOException e) {
            throw new JsonException(e.getLocalizedMessage());
        }
    }

    /**
     * 获取泛型的Collection Type
     * @param yamlFile        yaml文件
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类型
     */
    public static <T> T fromYamlFile(File yamlFile, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = YAML_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return YAML_MAPPER.readValue(yamlFile, javaType);
        } catch (IOException e) {
            throw new JsonException("convert json error:" + e.getLocalizedMessage());
        }
    }

    public static <T> T fromYamlValue(String yaml, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = YAML_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return YAML_MAPPER.readValue(yaml, javaType);
        } catch (IOException e) {
            throw new JsonException("convert json error:" + e.getLocalizedMessage());
        }
    }

    public static <T> void writeAsYaml(File yamlFile, Class<T> clazz) {
        try {
            YAML_MAPPER.writeValue(yamlFile, clazz);
        } catch (IOException e) {
            throw new JsonException(e.getLocalizedMessage());
        }
    }


}
