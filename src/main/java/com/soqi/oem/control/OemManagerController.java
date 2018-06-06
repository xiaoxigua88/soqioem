package com.soqi.oem.control;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soqi.common.utils.MD5Utils;
import com.soqi.common.utils.ResultDTO;
import com.soqi.common.utils.ResultFontJS;
import com.soqi.common.utils.ShiroUtils;
import com.soqi.oem.gentry.Customer;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.OemCustomerService;
import com.soqi.system.service.STService;
import com.soqi.system.service.UserService;

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
			Customer newcst = oemCustomerService.selectByCustomerId(this.getCustomer().getCustomerid());
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
	public ResultDTO customerEdit(Customer customer){
		return ResultDTO.ok();
    }
	
	@RequestMapping("/oemmanager/self/passwordEdit")
	public String passwordEdit(Model model){
		return "/oemmanager/self/password";
    }
	
	@RequestMapping("/oemmanager/self/passwordUpdate")
	@ResponseBody
	public ResultDTO passwordUpdate(Customer customer){
		return ResultDTO.ok();
    }
	
	@RequestMapping("/oemmanager/self/operateLog")
	public String operateLog(Model model){
		return "/oemmanager/self/operatelog";
    }
}
