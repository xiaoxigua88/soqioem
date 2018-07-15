package com.soqi.oem.control;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soqi.common.exception.BDException;
import com.soqi.common.utils.CookieUtils;
import com.soqi.common.utils.EncrypDES;
import com.soqi.common.utils.MD5Utils;
import com.soqi.common.utils.ResultFontJS;
import com.soqi.common.utils.ShiroUtils;
import com.soqi.oem.gentry.Customer;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.OemCustomerService;
import com.soqi.system.service.UserService;
import com.soqi.system.vo.Filter;
import com.soqi.system.vo.Page;

@Controller
public class OemManagerController extends BaseController{
	private final Logger logger = LoggerFactory.getLogger(OemManagerController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private OemCustomerService oemCustomerService;
	/**管理员页面欢迎页
	 * @param model
	 * @return
	 */
	@RequestMapping("/oemmanager/main")
	public String adminMainPage(Model model){
		//添加cookie
		String oem_manager = CookieUtils.getCookie("oem_manager");
		if(StringUtils.isNotBlank(oem_manager)){
			CookieUtils.removeCookie(oem_manager);
		}
		try {
			String encrypStr = EncrypDES.encryption(this.getCustomer().getDomain().trim()+"_"+this.getCustomer().getMobile().trim(), EncrypDES.DES_SECRETKEY);
			CookieUtils.addCookie("oem_manager", encrypStr);
		} catch (Exception e) {
			new BDException("字符串加密失败");
		}
		return "/oemmanager/default";
    }
	//@RequiresPermissions("1102:3")//权限管理;
	@RequestMapping("/oemmanager/self/customerEdit")
	public String customerEdit(Model model){
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("customer", this.getCustomer());
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/self/baseinfo";
    }
	
	/**功能：个人资料信息修改、修改前要验证登录密码是否有效
	 * @param customer
	 * @param verifyCode
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("/oemmanager/self/updateinfo")
	@ResponseBody
	public ResultFontJS updateInfo(Customer customer, @RequestParam(value="verifyCode",required=true) String verifyCode){
		if(customer == null){
			return ResultFontJS.error("传参失败、请检查参数");
		}
		customer.setCustomerid(this.getCustomer().getCustomerid());
		//根据customerid获取customer
		Customer qrycst = oemCustomerService.selectByCustomerId(this.getCustomer().getCustomerid());
		//页面传过来的密码组装成MD5与数据库存的比较
		String verifyCodeMD5 = MD5Utils.encrypt(customer.getMobile(), verifyCode);
		if(StringUtils.equals(verifyCodeMD5, qrycst.getPwd())){
			int count = oemCustomerService.updateSelfInfo(customer);
			Customer newcst = oemCustomerService.qryCustomerAndOemBaseInfo(this.getCustomer().getCustomerid());
			//一种是通过重新更新Principal达到更新内存中用户信息的目的
			ShiroUtils.updatePrincipal(newcst);
			//另一种通过对像属性拷贝的方式达到更新内存中用户信息的目的
			//BeanUtils.copyProperties(newcst, this.getCustomer());
			return count > 0 ? ResultFontJS.ok("数据修改成功") : ResultFontJS.error("数据更新失败");
		}else{
			return ResultFontJS.error("密码不一致不更新个人资料");
		}
		
	}
	
	@RequestMapping("/oemmanager/self/customerUpdate")
	@ResponseBody
	public ResultFontJS customerEdit(Customer customer){
		return ResultFontJS.ok();
    }
	
	@RequestMapping("/oemmanager/self/passwordEdit")
	public String passwordEdit(Model model){
		return "/oemmanager/self/password";
    }
	
	@RequestMapping("/oemmanager/self/passwordUpdate")
	@ResponseBody
	public ResultFontJS passwordUpdate(@RequestParam(value="oldPassword", required=true) String oldPassword, @RequestParam(value="password", required=true) String password, @RequestParam(value="password2", required=true) String password2){
		if(StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password) || StringUtils.isBlank(password2)){
			return ResultFontJS.error("密码参数为空请检查");
		}
		if(!StringUtils.equals(password, password2)){
			return ResultFontJS.error("新密码输入不一致请重新输入");
		}
		if(StringUtils.equals(password, oldPassword)){
			return ResultFontJS.error("新旧密码输入一致不作更改");
		}
		Customer cus = oemCustomerService.selectByCustomerId(this.getCustomer().getCustomerid());
		String pwd = MD5Utils.encrypt(this.getCustomer().getMobile(),oldPassword);
		if(!StringUtils.equals(pwd, cus.getPwd())){
			return ResultFontJS.error("旧密码输入错误、请得新输入");
		}
		Customer customer = new Customer();
		customer.setCustomerid(cus.getCustomerid());
		String newpwd = MD5Utils.encrypt(this.getCustomer().getMobile(), password);
		customer.setPwd(newpwd);
		int c = oemCustomerService.updateSelfInfo(customer);
		return c > 0 ? ResultFontJS.ok("密码更新成功") : ResultFontJS.error("密码更新失败");
    }
	
	@RequestMapping("/oemmanager/self/operateLog")
	public String operateLog( @RequestParam(value="page", defaultValue="1") int pageNo, Filter filter){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="1";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		
		
		Page pager = new Page(pageNo, size, 0);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		
		return "/oemmanager/self/operatelog";
    }
}
