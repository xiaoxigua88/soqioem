package com.soqi.common.utils;

import java.util.HashMap;
import java.util.Map;

public class ResultFontJS extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	public ResultFontJS(String text) {
		put("text",text);
	}
	public ResultFontJS() {
		
	}
	public static ResultFontJS error() {
		return error("操作失败");
	}

	public static ResultFontJS error(String text) {
		ResultFontJS r = new ResultFontJS(text);
		r.put("result", false);
		r.put("reload", false);
		return r;
	}

	public static ResultFontJS ok(String text) {
		ResultFontJS r = new ResultFontJS(text);
		r.put("result", true);
		r.put("reload", true);
		return r;
	}

	public static ResultFontJS ok(Map<String, Object> map) {
		ResultFontJS r = new ResultFontJS("操作成功");
		r.put("result", true);
		r.put("reload", true);
		r.putAll(map);
		return r;
	}

	public static ResultFontJS ok() {
		return new ResultFontJS("操作成功");
	}

	@Override
	public ResultFontJS put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
