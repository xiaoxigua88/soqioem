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

import com.soqi.common.utils.CookieUtils;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemuser;
import com.soqi.oem.gentry.Useraccountdetail;
import com.soqi.oem.gentry.Userrecharge;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.FinanceService;
import com.soqi.system.service.OemCountService;
import com.soqi.system.service.UserService;
import com.soqi.system.vo.Filter;
import com.soqi.system.vo.Page;

@Controller
public class ClientFinanceController extends BaseController{
	private final Logger logger = LoggerFactory.getLogger(ClientFinanceController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private FinanceService financeService;
	
	@RequestMapping("/client/finance/accountdetail")
	public String accountdetail(Model model, Filter filter, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="20";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		List<Useraccountdetail> lst = financeService.qryUserAcDtlsByUserid(this.getOemuser().getUserid(), start, size);
		int total = financeService.qryCountUserAcDtlsByUserid(this.getOemuser().getUserid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		model.addAttribute("jsonData",jsonObj);
		return "/finance/accountdetail";
    }
	
	@RequestMapping("/client/finance/rechargelist")
	public String rechargelist(Model model, Filter filter, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		Oemuser  user = userService.qryOemuser(this.getOemuser().getUserid());
		if(user.getTotalamount() == null){
			user.setTotalamount(BigDecimal.ZERO);
			user.setFreezeamount(BigDecimal.ZERO);
			user.setAvailableamount(BigDecimal.ZERO);
		}
		user.setAvailableamount(user.getTotalamount().subtract(user.getFreezeamount()));
		user.setPaymin(BigDecimal.valueOf(100.00));
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="20";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		List<Userrecharge> lst = financeService.qryUserRechargesByUserid(this.getOemuser().getUserid(), start, size);
		int total = financeService.qryCountUserRechargesByUserid(this.getOemuser().getUserid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		jsonObj.put("user", user);
		model.addAttribute("jsonData",jsonObj);
		return "/finance/rechargelist";
	}
	
	@RequestMapping("/client/finance/recharge")
	public String recharge(Model model){
		Oemuser  user = userService.qryOemuser(this.getOemuser().getUserid());
		if(user.getTotalamount() == null){
			user.setTotalamount(BigDecimal.ZERO);
			user.setFreezeamount(BigDecimal.ZERO);
			user.setAvailableamount(BigDecimal.ZERO);
		}
		user.setAvailableamount(user.getTotalamount().subtract(user.getFreezeamount()));
		user.setPaymin(BigDecimal.valueOf(100.00));
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("user", user);
		model.addAttribute("jsonData",jsonObj);
		return "/finance/recharge";
	}
	@RequestMapping("/client/finance/contract")
	public String contract(Customer customer){
		return "/finance/contract";
	}
}
