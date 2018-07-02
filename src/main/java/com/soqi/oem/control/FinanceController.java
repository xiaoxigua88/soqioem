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
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemaccountdetail;
import com.soqi.oem.gentry.Oemrecharge;
import com.soqi.oem.gentry.Useraccountdetail;
import com.soqi.oem.gentry.Userrecharge;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.FinanceService;
import com.soqi.system.vo.Filter;
import com.soqi.system.vo.Page;

@Controller
public class FinanceController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(FinanceController.class);
	@Autowired
	private FinanceService financeService;
	
	
	@RequestMapping("/oemmanager/userinfo/accountdetail")
	public String userAccountdetail(Model model, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="20";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		Filter filter = new Filter("desc", "", "");
		Customer ct = this.getCustomer();
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		List<Useraccountdetail> lst = financeService.qryUserAcDtlsByOemid(ct.getOemid(), start, size);
		int total = financeService.qryCountUserAcDtlsByOemid(ct.getOemid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		jsonObj.put("oemid", ct.getOemid());
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/userinfo/accountdetail";
    }
	
	@RequestMapping("/oemmanager/userinfo/rechargelist")
	public String userRechargelist(Model model, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="20";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		Filter filter = new Filter("desc", "", "");
		Customer ct = this.getCustomer();
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		List<Userrecharge> lst = financeService.qryUserRechargesByOemid(ct.getOemid(), start, size);
		int total = financeService.qryCountUserRechargesByOemid(ct.getOemid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		jsonObj.put("oemid", ct.getOemid());
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/userinfo/rechargelist";
	}
	
	@RequestMapping("/oemmanager/child/accountdetail")
	public String oemAccountdetail(Model model, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="20";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		Filter filter = new Filter("desc", "", "");
		Customer ct = this.getCustomer();
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		List<Oemaccountdetail> lst = financeService.qryOemAcDtlsByOemid(ct.getOemid(), start, size);
		int total = financeService.qryCountOemAcDtlsByOemid(ct.getOemid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		jsonObj.put("oemid", ct.getOemid());
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/child/accountdetail";
	}
	
	@RequestMapping("/oemmanager/child/oembill")
	public String oemBill(Model model, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="20";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		Filter filter = new Filter("desc", "", "");
		Customer ct = this.getCustomer();
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		List<Oemaccountdetail> lst = financeService.qrySecondOemPersonBill(ct.getOemid(), start, size);
		int total = financeService.qryCountSecondOemPersonBill(ct.getOemid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		jsonObj.put("oemid", ct.getOemid());
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/child/oembill";
	}
	
	@RequestMapping("/oemmanager/child/rechargelist")
	public String oemRechargelist(Model model, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="20";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		Filter filter = new Filter("desc", "", "");
		Customer ct = this.getCustomer();
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		List<Oemrecharge> lst = financeService.qryOemRechargesByOemid(ct.getOemid(), start, size);
		int total = financeService.qryCountOemRechargesByOemid(ct.getOemid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		jsonObj.put("oemid", ct.getOemid());
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/child/rechargelist";
	}
}
