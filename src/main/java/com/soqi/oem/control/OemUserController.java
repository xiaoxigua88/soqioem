package com.soqi.oem.control;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soqi.common.constants.Constant;
import com.soqi.common.utils.CookieUtils;
import com.soqi.common.utils.IDUtils;
import com.soqi.common.utils.MD5Utils;
import com.soqi.common.utils.ResultDTO;
import com.soqi.common.utils.ResultFontJS;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemuser;
import com.soqi.oem.gentry.Userloginlog;
import com.soqi.oem.gentry.Userrecharge;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.LogService;
import com.soqi.system.service.FinanceService;
import com.soqi.system.service.UserService;
import com.soqi.system.vo.Filter;
import com.soqi.system.vo.Page;

@Controller
public class OemUserController extends BaseController{
	private final Logger logger = LoggerFactory.getLogger(OemUserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private LogService LogService;
	@Autowired
	private FinanceService rechargeService ;
	//@RequiresPermissions("1301:7")//权限管理;
	@RequestMapping(value="/oemmanager/userinfo/userlist")
	public String userlist(Model model, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
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
		List<Oemuser> lst = userService.qryUsersByOemId(ct.getOemid(), start, size);
		int total = userService.selectCountByOemId(ct.getOemid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		jsonObj.put("oemid", ct.getOemid());
		//jsonObj.put("username", ct.getCallname());
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/userinfo/userlist";
    }
	
	@RequiresPermissions("1301:16")//权限管理;
	@RequestMapping("/oemmanager/userinfo/useredit")
	@ResponseBody
	public ResultFontJS useredit(@RequestParam(value="userid",required=true) Integer userid, HttpServletRequest req,  HttpServletResponse resp){
		Oemuser oemuser = userService.qryOemuser(userid);
		if(oemuser !=null){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("user", oemuser);
			return ResultFontJS.ok(map);
		}else{
			return ResultFontJS.error("查询数据失败");
		}
	}
	
	//@RequiresPermissions("1301:16")
	@RequestMapping("/oemmanager/userinfo/userUpdate")
	@ResponseBody
	public ResultFontJS userUpdate(Oemuser user, HttpServletRequest req,  HttpServletResponse resp){
		
		if(StringUtils.isEmpty(user.getMobile())){
			return ResultFontJS.error("手机号参为空");
		}
		if(user.getOemid()==null || user.getOemid()==0){
			Customer ct = this.getCustomer();
			user.setOemid(ct.getOemid());
		}
		//查询出当前数据
		Oemuser currentUser = userService.qryOemuser(user.getUserid());
		if(!StringUtils.equals(currentUser.getMobile(), user.getMobile())){
			//根据手机号和代理号、用户编号判断会不会是手机号修改成其它手机号
			boolean bl = userService.hasSameMobileOfOEM(user.getOemid(), user.getMobile(), user.getUserid());
			if(!bl){
				return ResultFontJS.error("该手机号已经注册请更换");
			}
			//用户密码生成
			user.setPwd(MD5Utils.encrypt(user.getMobile(), "12345678"));
		}
		int record = userService.updateOemuser(user);
		if(record >0){
			return ResultFontJS.ok("用户数据更新成功");
		}else{
			return ResultFontJS.error("用户数据更新失败");
		}
	}
	
	//@RequiresPermissions("1301:8")
	@RequestMapping("/oemmanager/userinfo/useradd")
	@ResponseBody
	public ResultFontJS useradd(Oemuser user, HttpServletRequest req,  HttpServletResponse resp){
		if(StringUtils.isEmpty(user.getMobile())){
			return ResultFontJS.error("手机号参为空");
		}
		Customer ct = this.getCustomer();
		if(user.getOemid()==null || user.getOemid()==0){
			user.setOemid(ct.getOemid());
		}
		//根据手机号和代理号判断会不会有重号加入
		boolean bl = userService.hasSameMobileOfOEM(user.getOemid(), user.getMobile(), null);
		if(!bl){
			return ResultFontJS.error("您已经添加过相同的用户");
		}
		//用户密码生成
		user.setPwd(MD5Utils.encrypt(user.getMobile(), "12345678"));
		//用户所属站点绑定
		user.setDomain(ct.getDomain());
		int record = userService.saveOemuser(user);
		if(record >0){
			return ResultFontJS.ok("数据保存成功");
		}else{
			return ResultFontJS.error("数据保存失败");
		}
	}
	
	/**功能：初始化用户密码
	 * @param action
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/oemmanager/userinfo/userinitpwd")
	@ResponseBody
	public ResultFontJS userinitpwd(@RequestParam(value="action",required=true) String action, @RequestParam(value="userid",required=true) Integer userid){
		//更新用户
		if(userid != null){
			//根据userid查询用户信息
			Oemuser oemuser = userService.qryOemuser(userid);
			if(oemuser == null){
				return ResultFontJS.error("未查询到需要修改密码的用户");
			}
			if(oemuser.getOemid().intValue() != this.getCustomer().getOemid().intValue()){
				return ResultFontJS.error("不是同一个代理下的用户无法修改其密码");
			}
			String pwd = userService.InitPwd(oemuser);
			if (pwd != null) {
				return ResultFontJS.ok("密码重置成功，新密码为"+pwd, false).put("time", 10);
			}
		}
		return ResultFontJS.error();
	}
	
	
	@RequestMapping("/oemmanager/userinfo/userrecharge")
	@ResponseBody
	public ResultFontJS userRecharge(@RequestParam(value="rechargeAmount",required=true) Integer rechargeAmount, @RequestParam(value="rechargeMemo",required=false) String rechargeMemo, @RequestParam(value="userid",required=true) Integer userid){
		Userrecharge recharge = new Userrecharge();
		Date date  = new Date();
		recharge.setAddtime(date);
		recharge.setAmount(BigDecimal.valueOf(rechargeAmount));
		recharge.setMemo(rechargeMemo);
		recharge.setUserid(userid);
		recharge.setOrderid(Long.valueOf(IDUtils.createID()));
		recharge.setPaytype(Constant.REHARGE_CASH);
		recharge.setStatus(1);
		recharge.setFinishtime(date);
		rechargeService.userRecharge(recharge);
		return ResultFontJS.ok("用户充值成功");
	}
	
	@RequiresPermissions("1301:16")//权限管理;
	@RequestMapping("/oemmanager/userinfo/recharge")
	@ResponseBody
	public ResultDTO recharge( @RequestParam(value="userid",required=false) Integer userid, HttpServletRequest req,  HttpServletResponse resp){
		return null;
	}

	
	@RequestMapping("/oemmanager/userinfo/userloginlog")
	public String userloginlog(Model model, @RequestParam(value="page", defaultValue="1") int pageNo){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="1";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		Filter filter = new Filter("desc", "", "");
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		List<Userloginlog> lst =LogService.qryUserLogsByOemid(this.getCustomer().getOemid(), start, size);
		int total = LogService.qryUserLogsCountByOemid(this.getCustomer().getOemid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/userinfo/userloginlog";
	}
	
	@RequestMapping("/oemmanager/userinfo/verify")
	public String verify(Model model, @RequestParam(value="page", defaultValue="1") int pageNo){
		
		return "/oemmanager/userinfo/verify";
	}
	
}
