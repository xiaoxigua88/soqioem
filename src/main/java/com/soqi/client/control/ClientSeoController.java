package com.soqi.client.control;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.ResponseBody;

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
public class ClientSeoController extends BaseController{
	private final Logger logger = LoggerFactory.getLogger(ClientSeoController.class);
	@Autowired
	private SeoService seoService;
	
	@RequestMapping("/client/business/seo/manage")
	public String userSeoManage(Model model, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="20";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		Filter filter = new Filter("desc", "", "");
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		List<Seo> lst = seoService.qrySeoManageListByUserId(this.getOemuser().getUserid(), start, size);
		int total = seoService.qryCountSeoManageListByUserId(this.getOemuser().getUserid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		String searchTypeList = "[{\"TypeId\":0,\"Sort\":0,\"Name\":\"全部搜索\",\"MaxPage\":5,\"Enabled\":false},{\"TypeId\":1010,\"Sort\":1,\"Name\":\"百度PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":1015,\"Sort\":2,\"Name\":\"360PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":1030,\"Sort\":3,\"Name\":\"搜狗PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7010,\"Sort\":4,\"Name\":\"百度手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7015,\"Sort\":5,\"Name\":\"360手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7030,\"Sort\":6,\"Name\":\"搜狗手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7070,\"Sort\":7,\"Name\":\"神马\",\"MaxPage\":5,\"Enabled\":true}]";
		String settleStatusList = "[{\"Name\":\"All\",\"Value\":-999,\"Description\":\"全部\"},{\"Name\":\"Waiting\",\"Value\":1,\"Description\":\"未结算\"},{\"Name\":\"Low\",\"Value\":2,\"Description\":\"未达标\"},{\"Name\":\"Up\",\"Value\":3,\"Description\":\"已达标\"}]";
		String keywordLikeList = "[{\"Name\":\"Like\",\"Value\":1,\"Description\":\"包含\"},{\"Name\":\"Equal\",\"Value\":2,\"Description\":\"等于\"},{\"Name\":\"NotLike\",\"Value\":3,\"Description\":\"不包含\"},{\"Name\":\"RightLike\",\"Value\":4,\"Description\":\"以开头\"},{\"Name\":\"LeftLike\",\"Value\":5,\"Description\":\"以结尾\"}]";
		String seoStatusList = "[{\"Name\":\"All\",\"Value\":-999,\"Description\":\"全部\"},{\"Name\":\"Doing\",\"Value\":4,\"Description\":\"优化中\"},{\"Name\":\"Stop\",\"Value\":5,\"Description\":\"已停止\"}]";
		jsonObj.put("searchTypeList", FastJsonUtil.parseObject(searchTypeList, List.class));
		jsonObj.put("settleStatusList", FastJsonUtil.parseObject(settleStatusList, List.class));
		jsonObj.put("keywordLikeList", FastJsonUtil.parseObject(keywordLikeList, List.class));
		jsonObj.put("seoStatusList", FastJsonUtil.parseObject(seoStatusList, List.class));
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		model.addAttribute("jsonData",jsonObj);
		return "/business/seo/manage";
    }
	
	/**云排名购买列表
	 * @param model
	 * @param pageNo
	 * @param resp
	 * @return
	 */
	@RequestMapping("/client/business/seo/apply")
	public String apply(Model model, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="20";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		Filter filter = new Filter("desc", "", "");
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		List<Seo> lst = seoService.qrySeoApplyListByUserId(this.getOemuser().getUserid(), start, size);
		int total = seoService.qryCountSeoApplyListByUserId(this.getOemuser().getUserid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		String searchTypeList = "[{\"TypeId\":1010,\"Sort\":1,\"Name\":\"百度PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":1015,\"Sort\":2,\"Name\":\"360PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":1030,\"Sort\":3,\"Name\":\"搜狗PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7010,\"Sort\":4,\"Name\":\"百度手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7015,\"Sort\":5,\"Name\":\"360手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7030,\"Sort\":6,\"Name\":\"搜狗手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7070,\"Sort\":7,\"Name\":\"神马\",\"MaxPage\":5,\"Enabled\":true}]";
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("searchTypeList", FastJsonUtil.parseObject(searchTypeList, List.class));
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		jsonObj.put("seofreezeday", "90");
		jsonObj.put("SeoFailedDay", "60");
		model.addAttribute("jsonData",jsonObj);
		return "/business/seo/apply";
	}
	
	/**功能:刷新状态
	 * @param url
	 * @param keyword
	 * @param searchType
	 * @param verifycode
	 * @return
	 */
	@RequestMapping("/client/business/seo/refreshstatus")
	@ResponseBody
	public ResultFontJS refreshStatus(@RequestParam(value="taskId",required=true) String taskId){
		if(StringUtils.isBlank(taskId)){
			return ResultFontJS.error("刷新任务的ID号不能为空");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", 1);
		return ResultFontJS.ok(map);
	}
	
	/**功能:多对一加词
	 * @param url
	 * @param keyword
	 * @param searchType
	 * @param verifycode
	 * @return
	 */
	@RequestMapping("/client/business/seo/importsingle")
	@ResponseBody
	public ResultFontJS importSingle(@RequestParam(value="url",required=true) String url, String keyword, String[] searchType, String verifycode){
		if(StringUtils.isBlank(url)){
			return ResultFontJS.error("地址不能为空");
		}
		if(StringUtils.isBlank(keyword)){
			return ResultFontJS.error("关键词不能为空");
		}
		if(null == searchType || searchType.length == 0){
			return ResultFontJS.error("搜索引擎不能为空");
		}
		if(StringUtils.isBlank(verifycode)){
			return ResultFontJS.error("验证码不能为空");
		}
		//TODO 验证码后台校验值对比
		//封装seo对像
		Map<String, List<Seo>> listMap = SeoWrapper.singleUroToSeo(this.getOemuser().getUserid(), url, keyword.trim().split("\r\n"), searchType);
		int count = seoService.addSeoTask(listMap);
		if(count >0){
			return ResultFontJS.ok(/*"提交完成，稍候请在列表中查看检测结果！<br/>本次请求不重复记录<b class='text-red'>"+seos.size()+"</b>个。失败<b class='text-red'>"+(seos.size()-count)+"</b>个，忽略<b class='text-red'>"+(seos.size()-count)+"</b>个，成功<b class='text-red'>"+ count +"</b>个！"*/);
		}
		return ResultFontJS.error("添加关键词查询失败");
	}
	/**功能:一对一加词
	 * @param url
	 * @param keyword
	 * @param searchType
	 * @param verifycode
	 * @return
	 */
	@RequestMapping("/client/business/seo/importmultiple")
	@ResponseBody
	public ResultFontJS importMultiple(@RequestParam(value="url",required=true) String url, String keyword, String searchType, String verifycode){
		if(StringUtils.isBlank(url)){
			return ResultFontJS.error("地址不能为空");
		}
		if(StringUtils.isBlank(keyword)){
			return ResultFontJS.error("关键词不能为空");
		}
		if(StringUtils.isBlank(verifycode)){
			return ResultFontJS.error("验证码不能为空");
		}
		if(StringUtils.isBlank(searchType)){
			return ResultFontJS.error("搜索引擎不能为空");
		}
		//TODO 验证码后台校验值对比
		//封装seo对像
		Map<String, List<Seo>> listMap = SeoWrapper.multipleUroToSeo(this.getOemuser().getUserid(), url.trim().split("\r\n"), keyword.trim().split("\r\n"), searchType);
		int count = seoService.addSeoTask(listMap);
		if(count >0){
			return ResultFontJS.ok(/*"提交完成，稍候请在列表中查看检测结果！<br/>本次请求不重复记录<b class='text-red'>"+seos.size()+"</b>个。失败<b class='text-red'>"+(seos.size()-count)+"</b>个，忽略<b class='text-red'>"+(seos.size()-count)+"</b>个，成功<b class='text-red'>"+ count +"</b>个！"*/);
		}
		return ResultFontJS.error("添加关键词查询失败");
	}
	
	
	@RequestMapping("/client/business/seo/batchdel")
	@ResponseBody
	public ResultFontJS batchDel(@RequestParam(value="action",required=true) String action, Integer[] taskIds){
		if( taskIds == null || taskIds.length <= 0){
			return ResultFontJS.error("删除任务的ID号不能为空请检查");
		}
		seoService.deleteSeoTasks(taskIds);
		return ResultFontJS.ok("关键词删除成功");
	}
	
	/**关键词批量购买
	 * @param action
	 * @param taskIds
	 * @return
	 */
	@RequestMapping("/client/business/seo/batchapply")
	@ResponseBody
	public ResultFontJS batchApply(@RequestParam(value="action",required=true) String action, Integer[] taskIds){
		if( taskIds == null || taskIds.length <= 0){
			return ResultFontJS.error("需要购买的任务的ID号不能为空请检查");
		}
		//关键词购买校验
		boolean check = seoService.checkAmountForSeoApply(taskIds, this.getOemuser().getUserid());
		BigDecimal frozen = seoService.frozenAmountOfPayment(taskIds);
		if(!check){
			String msg = "您需要冻结<b class='text-red'>￥" + frozen.toString() + "</b>元！抱歉，由于可用金额不足，请充值后再购买！";
			ResultFontJS rs = new ResultFontJS(msg);
			rs.put("result", false);
			rs.put("reload", true);
			return rs;
		}
		//关键词购买
		seoService.applySeoTasks(taskIds, this.getOemuser().getUserid(), frozen);
		return ResultFontJS.ok("云排名关键词购买成功");
	}
	
	/**客户端关键词（批量/单个）启动
	 * @param action
	 * @param taskIds
	 * @return
	 */
	@RequestMapping("/client/business/seo/batchstart")
	@ResponseBody
	public ResultFontJS batchStart(@RequestParam(value="action",required=true) String action, Integer[] taskIds){
		if( taskIds == null || taskIds.length <= 0){
			return ResultFontJS.error("任务的ID号不能为空请检查");
		}
		//关键词购买校验
		boolean check = seoService.checkAmountForSeoApply(taskIds, this.getOemuser().getUserid());
		BigDecimal frozen = seoService.frozenAmountOfPayment(taskIds);
		if(!check){
			String msg = "您需要冻结<b class='text-red'>￥" + frozen.toString() + "</b>元！抱歉，由于可用金额不足，请充值后再购买！";
			ResultFontJS rs = new ResultFontJS(msg);
			rs.put("result", false);
			rs.put("reload", true);
			return rs;
		}
		seoService.startSeoTasks(taskIds);
		return ResultFontJS.ok("关键词任务启动成功");
	}
}
