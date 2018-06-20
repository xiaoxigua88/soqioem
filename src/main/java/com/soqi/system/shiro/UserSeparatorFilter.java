package com.soqi.system.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemuser;

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
            if(subject.getPrincipal() instanceof Oemuser){
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
            }
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
