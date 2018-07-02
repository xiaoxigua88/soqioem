package com.soqi.apido;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.soqi.common.utils.BusinessUtil;
import com.soqi.common.utils.DateUtil;
import com.soqi.common.utils.EncodeUtils;
import com.soqi.common.utils.HttpClientUtil;
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
		JSONObject result = apiDo(seos, "AddSearchTask", data);
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
	public boolean keywordRankSearchAdd(List<Seo> seos){
		int apiExtend = 1; //这个值自己定义，用于自己的扩展标志，正整数，缺省值为1，接口回调时会原样返回
		int businessType = 1006;
		String[] keyword = BusinessUtil.couvertToKeyAndUrl(seos).get("keywords");
        String[] url = BusinessUtil.couvertToKeyAndUrl(seos).get("urls");
        int searchType = 1010;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("apiExtend", apiExtend);
		dataMap.put("businessType", businessType);
		dataMap.put("keyword", keyword);
		dataMap.put("url", url);
		dataMap.put("searchType", searchType);
		dataMap.put("time", DateUtil.getSecondTimestampTwo(new Date()));
		String data = JSONObject.toJSONString(dataMap);
		JSONObject result = apiDo(seos, "AddSearchTask", data);
		if(result.getInteger("xCode") == 0){
			BusinessUtil.getSeoListFormKeywords(seos, result.getJSONArray("xValue"), businessType);
			return true;
		}else{
			logger.error(result.getString("xMessage"));
			return false;
		}
	}
	
	/**
	 * @param seos
	 * @return
	 */
	public String keywordRankWatchAdd(List<Seo> seos){
		int apiExtend = 1; //这个值自己定义，用于自己的扩展标志，正整数，缺省值为1，接口回调时会原样返回
		int businessType = 2006;
		String[] keyword = { "58同城", "赶集网" };
		String[] url = { "58.com", "ganji.com" };
		int[] timeSet = { 10, 16 };
		boolean searchOnce = true;
		int searchType = 1010;
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
		return /*apiDo("AddSearchTask", data)*/null;
	}
	
	public JSONObject apiDo(List<Seo> seos, String action, String data){
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
}
