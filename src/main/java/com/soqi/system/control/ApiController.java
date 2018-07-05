package com.soqi.system.control;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.soqi.apido.DataPlatformCallbackService;
import com.soqi.apido.gentry.KeyPrice;
import com.soqi.common.utils.EncodeUtils;
import com.soqi.oem.gentry.Seo;



@Controller
public class ApiController {
	
	private static Logger logger = LogManager.getLogger(ApiController.class.getName());
	
	@Autowired
	private DataPlatformCallbackService callback;
	
	@RequestMapping("/oemapi")
	@ResponseBody
	public String demo(@RequestParam(value="xParam", required=true) String xParam, HttpServletRequest req) {
		xParam = EncodeUtils.urlDecode(xParam).toLowerCase();
		logger.debug("获取到的xParam参数值为：" + xParam);
		JSONObject jxpar = JSONObject.parseObject(xParam);
		//参数校验部分
		int businessType = jxpar.getIntValue("businesstype");
		String beanStr = jxpar.getString("value");
		//业务处理部分
		if(businessType == 2006){
			//关键词排名监控通知
			beanStr = beanStr.replace("updatetime", "rankupdatetime").replace("taskid", "apiwatchtaskid");
			logger.debug("云监控字符串内容：" + beanStr);
			Seo seo = JSONObject.parseObject(beanStr).toJavaObject(Seo.class);
			//关键词排名监控通知
			callback.seoRankUpdateByPlatformId(seo);
		}else if(businessType == 1008){
			//关键词价格实时查通知
			KeyPrice kp = JSONObject.parseObject(beanStr).toJavaObject(KeyPrice.class);
			callback.seoPriceUpdatePlatFormId(kp);
			
		}else if(businessType == 1006){
			beanStr = beanStr.replace("updatetime", "rankupdatetime").replace("taskid", "apiranktaskid");
			logger.debug("云排名实时查字符串内容：" + beanStr);
			Seo seo = JSONObject.parseObject(beanStr).toJavaObject(Seo.class);
			//关键词排名实时查通知
			callback.seoRankUpdateByPlatformId(seo);
		}
        return "1";
    }
}
