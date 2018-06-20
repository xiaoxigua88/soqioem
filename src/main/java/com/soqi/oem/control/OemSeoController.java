package com.soqi.oem.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soqi.common.utils.CookieUtils;
import com.soqi.common.utils.FastJsonUtil;
import com.soqi.oem.gentry.Seo;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.SeoService;
import com.soqi.system.vo.Filter;
import com.soqi.system.vo.Page;

@Controller
public class OemSeoController extends BaseController{
	private final Logger logger = LoggerFactory.getLogger(OemSeoController.class);
	@Autowired
	private SeoService seoService;
	@RequestMapping("/oemmanager/business/seo/manage")
	public String oemSeoManage(Model model, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="20";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		Filter filter = new Filter("desc", "", "");
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		List<Seo> lst = seoService.qrySeoManageListByOemId(this.getCustomer().getOemid(), start, size);
		int total = seoService.qryCountSeoManageListByOemId(this.getCustomer().getOemid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		String searchTypeList = "[{\"TypeId\":0,\"Sort\":0,\"Name\":\"全部搜索\",\"MaxPage\":5,\"Enabled\":false},{\"TypeId\":1010,\"Sort\":1,\"Name\":\"百度PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":1015,\"Sort\":2,\"Name\":\"360PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":1030,\"Sort\":3,\"Name\":\"搜狗PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7010,\"Sort\":4,\"Name\":\"百度手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7015,\"Sort\":5,\"Name\":\"360手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7030,\"Sort\":6,\"Name\":\"搜狗手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7070,\"Sort\":7,\"Name\":\"神马\",\"MaxPage\":5,\"Enabled\":true}]";
		String settleStatusList = "[{\"Name\":\"All\",\"Value\":-999,\"Description\":\"全部\"},{\"Name\":\"Waiting\",\"Value\":1,\"Description\":\"未结算\"},{\"Name\":\"Low\",\"Value\":2,\"Description\":\"未达标\"},{\"Name\":\"Up\",\"Value\":3,\"Description\":\"已达标\"}]";
		String keywordLikeList = "[{\"Name\":\"Like\",\"Value\":1,\"Description\":\"包含\"},{\"Name\":\"Equal\",\"Value\":2,\"Description\":\"等于\"},{\"Name\":\"NotLike\",\"Value\":3,\"Description\":\"不包含\"},{\"Name\":\"RightLike\",\"Value\":4,\"Description\":\"以开头\"},{\"Name\":\"LeftLike\",\"Value\":5,\"Description\":\"以结尾\"}]";
		String seoStatusList = "[{\"Name\":\"All\",\"Value\":-999,\"Description\":\"全部\"},{\"Name\":\"Checking\",\"Value\":1,\"Description\":\"检测中\"},{\"Name\":\"CheckError\",\"Value\":2,\"Description\":\"不可用\"},{\"Name\":\"NeedPay\",\"Value\":3,\"Description\":\"待付款\"},{\"Name\":\"Doing\",\"Value\":4,\"Description\":\"优化中\"},{\"Name\":\"Stop\",\"Value\":5,\"Description\":\"已停止\"},{\"Name\":\"Delete\",\"Value\":-1,\"Description\":\"已删除\"}]";
		jsonObj.put("searchTypeList", FastJsonUtil.parseObject(searchTypeList, List.class));
		jsonObj.put("settleStatusList", FastJsonUtil.parseObject(settleStatusList, List.class));
		jsonObj.put("keywordLikeList", FastJsonUtil.parseObject(keywordLikeList, List.class));
		jsonObj.put("seoStatusList", FastJsonUtil.parseObject(seoStatusList, List.class));
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/business/seo/manage";
	}
}
