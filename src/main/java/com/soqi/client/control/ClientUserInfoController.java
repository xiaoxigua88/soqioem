package com.soqi.client.control;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.subject.WebSubject.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soqi.common.constants.Constant;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemuser;
import com.soqi.system.service.STService;
import com.soqi.system.service.UserService;
import com.soqi.system.shiro.UsernamePasswordUserTypeToken;

@Controller
public class ClientUserInfoController {
	private final Logger logger = LoggerFactory.getLogger(ClientUserInfoController.class);
	@Autowired
	private UserService userService;
	@Autowired
    private STService sf;
	
	@RequestMapping("/client/userinf/default")
	public String userinfo(@RequestParam(value="userid",required=false) Integer userid, @RequestParam(value="action",required=false) String action, HttpServletRequest req, HttpServletResponse res){
		if(userid>0 && StringUtils.equals(action, "LoginUser")){
			/*Oemuser oemuser = userService.qryOemuser(userid);
			UsernamePasswordUserTypeToken token = new UsernamePasswordUserTypeToken(oemuser.getMobile(), oemuser.getPwd(), false, null, null, Constant.USERTYPE_USER, Constant.NO_PASSWORD);
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);*/
			//return oemuser == null ? "/userinfo/default" : "/login"; 
			/*PrincipalCollection principals = new SimplePrincipalCollection(userid, "MobileRealm");
			Builder builder = new WebSubject.Builder(req, res);
			builder.principals(principals);
			builder.authenticated(true);
			WebSubject subject = builder.buildWebSubject();
			ThreadContext.bind(subject);*/
			
		}
		/*PrincipalCollection principals = new SimplePrincipalCollection(  
				                user.getId(), "MobileRealm");  
				Builder builder = new WebSubject.Builder(  
				                ServletActionContext.getRequest(),  
				                ServletActionContext.getResponse());  
				builder.principals(principals);  
				builder.authenticated(true);  
				WebSubject subject = builder.buildWebSubject();  
				ThreadContext.bind(subject);  */
		return "/userinfo/default";
    }
	
	@RequestMapping("/client/userinf/address")
	public String address(Customer customer){
		return "/userinfo/address";
    }
	
	@RequestMapping("/client/userinf/email")
	public String email(Customer customer){
		return "/userinfo/email";
    }
	
	@RequestMapping("/client/userinf/emailbind")
	public String emailbind(Customer customer){
		return "/userinfo/emailbind";
	}
	
	@RequestMapping("/client/userinf/emailset")
	public String emailset(Customer customer){
		return "/userinfo/emailset";
	}
	
	@RequestMapping("/client/userinf/mobile")
	public String mobile(Customer customer){
		return "/userinfo/mobile";
	}
	
	@RequestMapping("/client/userinf/mobileset")
	public String mobileset(Customer customer){
		return "/userinfo/mobileset";
	}
	
	@RequestMapping("/client/userinf/password")
	public String password(Customer customer){
		return "/userinfo/password";
	}
	
	@RequestMapping("/client/userinf/protect")
	public String protect(Customer customer){
		return "/userinfo/protect";
	}
	
	@RequestMapping("/client/userinf/rights")
	public String rights(Customer customer){
		return "/userinfo/rights";
	}
	
	@RequestMapping("/client/userinf/safety")
	public String safety(Customer customer){
		return "/userinfo/safety";
	}
	

	@RequestMapping("/client/userinf/verify")
	public String verify(Customer customer){
		return "/userinfo/verify";
    }
}
