package com.soqi.client.control;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soqi.oem.gentry.Oemuser;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.LogService;
import com.soqi.system.service.UserService;

@Controller
public class CilentHomeController extends BaseController{
	private final Logger logger = LoggerFactory.getLogger(CilentHomeController.class);
	@Autowired
	private UserService userService;
	@Autowired
    private LogService logService;
	/**客户页面欢迎页
	 * @param model
	 * @return
	 */
	@RequestMapping("/client/main")
	public String clientMainPage(HttpServletRequest req){
		//添加用户登录成功日志
		Oemuser user = this.getOemuser();
		if(!user.isIsinsertlog()){
			logger.debug("用户登录日志添加开始");
			logService.addUserLoginLog(user.getUserid(), user.getOemid(), req);
			logger.debug("用户登录日志添加结束");
			user.setIsinsertlog(true);
		}
		return "/default";
    }
}
