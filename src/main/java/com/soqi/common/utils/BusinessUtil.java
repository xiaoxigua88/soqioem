package com.soqi.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.soqi.oem.gentry.Role;
import com.soqi.oem.gentry.Seo;

public class BusinessUtil {

	public static List<Role> checkedForRoles(List<Role> source, List<Role> target){
		for (Role role : target) {
			for(Role sr :source){
				if(sr.getRoleid().intValue() == role.getRoleid().intValue()){
					role.setExist(1);
				}else{
					role.setExist(0);
				}
			}
		}
		return target;
	}
	
	/**从seo对象中转化接口需要的关键词和url数组
	 * @param seos
	 * @return
	 */
	public static Map<String,String[]> couvertToKeyAndUrl(List<Seo> seos){
		Map<String,String[]> map = new HashMap<String,String[]>();
		String[] keyWords = new String[seos.size()];
		String[] urls = new String[seos.size()];
		for (int i = 0; i<seos.size(); i++) {
			keyWords[i] = seos.get(i).getKeyword();
			urls[i] = seos.get(i).getUrl();
		}
		map.put("keywords", keyWords);
		map.put("urls", urls);
		return map;
	}
	
	/**Api接口任务id转化
	 * @param seos
	 * @param cloudTaskids
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static void getSeoListFormKeywords(List<Seo> seos,JSONArray cloudTaskids, int businessType){
		List<List> cloudTaskList = cloudTaskids.toJavaList(List.class);
		for (int i = 0; i<seos.size(); i++) {
			//String apiTaskid = (String)cloudTaskList.get(i).get(0);
			Integer apiTaskid = (Integer)cloudTaskList.get(i).get(0);
			if(businessType == 1008){
				//关键词价格
				seos.get(i).setApipricetaskid(apiTaskid.longValue());
			}else if(businessType == 1006){
				//关键词排名
				seos.get(i).setApiranktaskid(apiTaskid.longValue());
			}else if(businessType == 2006){
				seos.get(i).setApiwatchtaskid(apiTaskid.longValue());
			}
		}
	}
}
