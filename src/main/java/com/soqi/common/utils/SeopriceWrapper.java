package com.soqi.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.soqi.oem.gentry.Seoprice;

/**Seoprice 包装类
 * @author 孙傲
 *
 */
public class SeopriceWrapper {
	
	/**把Seoprice分组拆分，分组条件是userid,每个userid是一个list数组
	 * @param splist
	 * @return
	 */
	public static Map<String, List<Seoprice>> getListbyUserid(List<Seoprice> splist){
		Map<String, List<Seoprice>> map = new HashMap<String, List<Seoprice>>();
		for(Seoprice sp : splist){
			String userid = sp.getUserid().toString();
			if(map.containsKey(userid)){
				map.get(userid).add(sp);
			}else{
				List<Seoprice> l = new ArrayList<Seoprice>();
				l.add(sp);
				map.put(userid, l);
			}
		}
		return map;
	}
}
