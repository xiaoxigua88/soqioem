package com.soqi.client.control;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soqi.common.utils.ShiroUtils;
import com.soqi.common.utils.WebAddrUtils;
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
	public String clientMainPage(Model model, HttpServletRequest req){
		//添加用户登录成功日志
		Oemuser user = this.getOemuser();
		if(!user.isIsinsertlog()){
			logger.debug("用户登录日志添加开始");
			logService.addUserLoginLog(user.getUserid(), user.getOemid(), req);
			logger.debug("用户登录日志添加结束");
			//更新用户内存信息
			user.setLogincount(user.getLogincount()+1);
			user.setLastlogintime(user.getCurrentlogintime());
			user.setLastloginip(user.getCurrentloginip());
			user.setCurrentlogintime(new Date());
			user.setCurrentloginip(WebAddrUtils.getIpAddr(req));
			ShiroUtils.updatePrincipal(user);
			logger.debug("更新用户登录信息开始");
			userService.updateOemuser(user);
			logger.debug("更新用户登录信息结束");
			user.setIsinsertlog(true);
		}
		Oemuser newcst = new Oemuser();
		BeanUtils.copyProperties(user, newcst);
		newcst.setPwd(null);
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("user", newcst);
		model.addAttribute("jsonData",jsonObj);
		return "/default";
    }
}
