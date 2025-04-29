package com.weaver.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * jackson工具类
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        //序列化的时候序列对象的所有属性
        MAPPER.setSerializationInclusion(Include.ALWAYS);
        //反序列化的时候如果多了其他属性,不抛出异常
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //如果是空对象的时候,不抛异常
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //增加jdk8时间支持
        MAPPER.findAndRegisterModules();
    }


    /**
     * 将对象转换成json字符串
     *
     * @param data 需要转换的对象
     * @return json
     */
    @Nullable
    public static String objectToJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    /**
     * 将json转换成pojo对象
     *
     * @param jsonData json字符串
     * @param beanType 需要转换的对象类型
     * @return 转换的对象
     */
    @Nullable
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    /**
     * json转换map
     *
     * @param jsonStr   json字符串
     * @param valueType Map的VALUE类型
     * @return 转换后的Map
     */
    @Nullable
    public static <T> Map<String, T> jsonToGenericMap(String jsonStr, Class<T> valueType) {
        try {
            return MAPPER.readValue(jsonStr,
                    MAPPER.getTypeFactory().constructMapType(LinkedHashMap.class, String.class, valueType));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    /**
     * json转换list
     *
     * @param jsonData json字符串
     * @param beanType List的泛型类型
     * @return 转换后的List
     */
    @Nullable
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    /**
     * 判断输入字符串是否为json数组
     *
     * @param dataInfo 输入字符串
     * @return 是否为json数组
     */
    public static boolean isListJson(String dataInfo) {
        if (StringUtils.isBlank(dataInfo) || !dataInfo.startsWith("[") || !dataInfo.startsWith("]")){
            return false;
        }
        try {
            MAPPER.readValue(dataInfo, List.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}