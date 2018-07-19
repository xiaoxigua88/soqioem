package com.soqi.oem.control;

import java.util.ArrayList;
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

import com.soqi.common.utils.BigDecimalUtil;
import com.soqi.common.utils.FastJsonUtil;
import com.soqi.common.utils.PriceTemplWrapper;
import com.soqi.common.utils.ResultFontJS;
import com.soqi.common.utils.ValidateUtils;
import com.soqi.oem.gentry.Apipricechange;
import com.soqi.oem.gentry.Apipricechangedetail;
import com.soqi.oem.gentry.Oemserviceconfig;
import com.soqi.oem.gentry.Pricetempl;
import com.soqi.oem.gentry.Pricetempldtail;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.ApiPriceChangeService;
import com.soqi.system.service.OemServiceConfigService;
import com.soqi.system.service.PriceTemplService;

/**代理端系统信息控制层
 * @author 孙傲
 *
 */
@Controller
public class OemSysConfController extends BaseController {
	@Autowired
	private PriceTemplService ptService;
	@Autowired
	private ApiPriceChangeService apcService;
	@Autowired
	private OemServiceConfigService serviceConfig;
	/**
	 *价格策略配置
	 */
	@RequestMapping("/oemmanager/sysconfig/priceconfig")
	public String priceConfig(Model model,@RequestParam(value="templtype",required=true) Byte templtype){
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		String searchTypeList = "[{\"TypeId\":0,\"Sort\":0,\"Name\":\"全部搜索\",\"MaxPage\":5,\"Enabled\":false},{\"TypeId\":1010,\"Sort\":1,\"Name\":\"百度PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":1015,\"Sort\":2,\"Name\":\"360PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":1030,\"Sort\":3,\"Name\":\"搜狗PC\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7010,\"Sort\":4,\"Name\":\"百度手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7015,\"Sort\":5,\"Name\":\"360手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7030,\"Sort\":6,\"Name\":\"搜狗手机\",\"MaxPage\":5,\"Enabled\":true},{\"TypeId\":7070,\"Sort\":7,\"Name\":\"神马\",\"MaxPage\":5,\"Enabled\":true}]";
		jsonObj.put("searchTypeList", FastJsonUtil.parseObject(searchTypeList, List.class));
		List<Pricetempldtail> lst = ptService.qryPriceTempl(this.getCustomer().getOemid(), templtype);
		if(null != lst && !lst.isEmpty()){
			jsonObj.put("lst", lst);
		}
		jsonObj.put("templtype", templtype);
		model.addAttribute("templtype",templtype.toString());
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/sysconfig/priceconfig";
	}
	
	@RequestMapping("/oemmanager/sysconfig/savepricetempl")
	@ResponseBody
	public ResultFontJS savePriceTempl(@RequestParam(value="templtype",required=true) Byte templtype, @RequestParam(value="searchtype",required=true) String[] searchtype, String[] pricetemplid, HttpServletRequest req){
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
		Pricetempl pricetempl = PriceTemplWrapper.converParamToPricetempl(templtype, searchtype, req, this.getCustomer().getOemid(), pricetemplid);
		//选查询当前代理账户有没有设置价格策略模板
		List<Pricetempldtail> lst = ptService.qryPriceTempl(this.getCustomer().getOemid(), templtype);
		if(null == lst || lst.isEmpty()){
			ptService.addPriceTemplToChildOemByOem(pricetempl, pricetempl.getPtdsList());
		}else{
			int a = pricetempl.getPtdsList().get(0).getPricetemplid().intValue();
			int b = lst.get(0).getPricetemplid().intValue();
			if(a==b){
					ptService.updatePriceTemplToChildOemByOem(pricetempl, pricetempl.getPtdsList());
			}else{
				ResultFontJS.error("策略模板无法更新");
			}
		}
		return ResultFontJS.ok("价格策略模板配置成功");
	}
	
	/**
	 *接口价格调整
	 */
	@RequestMapping("/oemmanager/sysconfig/apipricechange")
	public String apiPriceChange(Model model){
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		Apipricechange change = apcService.getApipricechange(this.getCustomer().getOemid());
		if(null != change){
			List<Apipricechangedetail> lst = change.getAcdList();
			if(null != lst && !lst.isEmpty()){
				jsonObj.put("lst", lst);
			}
			jsonObj.put("id", change.getId());
			jsonObj.put("maxprice", change.getMaxprice());
			jsonObj.put("minprice", change.getMinprice());
		}
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/sysconfig/apipricechange";
	}
	
