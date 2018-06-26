package com.soqi.oem.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soqi.common.utils.BigDecimalUtil;
import com.soqi.common.utils.CookieUtils;
import com.soqi.common.utils.FastJsonUtil;
import com.soqi.common.utils.ResultFontJS;
import com.soqi.common.utils.SeoWrapper;
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
	
	/**功能:不同价导入
	 * @param url
	 * @param keyword
	 * @param searchType
	 * @param verifycode
	 * @return
	 */
	@RequestMapping("/oemmanager/business/seo/importdiff")
	@ResponseBody
	public ResultFontJS importDiff(@RequestParam(value="username",required=true) String username, String keyword, String url, HttpServletRequest req){
		username = username.trim();
		keyword = keyword.trim();
		url = url.trim();
		if(StringUtils.isBlank(username)){
			return ResultFontJS.error("用户ID、手机号、邮箱不能为空");
		}
		if(StringUtils.isBlank(keyword)){
			return ResultFontJS.error("关键词不能为空");
		}
		if(StringUtils.isBlank(url)){
			return ResultFontJS.error("url不能为空");
		}
		String[] urls = url.trim().split("\r\n");
		int len = keyword.trim().split("\r\n").length;
		if(len != urls.length && urls.length != 1){
			return ResultFontJS.error("网址的数量不匹配：一条或与关键词数量相等，请确认！");
		}
		String errText = null;
		String name = null;
		String[] searchType = {"1010","1015","1030","7010","7015","7030","7070"};
		for (String st : searchType) {
			String torank_ = req.getParameter("torank_"+st);
			String price_ = req.getParameter("price_"+st);
			if(StringUtils.isNotBlank(torank_)){
				if(StringUtils.isBlank(price_) || price_.split("\r\n").length != len){
					errText = "关键词价格设置错误";
					name = "price_" + st;
					break;
				}
			}
		}
		if(errText != null && name != null){
			ResultFontJS rs = ResultFontJS.error(errText);
			rs.put("name",name);
			return rs;
		}
		//封装seo对像
		List<Seo> seos = SeoWrapper.bathConvertDiffSeo(req, 1001, url, keyword, searchType);
		int count = seoService.batchSameSeoInsert(seos);
		if(count >0){
			return ResultFontJS.ok("提交完成，稍候请在列表中查看检测结果！<br/>本次请求不重复记录<b class='text-red'>"+seos.size()+"</b>个。失败<b class='text-red'>"+(seos.size()-count)+"</b>个，忽略<b class='text-red'>"+(seos.size()-count)+"</b>个，成功<b class='text-red'>"+ count +"</b>个！");
		}
		return ResultFontJS.error("不同价关键词导入失败");
	}
	
	/**功能:相同价导入
	 * @param url
	 * @param keyword
	 * @param searchType
	 * @param verifycode
	 * @return
	 */
	@RequestMapping("/oemmanager/business/seo/importsame")
	@ResponseBody
	public ResultFontJS importSame(@RequestParam(value="username",required=true) String username, String keyword, String url, HttpServletRequest req){
		username = username.trim();
		keyword = keyword.trim();
		url = url.trim();
		if(StringUtils.isBlank(username)){
			return ResultFontJS.error("用户ID、手机号、邮箱不能为空");
		}
		if(StringUtils.isBlank(keyword)){
			return ResultFontJS.error("关键词不能为空");
		}
		if(StringUtils.isBlank(url)){
			return ResultFontJS.error("url不能为空");
		}
		if(keyword.split("\r\n").length != url.split("\r\n").length){
			return ResultFontJS.error("网址的数量不匹配：一条或与关键词数量相等，请确认！");
		}
		String errText = null;
		String name = null;
		String[] searchType = {"1010","1015","1030","7010","7015","7030","7070"};
		for (String st : searchType) {
			String torank1_ = req.getParameter("torank1_"+st);
			String price1_ = req.getParameter("price1_"+st);
			String torank2_ = req.getParameter("torank2_"+st);
			String price2_ = req.getParameter("price2_"+st);
			if(torank1_ == null && price1_ == null && torank2_ == null && price2_ == null){
				continue;
			}
			if(StringUtils.isBlank(torank1_) || !StringUtils.isNumeric(torank1_) || !(Integer.valueOf(torank1_) > 0 ) ){
				errText = "达标条件参数设置错误、必须为正整数";
				name = "torank1_" + st;
				break;
			}
			
			if(StringUtils.isBlank(price1_) || !BigDecimalUtil.isBigDecimal(price1_) || !BigDecimalUtil.compareSize(Double.valueOf(price1_), 0.00)){
				errText = "价格参数设置错误、必须为大于0的数值";
				name = "price1_" + st;
				break;
			}
			
			if(StringUtils.isNotBlank(torank2_)){
				if(!StringUtils.isNumeric(torank2_) || !(Integer.valueOf(torank2_) > 0 )){
					errText = "达标条件参数设置错误、必须为正整数";
					name = "torank2_" + st;
					break;
				}
				
				if(StringUtils.isBlank(price2_) || !BigDecimalUtil.isBigDecimal(price2_) || !BigDecimalUtil.compareSize(Double.valueOf(price2_), 0.00)){
					errText = "价格参数设置错误、必须为大于0的数值";
					name = "price2_" + st;
					break;
				}
				
				if(Integer.valueOf(torank1_) > Integer.valueOf(torank2_)){
					if(BigDecimalUtil.compareSize(Double.valueOf(price1_), Double.valueOf(price2_))){
						errText = "价格参数设置错误、前" + torank1_ +"名的价格必须要低于前" + torank2_ + "名的价格";
						name = "price1_" + st;
						break;
					}
				}
				
				if(Integer.valueOf(torank1_) < Integer.valueOf(torank2_)){
					if(!BigDecimalUtil.compareSize(Double.valueOf(price1_), Double.valueOf(price2_))){
						errText = "价格参数设置错误、前" + torank1_ +"名的价格必须要高于前" + torank2_ + "名的价格";
						name = "price1_" + st;
						break;
					}
				}
			}
			
		}
		if(errText != null && name != null){
			ResultFontJS rs = ResultFontJS.error(errText);
			rs.put("name",name);
			return rs;
		}
		//封装seo对像
		List<Seo> seos = SeoWrapper.bathConvertSameSeo(req, 1001, url, keyword, searchType);
		int count = seoService.batchSameSeoInsert(seos);
		if(count >0){
			return ResultFontJS.ok("提交完成，稍候请在列表中查看检测结果！<br/>本次请求不重复记录<b class='text-red'>"+seos.size()+"</b>个。失败<b class='text-red'>"+(seos.size()-count)+"</b>个，忽略<b class='text-red'>"+(seos.size()-count)+"</b>个，成功<b class='text-red'>"+ count +"</b>个！");
		}
		return ResultFontJS.error("同价关键词导入失败");
	}
}
