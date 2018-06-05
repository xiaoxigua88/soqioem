package com.soqi.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriUtil {
	
	private static List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
	
	static{
		maps.add(setPril("Menu",1,"菜单"));
		maps.add(setPril("List",2,"列表"));
		maps.add(setPril("Model",4,"详情"));
		maps.add(setPril("Add",8,"增加"));
		maps.add(setPril("Update",16,"修改"));
		maps.add(setPril("Delete",32,"删除"));
	}
	
	public PriUtil() {
		// TODO Auto-generated constructor stub
	}
	
	private static Map<String, Object> setPril(String name, int value, String description){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("name", name);
		map.put("value", value);
		map.put("description", description);
		return map;
	}
	
	public static List<Map<String, Object>> getPriEnumList(){
		return maps;
	}
	
	public static void main(String args[]){
		System.out.println(FastJsonUtil.toJSONString(maps));
	}
}
