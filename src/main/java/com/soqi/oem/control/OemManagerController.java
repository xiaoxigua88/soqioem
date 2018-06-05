package com.soqi.oem.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soqi.common.utils.ResultDTO;
import com.soqi.oem.gentry.Customer;
import com.soqi.system.service.STService;
import com.soqi.system.service.UserService;

@Controller
public class OemManagerController {
	private final Logger logger = LoggerFactory.getLogger(OemManagerController.class);
	@Autowired
	private UserService userService;
	@Autowired
    private STService sf;
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
		return "/oemmanager/self/baseinfo";
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
