package com.soqi.system.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soqi.common.constants.Constant;
import com.soqi.common.utils.ResultDTO;
import com.soqi.common.utils.WebAddrUtils;
import com.soqi.system.service.STService;
import com.soqi.system.service.UserService;
import com.soqi.system.shiro.UsernamePasswordUserTypeToken;


@Controller
public class LoginController {
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private UserService userService;
	@Autowired
    private STService sf;
	@RequestMapping("/login")
	public String login(Model model){
		return "/login";
    }
	/**代理管理员页面登录
	 * @param model
	 * @return
	 */
	/*@RequestMapping("/oemmanager/logout")
	public String adminLogout(Model model){
		return "/oemmanager/login";
    }*/
	
	/**代理管理员退出页
	 * @param model
	 * @return
	 */
	@RequestMapping("/oemManagerLogin")
	public String adminLoginPage(Model model){
		return "/oemmanager/login";
    }
	@RequestMapping("/")
	public String index(Model model){
		return "/login";
    }
	
	//@RequiresPermissions("1102:3")//权限管理;
	/*@RequestMapping("/main")
	public String main(Model model){
		//确定是哪种类型的用户跳转
		Object obj =  SecurityUtils.getSubject().getPrincipal();
		if(obj instanceof Customer){
			return "/main";
		}
		return "/main";
    }*/
	@RequestMapping("/loginForm")
	@ResponseBody
	public ResultDTO loginForm(String userName, String password, String userType, String verifyCode, HttpServletRequest req,  HttpServletResponse resp){
		System.out.println(req.getServerName());
		logger.info("用户：" + userName + "密码：" + password + "domain:" + req.getRemoteAddr());
		UsernamePasswordUserTypeToken token = new UsernamePasswordUserTypeToken(userName, password, false, WebAddrUtils.getIpAddr(req), WebAddrUtils.getDomain(req), userType, Constant.HAS_PASSWORD);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			Map<String,Object> map = new HashMap<>();
			map.put("userType", userType);
			return ResultDTO.ok(map);
		} catch (AuthenticationException e) {
			return ResultDTO.error("用户密码错误！");
		}
		/*String exception = (String) req.getAttribute("shiroLoginFailure");
	    System.out.println("exception=" + exception);
	    ResultDTO res = null;
	    if (exception != null) {
	        if (UnknownAccountException.class.getName().equals(exception)) {
	            System.out.println("UnknownAccountException -- > 账号不存在：");
	            res = ResultDTO.error("UnknownAccountException -- > 账号不存在：");
	        } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
	            System.out.println("IncorrectCredentialsException -- > 密码不正确：");
	            res = ResultDTO.error("IncorrectCredentialsException -- > 密码不正确：");
	        } else if ("kaptchaValidateFailed".equals(exception)) {
	            System.out.println("kaptchaValidateFailed -- > 验证码错误");
	            res = ResultDTO.error("kaptchaValidateFailed -- > 验证码错误");
	        } else {
	            logger.info("else -- >" + exception);
	        }
	    }
	    // 此方法不处理登录成功,由shiro进行处理
	    return res;*/
    }
}
