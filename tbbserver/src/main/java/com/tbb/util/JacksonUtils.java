package com.tbb.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Description:Jackson工具类.
 *
 * @author Eason
 * @since 14-5-1 上午9:11
 */
public class JacksonUtils {
    private final static Log logger = LogFactory.getLog(JacksonUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();//饿汉方式

    /**
     * 获得默认的ObjectMapper.
     *
     * @return 返回默认的ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {
        //一次创建,重复使用
        //重置命名机制
        return mapper.setPropertyNamingStrategy(null); // create once, reuse
    }

    /**
     * 创建含有命名机制的ObjectMapper.
     *
     * @param strategy 命名机制
     * @return ObjectMapper
     */
    public static ObjectMapper getObjectMapper(PropertyNamingStrategy strategy) {
        //一次创建,重复使用
        return mapper.setPropertyNamingStrategy(strategy);
    }

    /**
     * 符合json格式数组的字符串转换成list,每个元素是封装好的domain对象
     *
     * @param jsonStr   符合json格式数组的字符串
     * @param reference 转换类型
     * @param <T>       泛型类型
     * @return 含有泛型的list
     * @throws java.io.IOException
     */
    public static <T> List<T> toObject(String jsonStr, TypeReference<List<T>> reference) throws IOException {
        ObjectMapper mapper = getObjectMapper();
        return mapper.readValue(jsonStr, reference);
    }

    /**
     * 符合json格式数组的字符串转换成list,每个元素是封装好的domain对象
     *
     * @param mapper    ObjectMapper
     * @param jsonStr   符合json格式数组的字符串
     * @param reference 转换类型
     * @param <T>       泛型类型
     * @return 含有泛型的list
     * @throws java.io.IOException
     */
    public static <T> List<T> toObject(ObjectMapper mapper, String jsonStr, TypeReference<List<T>> reference) throws IOException {
        return mapper.readValue(jsonStr, reference);
    }

    public static <K, V> Map<K, V> toMap(ObjectMapper mapper, String jsonStr, TypeReference<Map<K, V>> reference) throws IOException {
        return mapper.readValue(jsonStr, reference);
    }

    public static <T> T toObj(String jsonStr, TypeReference<?> reference) throws IOException {
        ObjectMapper mapper = getObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(jsonStr, reference);
    }

    /**
     * 符合json格式字符串转换成bean
     *
     * @param jsonStr   符合json格式字符串
     * @param valueType 转换类型
     * @param <T>       泛型类型
     * @return 类型为T的bean
     * @throws java.io.IOException
     */
    public static <T> T toObject(String jsonStr, Class<T> valueType) throws IOException {
        ObjectMapper mapper = getObjectMapper();
        return mapper.readValue(jsonStr, valueType);
    }

    /**
     * 符合json格式字符串转换成bean
     *
     * @param jsonStr   符合json格式字符串
     * @param valueType 转换类型
     * @param <T>       泛型类型
     * @return 类型为T的bean
     * @throws java.io.IOException
     */
    public static <T> T toObj(String jsonStr, Class<T> valueType) {
        ObjectMapper mapper = getObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(jsonStr, valueType);
        } catch (JsonMappingException e) {
            logger.error(e);
        } catch (JsonParseException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }

    /**
     * 符合json格式字符串转换成bean
     *
     * @param mapper    ObjectMapper
     * @param jsonStr   符合json格式字符串
     * @param valueType 转换类型
     * @param <T>       泛型类型
     * @return 类型为T的bean
     * @throws java.io.IOException
     */
    public static <T> T toObject(ObjectMapper mapper, String jsonStr, Class<T> valueType) throws IOException {
        return mapper.readValue(jsonStr, valueType);
    }

    /**
     * 返回指定节点名称的内容.
     *
     * @param jsonStr  符合json格式的字符串
     * @param nodeName 节点名称
     * @return 节点的内容
     * @throws java.io.IOException
     */
    public static JsonNode getNode(String jsonStr, String nodeName) throws IOException {
        ObjectMapper mapper = getObjectMapper();
        JsonNode jsonTree = mapper.readTree(jsonStr);
        return jsonTree.get(nodeName);
    }

    /**
     * 返回指定节点名称的内容.
     *
     * @param mapper   指定PropertyNamingStrategy的ObjectMapper
     * @param jsonStr  符合json格式的字符串
     * @param nodeName 节点名称
     * @return 节点的内容
     * @throws java.io.IOException
     */
    public static JsonNode getNode(ObjectMapper mapper, String jsonStr, String nodeName) throws IOException {
        JsonNode jsonTree = mapper.readTree(jsonStr);
        return jsonTree.get(nodeName);
    }

    /**
     * 返回指定节点名称的实体对象.
     *
     * @param jsonStr   符合json格式的字符串
     * @param valueType 转换后的类型
     * @param nodeName  节点名称
     * @param <T>       转换后的实体
     * @return 指定节点名称的实体对象
     * @throws java.io.IOException
     */
    public static <T> T toObject(String jsonStr, Class<T> valueType, String nodeName) throws IOException {
        JsonNode eventsNode = getNode(jsonStr, nodeName);
        return toObject(eventsNode.toString(), valueType);
    }


    /**
     * 返回指定节点名称的实体对象.
     *
     * @param mapper    指定PropertyNamingStrategy的ObjectMapper
     * @param jsonStr   符合json格式的字符串
     * @param valueType 转换后的类型
     * @param nodeName  节点名称
     * @param <T>       转换后的实体
     * @return 指定节点名称的实体对象
     * @throws java.io.IOException
     */
    public static <T> T toObject(ObjectMapper mapper, String jsonStr, Class<T> valueType, String nodeName) throws IOException {
        JsonNode eventsNode = getNode(mapper, jsonStr, nodeName);
        return toObject(mapper, eventsNode.toString(), valueType);
    }

    /**
     * 返回指定节点名称的实体对象.
     *
     * @param jsonStr   符合json格式的字符串
     * @param reference 转换类型
     * @param nodeName  节点名称
     * @param <T>       转换后的实体
     * @return 指定节点名称的实体对象
     * @throws java.io.IOException
     */
    public static <T> List<T> toObject(String jsonStr, TypeReference<List<T>> reference, String nodeName) throws IOException {
        JsonNode eventsNode = getNode(jsonStr, nodeName);
        return toObject(mapper, eventsNode.toString(), reference);
    }

    /**
     * 返回指定节点名称的实体对象.
     *
     * @param mapper    指定PropertyNamingStrategy的ObjectMapper
     * @param jsonStr   符合json格式的字符串
     * @param reference 转换类型
     * @param nodeName  节点名称
     * @param <T>       转换后的实体
     * @return 指定节点名称的实体对象
     * @throws java.io.IOException
     */
    public static <T> List<T> toObject(ObjectMapper mapper, String jsonStr, TypeReference<List<T>> reference, String nodeName) throws IOException {
        JsonNode eventsNode = getNode(mapper, jsonStr, nodeName);
        return toObject(mapper, eventsNode.toString(), reference);
    }

    /**
     * 转换成json格式的字符串
     *
     * @param obj 实体对象
     * @return json格式的字符串
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static String toString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = getObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);  //属性值为null的不序列化
        return mapper.writeValueAsString(obj);
    }

    /**
     * 转换成json格式的字符串
     *
     * @param obj 实体对象
     * @return json格式的字符串
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static String toStr(Object obj) {
        ObjectMapper mapper = getObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);  //属性值为null的不序列化
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error(e);
        }
        return null;
    }

    /**
     * 转换成json格式的字符串
     *
     * @param mapper ObjectMapper
     * @param obj    实体对象
     * @return json格式的字符串
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static String toString(ObjectMapper mapper, Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }
}