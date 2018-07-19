package com.soqi.oem.control;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soqi.common.constants.Constant;
import com.soqi.common.utils.BigDecimalUtil;
import com.soqi.common.utils.CookieUtils;
import com.soqi.common.utils.FastJsonUtil;
import com.soqi.common.utils.IDUtils;
import com.soqi.common.utils.PriceTemplWrapper;
import com.soqi.common.utils.ResultFontJS;
import com.soqi.common.utils.ValidateUtils;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oembase;
import com.soqi.oem.gentry.Oempricetempl;
import com.soqi.oem.gentry.Oemrecharge;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.FinanceService;
import com.soqi.system.service.OemBaseService;
import com.soqi.system.service.PriceTemplService;
import com.soqi.system.vo.Filter;
import com.soqi.system.vo.Page;

@Controller
public class ChildOemController extends BaseController {
	@Autowired
	private OemBaseService oemBaseService;
	@Autowired
	private FinanceService rechargeService ;
	@Autowired
	private PriceTemplService priceTemplService;
	
	@RequestMapping("/oemmanager/child/childoemlist")
	public String childoemlist(Model model, Filter filter, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="1";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		List<Oembase> lst = oemBaseService.qryChildOemsByOemId(this.getCustomer().getOemid(), start, size);
		int total = oemBaseService.qryCountChildOemsByOemId(this.getCustomer().getOemid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		String statusList = "[{\"name\":\"All\",\"value\":-999,\"description\":\"全部\"},{\"name\":\"Normal\",\"value\":1,\"description\":\"正常\"},{\"name\":\"Profile\",\"value\":2,\"description\":\"待完善\"},{\"name\":\"Lock\",\"value\":3,\"description\":\"锁定\"}]";
		jsonObj.put("statusList", FastJsonUtil.parseObject(statusList, List.class));
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/child/oemlist";
    }
	
	@RequestMapping("/oemmanager/child/childoemsave")
	@ResponseBody
	public ResultFontJS childoemsave(Customer customer, Oembase oembase, HttpServletRequest req){
		oembase.setParentoemid(this.getCustomer().getOemid());
		oemBaseService.saveOemBase(customer, oembase);
		return ResultFontJS.ok("创建代理成功！");
	}
	
	
	/**功能：查询当前需要修改的信息
	 * @param oemid
	 * @param req
	 * @return
	 */
	@RequestMapping("/oemmanager/child/childoemedit")
	@ResponseBody
	public ResultFontJS childoemEdit(@RequestParam(value="oemid", required=true) Integer oemid, HttpServletRequest req){
		Oembase oembase = oemBaseService.qryOembaseInfo(oemid);
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("oembase", oembase);
		return ResultFontJS.ok(jsonObj);
	}
	
	@RequestMapping("/oemmanager/child/childoemupdate")
	@ResponseBody
	public ResultFontJS childoemUpdate(Oembase oembase, HttpServletRequest req){
		int c = oemBaseService.childoemUpdate(oembase);
		return c > 0 ? ResultFontJS.ok("代理数据修改成功！") : ResultFontJS.error("代理修改失败");
	}
	
	/*
	 * 代理充值
	 */
	@RequestMapping("/oemmanager/child/oemrecharge")
	@ResponseBody
	public ResultFontJS oemRecharge(@RequestParam(value="rechargeAmount",required=true) Integer rechargeAmount, @RequestParam(value="rechargeMemo",required=false) String rechargeMemo, @RequestParam(value="oemid",required=true) Integer oemid){
		Oemrecharge recharge = new Oemrecharge();
		Date date  = new Date();
		recharge.setAddtime(date);
		recharge.setAmount(BigDecimal.valueOf(rechargeAmount));
		recharge.setMemo(rechargeMemo);
		recharge.setOemid(oemid);
		recharge.setOrderid(Long.valueOf(IDUtils.createID()));
		recharge.setPaytype(Constant.REHARGE_CASH);
		recharge.setStatus(1);
		recharge.setFinishtime(date);
		rechargeService.oemRecharge(recharge);
		return ResultFontJS.ok("用户充值成功");
	}
	
	@RequestMapping("/oemmanager/child/seopricechange")
	@ResponseBody
	public ResultFontJS seopricechange(@RequestParam(value="oemid", required=true) Integer oemid){
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		String searchTypeList = "[{\"TypeId\":0,\"Sort\":0,\"Name\":\"全部搜索\",\"MaxPage\":5,\"Enabled\":false},{\"TypeId\":1010,\"Sort\":1,\"Name\":\"百度PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":1015,\"Sort\":2,\"Name\":\"360PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":1030,\"Sort\":3,\"Name\":\"搜狗PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7010,\"Sort\":4,\"Name\":\"百度手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7015,\"Sort\":5,\"Name\":\"360手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7030,\"Sort\":6,\"Name\":\"搜狗手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7070,\"Sort\":7,\"Name\":\"神马\",\"MaxPage\":5,\"Enabled\":true}]";
		jsonObj.put("searchTypeList", FastJsonUtil.parseObject(searchTypeList, List.class));
		List<Oempricetempl> lst = priceTemplService.qryOempricetemplsByOemid(oemid);
		if(null != lst && !lst.isEmpty()){
			jsonObj.put("lst", lst);
		}
		jsonObj.put("oemid", oemid);
		return ResultFontJS.ok(jsonObj);
	}
	
	@RequestMapping("/oemmanager/child/savepricetempl")
	@ResponseBody
	public ResultFontJS savePriceTempl(@RequestParam(value="searchtype",required=true) String[] searchtype, @RequestParam(value="oemid",required=true) Integer oemid, HttpServletRequest req){
		String errText = null;
		String name = null;
		for (String st : searchtype) {
			String discounttype_ = req.getParameter("discounttype_"+st);
			String fixprice_ = req.getParameter("fixprice_"+st);
			String minprice_ = req.getParameter("minprice_"+st);
			String maxprice_ = req.getParameter("maxprice_"+st);
			String ratio_ = req.getParameter("ratio_"+st);
			if(StringUtils.isBlank(discounttype_)){
				errText = "折扣类型并未设置请检查页面";
				name = "discounttype_" + st;
			}else{
				if(StringUtils.equals(discounttype_, "1")){
					//一口价
					if(StringUtils.isBlank(fixprice_)){
						errText = "一口价不能为空";
						name = "fixprice_" + st;
					}
					//验证金额
					if(!ValidateUtils.PositiveNumber(fixprice_)){
						errText = "请正确输入金额格式";
						name = "fixprice_" + st;
					}
				}else if(StringUtils.equals(discounttype_, "2")){
					//折扣价、最小、最大、比率
					if(StringUtils.isBlank(minprice_)){
						errText = "最小金额不能为空";
						name = "minprice_" + st;
					}
					//验证金额
					if(!ValidateUtils.PositiveNumber(minprice_)){
						errText = "请正确输入最小金额格式";
						name = "minprice_" + st;
					}
					if(StringUtils.isBlank(maxprice_)){
						errText = "最大金额不能为空";
						name = "maxprice_" + st;
					}
					//验证金额
					if(!ValidateUtils.PositiveNumber(maxprice_)){
						errText = "请正确输入最大金额格式";
						name = "maxprice_" + st;
					}
					if(StringUtils.isBlank(ratio_)){
						errText = "折扣率不能为空";
						name = "ratio_" + st;
					}
					//验证金额
					if(!ValidateUtils.PositiveNumber(ratio_)){
						errText = "请正确输入金额格式";
						name = "ratio_" + st;
					}
					//比较区间大小
					if(!BigDecimalUtil.compareSize(maxprice_, minprice_)){
						errText = "金额区间最大小最设置不正确";
						name = "minprice_" + st;
					}
					//和0-1比较
					if(!(Double.valueOf(ratio_)>=0 && Double.valueOf(ratio_)<=1)){
						errText = "请输入0到1之间的小数、不包括0、1";
						name = "ratio_" + st;
					}
				}
			}
			if(errText != null && name != null){
				ResultFontJS rs = ResultFontJS.error(errText);
				rs.put("name",name);
				return rs;
			}
		}
		List<Oempricetempl> optList = PriceTemplWrapper.convertOempricetempls(searchtype, req, oemid);
		//查询子代理有没有配置价格模板
		List<Oempricetempl> lst = priceTemplService.qryOempricetemplsByOemid(oemid);
		if(null == lst || lst.isEmpty()){
			priceTemplService.batchInsertOemPriceTempls(optList);
		}else{
			int a = optList.get(0).getOemid().intValue();
			int b = lst.get(0).getOemid().intValue();
			if(a==b){
				priceTemplService.batchUpdateOemPriceTempls(optList);
			}else{
				ResultFontJS.error("策略模板无法更新");
			}
		}
		return ResultFontJS.ok("价格策略模板配置成功");
	}
}
