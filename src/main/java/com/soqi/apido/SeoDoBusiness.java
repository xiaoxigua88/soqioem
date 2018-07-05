package com.soqi.apido;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.soqi.common.utils.BusinessUtil;
import com.soqi.common.utils.DateUtil;
import com.soqi.common.utils.EncodeUtils;
import com.soqi.common.utils.HttpClientUtil;
import com.soqi.common.utils.SeoWrapper;
import com.soqi.oem.dao.SeoMapper;
import com.soqi.oem.gentry.Seo;
import com.soqi.system.config.SeoApiProperty;

/**
 * @author 孙傲
 *
 */

@Service
public class SeoDoBusiness {
	
	private final Logger logger = LoggerFactory.getLogger(SeoDoBusiness.class);
	@Autowired
	private SeoApiProperty sap;
	@Autowired
	private SeoMapper sm;
	//关键词价格实时查任务添加
	public boolean KeywordPriceSearchAdd(List<Seo> seos){
		int apiExtend = 1; //这个值自己定义，用于自己的扩展标志，正整数，缺省值为1，接口回调时会原样返回
		int businessType = 1008;
		String[] keyword = BusinessUtil.couvertToKeyAndUrl(seos).get("keywords");
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("apiExtend", apiExtend);
		dataMap.put("businessType", businessType);
		dataMap.put("keyword", keyword);
		dataMap.put("time", DateUtil.getSecondTimestampTwo(new Date()));
		String data = JSONObject.toJSONString(dataMap);
		JSONObject result = apiDo("AddSearchTask", data);
		if(result.getInteger("xCode") == 0){
			BusinessUtil.getSeoListFormKeywords(seos, result.getJSONArray("xValue"), businessType);
			return true;
		}else{
			logger.error(result.getString("xMessage"));
			return false;
		}
	}
	
	/**关键词排名任务实时查任务添加
	 * @return
	 */
	public boolean keywordRankSearchAdd(List<Seo> seos, int searchType){
		int apiExtend = 1; //这个值自己定义，用于自己的扩展标志，正整数，缺省值为1，接口回调时会原样返回
		int businessType = 1006;
		String[] keyword = BusinessUtil.couvertToKeyAndUrl(seos).get("keywords");
        String[] url = BusinessUtil.couvertToKeyAndUrl(seos).get("urls");
        //int searchType = 1010;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("apiExtend", apiExtend);
		dataMap.put("businessType", businessType);
		dataMap.put("keyword", keyword);
		dataMap.put("url", url);
		dataMap.put("searchType", searchType);
		dataMap.put("time", DateUtil.getSecondTimestampTwo(new Date()));
		String data = JSONObject.toJSONString(dataMap);
		JSONObject result = apiDo("AddSearchTask", data);
		if(result.getInteger("xCode") == 0){
			BusinessUtil.getSeoListFormKeywords(seos, result.getJSONArray("xValue"), businessType);
			return true;
		}else{
			logger.error(result.getString("xMessage"));
			return false;
		}
	}
	
	/**云排名监控添加
	 * @param seos
	 * @return
	 */
	public boolean keywordRankWatchAdd(List<Seo> seos, int searchType){
		int apiExtend = 1; //这个值自己定义，用于自己的扩展标志，正整数，缺省值为1，接口回调时会原样返回
		int businessType = 2006;
		String[] keyword = BusinessUtil.couvertToKeyAndUrl(seos).get("keywords");
		String[] url = BusinessUtil.couvertToKeyAndUrl(seos).get("urls");
		int[] timeSet = { 10, 16 };
		boolean searchOnce = true;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("apiExtend", apiExtend);
		dataMap.put("businessType", businessType);
		dataMap.put("keyword", keyword);
		dataMap.put("url", url);
		dataMap.put("timeSet", timeSet);
		dataMap.put("searchOnce", searchOnce);
		dataMap.put("searchType", searchType);
		dataMap.put("time", DateUtil.getSecondTimestampTwo(new Date()));
		String data = JSONObject.toJSONString(dataMap);
		JSONObject result = apiDo("AddSearchTask", data);
		if(result.getInteger("xCode") == 0){
			BusinessUtil.getSeoListFormKeywords(seos, result.getJSONArray("xValue"), businessType);
			return true;
		}else{
			logger.info(result.getString("xMessage"));
			return false;
		}
	}
	
