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
import com.soqi.common.utils.CookieUtils;
import com.soqi.common.utils.FastJsonUtil;
import com.soqi.common.utils.IDUtils;
import com.soqi.common.utils.ResultFontJS;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oembase;
import com.soqi.oem.gentry.Oemrecharge;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.FinanceService;
import com.soqi.system.service.OemBaseService;
import com.soqi.system.vo.Filter;
import com.soqi.system.vo.Page;

@Controller
public class ChildOemController extends BaseController {
	@Autowired
	private OemBaseService oemBaseService;
	@Autowired
	private FinanceService rechargeService ;
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
}
