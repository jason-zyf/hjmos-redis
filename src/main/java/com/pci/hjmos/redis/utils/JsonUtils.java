package com.pci.hjmos.redis.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class JsonUtils {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }

    /**
     * 将java-bean对象转换成json字符串
     *
     * @param bean java-bean
     * @return String
     */
    public static String toJsonStr(Object bean) {

        if (bean == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            log.error("JsonUtils#toJsonStr(Object) catch a exception -> ",e);

            return null;
        }
    }

    public static String toJsonStr(String bean) {

        if (bean == null) {
            return null;
        }

        return bean;
    }

    /**
     * 将java-bean对象转换成json byte
     *
     * @param bean java-bean
     * @return String
     */
    public static byte[] toJsonByte(Object bean) {

        if (bean == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsBytes(bean);
        } catch (JsonProcessingException e) {
            log.error("JsonUtils#toJsonByte(Object) catch a exception -> ",e);

            return null;
        }
    }

    /**
     * 将list转换成json字符串
     *
     * @param list list
     * @return String
     */
    public static String toJsonStr(List<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("JsonUtils#toJsonStr(List) catch a exception -> ",e);

            return null;
        }
    }

    /**
     * 将map转换成json字符串
     *
     * @param map map
     * @return String
     */
    public static String toJsonStr(Map<?, ?> map) {

        if (CollectionUtils.isEmpty(map)) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("JsonUtils#toJsonStr(Map) catch a exception -> ",e);

            return null;
        }
    }

    /**
     * 将java字符串转换成java对象
     *
     * @param jsonString json 字符串
     * @param beanClass  class
     * @return <T>
     */
    public static <T> T toBean(String jsonString, Class<T> beanClass) {

        if (StringUtils.isEmpty(jsonString) || beanClass == null) {
            return null;
        }

        try {
            return objectMapper.readValue(jsonString, beanClass);
        } catch (IOException e) {
            log.error("JsonUtils#toBean(String,Class) catch a exception -> ",e);

            return null;
        }
    }

    /**
     * 将java byte 转换成java对象
     *
     * @param jsonByte  json byte
     * @param beanClass class
     * @return <T>
     */
    public static <T> T toBean(byte[] jsonByte, Class<T> beanClass) {

        if (jsonByte == null || jsonByte.length == 0 || beanClass == null) {
            return null;
        }

        try {
            return objectMapper.readValue(jsonByte, beanClass);
        } catch (IOException e) {
            log.error("JsonUtils#toBean(byte[],Class) catch a exception -> ",e);

            return null;
        }
    }

    public static <T> T convert(Object obj, TypeReference<T> typeReference) {
        if (ObjectUtils.isEmpty(obj)) {
            return null;
        }

        try {
            return objectMapper.convertValue(obj, typeReference);
        } catch (Exception ex) {
            log.error("JsonUtils#convert(Object,TypeReference) catch a exception -> ",ex);

            return null;
        }
    }

    public static <T> T toMap(String jsonString, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(jsonString) || typeReference == null) {
            return null;
        }

        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (IOException e) {
            log.error("JsonUtils#toMap(String,TypeReference) catch a exception -> ",e);

            return null;
        }
    }

    public static <K, V> Map<K, V> toMap(String jsonString,
                                         Class<K> keyClass, Class<V> valueClass) {
        if (StringUtils.isEmpty(jsonString) ||
                keyClass == null || valueClass == null) {
            return null;
        }

        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(HashMap.class, keyClass, valueClass);
            return objectMapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            log.error("JsonUtils#toBean(String,Class) catch a exception -> ",e);

            return null;
        }
    }

    public static <T> List<T> toList(String jsonString, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(jsonString) || typeReference == null) {
            return null;
        }

        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (IOException e) {
            log.error("JsonUtils#toBean(String,Class) catch a exception -> ",e);

            return null;
        }
    }

    public static <T> List<T> toList(String jsonString, Class<T> beanClass) {
        if (StringUtils.isEmpty(jsonString) || beanClass == null) {
            return null;
        }

        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanClass);
            return objectMapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            log.error("JsonUtils#toBean(String,Class) catch a exception -> ",e);

            return null;
        }
    }

    public static JsonNode getValue(String jsonString, String key) {
        if (StringUtils.isEmpty(jsonString) || StringUtils.isEmpty(key)) {
            return null;
        }

        try {
            JsonNode root = objectMapper.readTree(jsonString);
            return root.get(key);
        } catch (IOException e) {
            log.error("JsonUtils#getValue(String,String) catch a exception -> ",e);
            log.error("params -> params1={}, params2={}", jsonString, key);
            return null;
        }

    }
}
