package com.soqi.client.control;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.subject.WebSubject.Builder;
import org.apache.shiro.web.util.WebUtils;
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
import com.soqi.common.utils.MD5Utils;
import com.soqi.common.utils.ResultFontJS;
import com.soqi.common.utils.ShiroUtils;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemuser;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.OemCountService;
import com.soqi.system.service.UserService;

@Controller
public class ClientUserInfoController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(ClientUserInfoController.class);
	@Autowired
	private UserService userService;
	@Autowired
    private OemCountService sf;
	
	@RequestMapping("/client/userinf/default")
	public String userinfo(Model model, @RequestParam(value="userid",required=false) Integer userid, @RequestParam(value="action",required=false) String action, HttpServletRequest req, HttpServletResponse res){
		//if(userid>0 && StringUtils.equals(action, "LoginUser")){
			//Oemuser oemuser = userService.qryOemuser(userid);
			/*UsernamePasswordUserTypeToken token = new UsernamePasswordUserTypeToken(oemuser.getMobile(), oemuser.getPwd(), false, null, null, Constant.USERTYPE_USER, Constant.NO_PASSWORD);
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);*/
			//return oemuser == null ? "/userinfo/default" : "/login"; 
			
			/*PrincipalCollection principals = new SimplePrincipalCollection(oemuser, "MobileRealm");
			Builder builder = new WebSubject.Builder(req, res);
			builder.principals(principals);
			builder.authenticated(true);
			WebSubject subject = builder.buildWebSubject();
			ThreadContext.bind(subject);
			WebUtils.saveRequest(req);*/
		//}
		//添加cookie
		String oem_user = CookieUtils.getCookie("oem_user");
		if(StringUtils.isNotBlank(oem_user)){
			CookieUtils.removeCookie(oem_user);
		}
		CookieUtils.addCookie("oem_user", this.getOemuser().getOemid() + "_" + this.getOemuser().getUserid());
		Map<String,Object> jsonData = new HashMap<String,Object>();
		jsonData.put("user", this.getOemuser());
		model.addAttribute("jsonData",jsonData);
		/*PrincipalCollection principals = new SimplePrincipalCollection(  
				                user.getId(), "MobileRealm");  
				Builder builder = new WebSubject.Builder(  
				                ServletActionContext.getRequest(),  
				                ServletActionContext.getResponse());  
				builder.principals(principals);  
				builder.authenticated(true);  
				WebSubject subject = builder.buildWebSubject();  
				ThreadContext.bind(subject);  */
		//Oemuser oemuser = userService.qryOemuser(userid);
		
		return "/userinfo/default";
    }
	
	/**功能：用户资料信息修改
	 * @param customer
	 * @param verifyCode
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping("/client/userinf/updateinfo")
	@ResponseBody
	public ResultFontJS updateInfo(Oemuser user){
		if(user == null){
			return ResultFontJS.error("传参失败、请检查参数");
		}
		if(user.getUserid().intValue() != this.getOemuser().getUserid().intValue()){
			return ResultFontJS.error("非法的数据修改");
		}
		
		
		int count = userService.updateOemuser(user);
		Oemuser newuser = userService.qryOemuser(user.getUserid());
		//一种是通过重新更新Principal达到更新内存中用户信息的目的
		ShiroUtils.updatePrincipal(newuser);
		//另一种通过对像属性拷贝的方式达到更新内存中用户信息的目的
		//BeanUtils.copyProperties(newcst, this.getCustomer());
		return count > 0 ? ResultFontJS.ok("数据修改成功") : ResultFontJS.error("数据更新失败");
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
