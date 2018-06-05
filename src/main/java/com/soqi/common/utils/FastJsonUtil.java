package com.soqi.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.DateCodec;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 功能说明: fastjson 服务类<br>
 * 系统版本: v1.0<br>
 * 开发人员: @author tanjk@hundsun.com<br>
 * 开发时间: 2012-4-23<br>
 */
public class FastJsonUtil {

	private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段
			SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
			SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
			SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
			SerializerFeature.WriteNullStringAsEmpty, // 字符类型字段如果为null，输出为""，而不是null
			SerializerFeature.WriteDateUseDateFormat,
			SerializerFeature.DisableCircularReferenceDetect};

	private static final SerializeConfig config;
	static {
		config = new SerializeConfig();
		config.put(java.util.Date.class, new DateCodec()); // 使用和json-lib兼容的日期输出格式
		config.put(java.sql.Date.class, new DateCodec()); // 使用和json-lib兼容的日期输出格式 }
	};

	public static String toJSONString(Object o) {
		return JSON.toJSONString(o, config, features);
	}

	public static <T> T parseObject(String jsonStr, Class<T> clazz) {
		return JSON.parseObject(jsonStr, clazz);
	}

	/**
	 * Json字符串转换为List<T>
	 * @param jsonStr
	 * @param clazz
	 * @param <T>
     * @return
     */
	public static <T> List<T> parseList(String jsonStr, Class<T> clazz) {
		return JSON.parseArray(jsonStr, clazz);
	}

	public static Object parseObject(JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		parseObject(map, jsonObject);
		return map;
	}

	public static List<Map<String, Object>> parseObject(JSONArray jsonArray) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (jsonArray != null && !jsonArray.isEmpty()) {
			for (Object object : jsonArray) {
				if (object instanceof JSONObject) {
					Map<String, Object> m = new HashMap<String, Object>();
					parseObject(m, (JSONObject)object);
					list.add(m);
				}
			}
		}
		return list;
	}

	private static void parseObject(Map<String, Object> map, JSONObject jsonObject) {
		if (jsonObject != null) {
			Set<Entry<String, Object>> keySet = jsonObject.entrySet();
			Iterator<Entry<String, Object>> it = keySet.iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof String) {
					map.put(key, value);
				} else if (value instanceof JSONObject) {
					Map<String, Object> m = new HashMap<String, Object>();
					parseObject(m, (JSONObject)value);
					map.put(key, m);
				} else if (value instanceof JSONArray) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					JSONArray jsonArray = (JSONArray)value;
					for (Object object : jsonArray) {
						if (object instanceof JSONObject) {
							Map<String, Object> m = new HashMap<String, Object>();
							parseObject(m, (JSONObject)object);
							list.add(m);
						}
					}
					map.put(key, list);
				}
			}
		}
	}
}
