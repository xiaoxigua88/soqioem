package com.soqi.system.control;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
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
import org.springframework.web.servlet.ModelAndView;

import com.soqi.common.constants.Constant;
import com.soqi.common.utils.CookieUtils;
import com.soqi.common.utils.EncrypDES;
import com.soqi.common.utils.ResultDTO;
import com.soqi.common.utils.WebAddrUtils;
import com.soqi.system.service.IImageCodeService;
import com.soqi.system.service.OemCountService;
import com.soqi.system.service.UserService;
import com.soqi.system.shiro.UsernamePasswordUserTypeToken;


@Controller
public class LoginController {
	private final Logger logger = LoggerFactory.getLogger(LoginController.class);
	/*@Autowired
    private Producer captchaProducer;*/
	@Autowired
	private IImageCodeService verifyCodeService;
	@Autowired
	private UserService userService;
	@Autowired
    private OemCountService sf;
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
	public ResultDTO loginForm(String userName, String password, String userType, String verifycode, HttpServletRequest req,  HttpServletResponse resp){
		String captchaCode = CookieUtils.getCookie("captchaCode");
		if(!verifyCodeService.checkVerifycode(verifycode, captchaCode)){
			return ResultDTO.error("证码输入错误！");
		}
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
	
	/*@RequestMapping(value = "/verifyimg")
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {  
        response.setDateHeader("Expires", 0);  
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");  
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");  
        response.setHeader("Pragma", "no-cache");  
        response.setContentType("image/jpeg");
        String capText = captchaProducer.createText(); 
        capText = EncrypDES.encryption(capText, EncrypDES.DES_SECRETKEY);
        CookieUtils.addCookie("captchaCode", capText);
        BufferedImage bi = captchaProducer.createImage(capText);  
        ServletOutputStream out = response.getOutputStream();  
        ImageIO.write(bi, "jpg", out);  
        try {  
            out.flush();  
        } finally {  
            out.close();  
        }  
        return null;  
    }  */
	
	@RequestMapping(value = "/verifyimg")
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setDateHeader("Expires", 0);  
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");  
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");  
        response.setHeader("Pragma", "no-cache");  
        response.setContentType("image/jpeg");
        // 生成随机验证码内容
        String verifyCode= verifyCodeService.getRandString();
        String vcEncryp = EncrypDES.encryption(verifyCode, EncrypDES.DES_SECRETKEY);
        CookieUtils.addCookie("captchaCode", vcEncryp);
        BufferedImage bi = verifyCodeService.createVerifyCodeImage(verifyCode); 
        ServletOutputStream out = response.getOutputStream();  
        ImageIO.write(bi, "jpg", out);  
        try {  
            out.flush();  
        } finally {  
            out.close();  
        }  
        return null;
	}
}
