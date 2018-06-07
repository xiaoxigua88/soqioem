package com.soqi.oem.control;

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

import com.soqi.common.utils.CookieUtils;
import com.soqi.common.utils.FastJsonUtil;
import com.soqi.common.utils.MD5Utils;
import com.soqi.common.utils.ResultFontJS;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oembase;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.OemBaseService;
import com.soqi.system.vo.Filter;
import com.soqi.system.vo.Page;

@Controller
public class ChildOemController extends BaseController {
	@Autowired
	private OemBaseService oemBaseService;
	
	@RequestMapping("/oemmanager/child/childoemlist")
	public String childoemlist(Model model, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="1";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		Filter filter = new Filter("desc", "", "");
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
}
