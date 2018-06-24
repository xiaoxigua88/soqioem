package com.soqi.system.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.soqi.common.utils.CookieUtils;
import com.soqi.common.utils.ShiroUtils;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemuser;
import com.soqi.system.config.ApplicationContextRegister;
import com.soqi.system.service.AuthenticateService;
import com.soqi.system.service.UserService;

public class UserSeparatorFilter extends AccessControlFilter{

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		if (isLoginRequest(request, response)) {
            return true;
        } else {
        	String uri = WebUtils.getRequestUri((HttpServletRequest) request);
        	//可能是无密登录的url需要做区分校验
        	String userid = request.getParameter("userid");
        	String action = request.getParameter("action");
            Subject subject = getSubject(request, response);
            PrincipalCollection pc = subject.getPrincipals();
            if(subject == null){
            	return false;
            }
            //如果系统当前用户是代理操作跳到客户端无密登录
            if(subject.getPrincipal() instanceof Customer){
            	Customer customer = (Customer)subject.getPrincipal();
            	if(StringUtils.contains(uri, "client")){
            		 UserService us = ApplicationContextRegister.getBean(UserService.class);
            		 Oemuser user = null;
            		if(StringUtils.isNotBlank(action) && StringUtils.isNotBlank(userid)){
            			user = us.selectOemuserByUseridAndOemid(customer.getOemid(), Integer.valueOf(userid));
            		}else{
            			//可能无密登录后、用户过段时间再操作此客户页面、保证用户正常操作
            			String oem_user = CookieUtils.getCookie("oem_user");
            			String[] oemid_userid = oem_user.split("_");
            			user = us.selectOemuserByUseridAndOemid(Integer.valueOf(oemid_userid[0]), Integer.valueOf(oemid_userid[1]));
            		}
            		if(user == null){
        				return false;
        			}else{
        				//无密登录替换成客户端session
        				ShiroUtils.replacePrincipal(user, request, response);
        			}
            	}
            }
            if(subject.getPrincipal() instanceof Oemuser){
            	Oemuser oemuser = (Oemuser)subject.getPrincipal();
            	String oem_manager = CookieUtils.getCookie("oem_manager");
            	if(StringUtils.isNotBlank(oem_manager)){
            		if(StringUtils.contains(uri, "oemmanager")){
	            		String[] domain_mobile = oem_manager.split("_");
	            		if(StringUtils.isNotBlank(domain_mobile[0]) && StringUtils.isNotBlank(domain_mobile[1])){
	            			AuthenticateService as = ApplicationContextRegister.getBean(AuthenticateService.class);
	                		Customer customer = as.qryCustomerByDomainAndMobile(domain_mobile[0], domain_mobile[1]);
	                		if(customer == null){
	                			return false;
	                		}else{
	                			//代理无密登录查看用户后、再回头去操作代理页需要切换成代理的subject
	                			ShiroUtils.replacePrincipal(customer, request, response);
	                		}
	            		}else{
	            			return false;
	            		}
            		}else if(StringUtils.contains(uri, "client")){
            			//如果代理访问一个客户后该客户没关，客户又去打开另一个客户，则系统当前的subject要换成另一个客户的，这样就形成了客户到客户的切换
            			UserService us = ApplicationContextRegister.getBean(UserService.class);
                		if(StringUtils.isNotBlank(action) && StringUtils.isNotBlank(userid)){
                			if(Integer.valueOf(userid) == oemuser.getUserid().intValue()){
                				return true;
                			}
                			Oemuser user = us.selectOemuserByUseridAndOemid(oemuser.getOemid(), Integer.valueOf(userid));
                			if(user == null){
                				return false;
                			}else{
                				//无密登录替换成客户端session
                				ShiroUtils.replacePrincipal(user, request, response);
                			}
                		}
            		}
            	}
            }
            /*if(subject.getPrincipal() instanceof Oemuser){
            	Oemuser user = (Oemuser)subject.getPrincipal();
            	if(StringUtils.contains(uri, "oemmanager")){
            		//同一浏览器下、代理端登录后、然后再客户端登录、再回去刷新代理页面、session是客户端的内容，程序不加校验导致报错
            		return false;
            	}
            	//如果是无密登录部分需要校验住
            	if(StringUtils.equals(action, "LoginUser") && StringUtils.isNotBlank(userid) && !StringUtils.equals(userid, user.getUserid().toString())){
            		return false;
            	}
            }
            if(subject.getPrincipal() instanceof Customer){
            	if(StringUtils.contains(uri, "client")){
            		//同一浏览器下、客户端登录后、然后再代理端登录、再回去刷新客户端页面、session是代理端的内容，程序不加校验导致报错
            		//TODO 无密用户必须是在该customer的oemid下面
            		//添加无密登录部分校验
            		if(StringUtils.isBlank(action) && StringUtils.isBlank(userid)){
            			return false;
            		}
            	}
            }*/
            return true;
        }
		
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		String uri = WebUtils.getRequestUri((HttpServletRequest) request);
		if(StringUtils.contains(uri, "oemmanager")){
			this.setLoginUrl("/oemManagerLogin");
		}
		saveRequestAndRedirectToLogin(request, response);
        return false;
	}

}