	/**删除数据平台任务
	 * @param taskIds
	 * @return
	 */
	@Async("myTaskAsyncPool")
	public List<Integer> keywordRankDel(Integer[] taskIds){
		List<Seo> seos = sm.selectByTaskids(taskIds);
		Integer[] platformTaskids = BusinessUtil.convertToPlateTaskids(seos).get("platformTaskids");
		int businessType = 2006;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("businessType", businessType);
		dataMap.put("time", DateUtil.getSecondTimestampTwo(new Date()));
		dataMap.put("taskId", platformTaskids);
		String data = JSONObject.toJSONString(dataMap);
		JSONObject result = apiDo("DelSearchTask", data);
		if(result.getInteger("xCode") == 0){
			List<Integer> fail = BusinessUtil.parseTaskIdResult(result.getJSONArray("xValue"));
			if(!fail.isEmpty()){
				logger.info("删除数据平台任务失败，失败任务ID为" + fail.toString() + "请检查任务ID的一致性");
				return fail;
			}
			return new ArrayList<Integer>();
		}else{
			return new ArrayList<Integer>();
		}
	}
	
	
	/** 功能：查询关键词排名云监控的所有任务
	 * @return
	 */
	public JSONArray qrySeoRankWatchTask(){
		int apiExtend = 1; //这个值自己定义，用于自己的扩展标志，正整数，缺省值为1，接口回调时会原样返回
		int businessType = 2006;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("apiExtend", apiExtend);
		dataMap.put("businessType", businessType);
		dataMap.put("time", DateUtil.getSecondTimestampTwo(new Date()));
		String data = JSONObject.toJSONString(dataMap);
		JSONObject result = apiDo("GetAllTask", data);
		if(result.getInteger("xCode") == 0){
			return result.getJSONArray("xValue");
		}else{
			logger.info(result.getString("xMessage"));
			return null;
		}
	}
	
	public JSONObject apiDo(String action, String data){
		JSONObject jsonObj = JSONObject.parseObject(data);
		jsonObj.put("userId", sap.getUserId());
		data = jsonObj.toString();
		
		String tocken = sap.getToken();
        String url = sap.getApiUrl();
        String sign = DigestUtils.md5Hex(action + tocken + data).toUpperCase();
        
		String param = EncodeUtils.urlEncode(data);
		String urlData = "wAction=" + action + "&wParam=" + param + "&wSign=" + sign;
		logger.info("原始param=" + data + "\r\n");
		logger.info("wAction=" + action + "\r\n");
		logger.info("POST请求时，要将参数值进行UTF-8编码\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
		logger.info("wParam=" + param + "\r\n");
		logger.info("wSign=" + sign + "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
		logger.info("wSign的值是wAction的值连接APITOCKEN连接data原始值进行MD5加密，在转化成大写：MD5(" + action + tocken + data + ")\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
		try {
			logger.info("POST请求的网关：" + tocken + "\r\n");
			logger.info("POST请求的参数：" + urlData + "\r\n");
			logger.info("POST请求的编码：utf-8\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
			String jsonResult = HttpClientUtil.postUrlData(url, urlData, "UTF-8", "UTF-8");
			logger.info("POST请求的结果（JSON格式）：" + jsonResult + "\r\n");
			JSONObject result = JSONObject.parseObject(jsonResult);
			return result;
		} catch (IOException e) {
			logger.error("与服务器通信出现故障:"+e.toString());
			return null;
		}
	}
	
	/**异步关键词排名实时查、价格实时查服务ID生成调用
	 * @param listMap
	 * @param isClient 客户端自助添加关键启涉及关键词查询、排名、购买时的排名监控的服务ID生成、代理端只有用于购买的服务ID生成
	 */
	@Async("myTaskAsyncPool")
	@Transactional("primaryTransactionManager")
    public void createServiceIdOfRP(Map<String, List<Seo>> listMap, boolean isClient){
		List<Seo> seos = new ArrayList<Seo>();
		for (Map.Entry<String, List<Seo>> entry : listMap.entrySet()) {
			if(isClient){
				//云排名任务ID添加
				this.keywordRankSearchAdd(entry.getValue(), Integer.valueOf(entry.getKey()));
				//云排名价格添加
				this.KeywordPriceSearchAdd(entry.getValue());
			}
			//云排名监控任务添加
			//this.keywordRankWatchAdd(entry.getValue(), Integer.valueOf(entry.getKey()));
			seos.addAll(entry.getValue());
		}
		sm.updateServiceIdByListSeo(seos);
    }
	
	/**异步关键词排名监控服务ID生成调用
	 * @param listMap
	 * @param isClient 客户端自助添加关键启涉及关键词购买时的排名监控的服务ID生成、代理端由于手工设置价格、排名所以无须调用此方法
	 */
	@Async("myTaskAsyncPool")
	@Transactional("primaryTransactionManager")
    public void createServiceIdOfW(Integer[] taskIds){
		List<Seo> seos = sm.selectByTaskids(taskIds);
		Map<String, List<Seo>> listMap = SeoWrapper.convertListToMapBySearchType(seos);
		for (Map.Entry<String, List<Seo>> entry : listMap.entrySet()) {
			//云排名监控任务添加
			this.keywordRankWatchAdd(entry.getValue(), Integer.valueOf(entry.getKey()));
			seos.addAll(entry.getValue());
		}
		sm.updateServiceIdByListSeo(seos);
    }
}