	@RequestMapping("/oemmanager/sysconfig/saveapipricechange")
	@ResponseBody
	public ResultFontJS saveApiPriceChange(@RequestParam(value="groupLength",required=true) Byte groupLength,Apipricechange tosave,HttpServletRequest req){
		String errText = null;
		String name = null;
		String minprice = req.getParameter("minprice");
		String maxprice = req.getParameter("maxprice");
		if(StringUtils.isBlank(minprice)){
			return ResultFontJS.error("价格限制最小值不能为空");
		}
		if(StringUtils.isBlank(maxprice)){
			return ResultFontJS.error("价格限制最大值不能为空");
		}
		if(!ValidateUtils.PositiveNumber(minprice)){
			ResultFontJS rs = ResultFontJS.error("请输入正确的金额格式");
			rs.put("name",minprice);
			return rs;
		}
		if(!ValidateUtils.PositiveNumber(maxprice)){
			ResultFontJS rs = ResultFontJS.error("请输入正确的金额格式");
			rs.put("name",maxprice);
			return rs;
		}
		if(!BigDecimalUtil.compareSize(maxprice, minprice)){
			ResultFontJS rs = ResultFontJS.error("请输入正确区间值，最小值要小于最大值");
			rs.put("name",minprice);
			return rs;
		}
		if(groupLength <= 0){
			return ResultFontJS.error("未设置调副区间、非法操作");
		}else{
			for(int i = 1 ; i<groupLength+1 ; i++){
				String pricefirst_ = req.getParameter("pricefirst_"+i);
				String pricelast_ = req.getParameter("pricelast_"+i);
				String range_ = req.getParameter("range_"+i);
				//非空校验
				if(StringUtils.isBlank(pricefirst_)){
					errText = "最低阀值";
					name = "pricefirst_" + i;
				}
				if(StringUtils.isBlank(pricelast_)){
					errText = "最高阀值";
					name = "pricelast_" + i;
				}
				if(StringUtils.isBlank(range_)){
					errText = "幅度不能为空";
					name = "range_" + i;
				}
				//验证金额
				if(!ValidateUtils.PositiveNumber(pricefirst_)){
					errText = "请正确输入金额格式";
					name = "pricefirst_" + i;
				}
				if(!ValidateUtils.PositiveNumber(pricelast_)){
					errText = "请正确输入金额格式";
					name = "pricelast_" + i;
				}
				if(!ValidateUtils.PositiveNumber(range_)){
					errText = "请正确输入幅度比如1.2或0.5等数字";
					name = "range_" + i;
				}
				if(errText != null && name != null){
					ResultFontJS rs = ResultFontJS.error(errText);
					rs.put("name",name);
					return rs;
				}
			}
		}
		
		Apipricechange change = apcService.getApipricechange(this.getCustomer().getOemid());
		//封装tosave对像
		PriceTemplWrapper.convertParamToApiPriceChange(groupLength, req, tosave, this.getCustomer().getOemid());
		if(change==null || change.getAcdList() == null || change.getAcdList().isEmpty()){
			apcService.saveApipricechange(tosave);
		}else{
			if(tosave.getId().intValue() == change.getId().intValue()){
				apcService.updateApipricechange(tosave);
			}else{
				ResultFontJS.error("接口调价无法更新");
			}
		}
		return ResultFontJS.ok("接口调价配置成功");
	}
	/**
	 *服务配置
	 */
	@RequestMapping("/oemmanager/sysconfig/serviceconfig")
	public String serviceConfig(Model model,HttpServletRequest req, HttpServletResponse res){
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		List<Oemserviceconfig> oscList = serviceConfig.getServiceConfigList(this.getCustomer().getOemid());
		if(null != oscList && !oscList.isEmpty()){
			jsonObj.put("lst", oscList);
		}
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/sysconfig/serviceconfig";
	}
	@RequestMapping("/oemmanager/sysconfig/serviceconfigsave")
	@ResponseBody
	public ResultFontJS serviceConfigSave(@RequestParam(value="oemid",required=true) Integer[] oemid,@RequestParam(value="serviceid",required=true) Integer[] serviceid,
			@RequestParam(value="status",required=true) Integer[] status){
		if(oemid.length != serviceid.length || serviceid.length != status.length || oemid.length != status.length){
			ResultFontJS.error("服务配置非法请求的url");
		}
		List<Oemserviceconfig> oscList = new ArrayList<Oemserviceconfig>();
		for(int i = 0; i < status.length; i++){
			Oemserviceconfig osc = new Oemserviceconfig();
			osc.setStatus(status[i]);
			osc.setOemid(oemid[0]);
			osc.setServiceid(serviceid[i]);
			oscList.add(osc);
		}
		serviceConfig.saveServiceConfigList(oscList);
		return ResultFontJS.ok("服务配置设置成功");
	}
}
