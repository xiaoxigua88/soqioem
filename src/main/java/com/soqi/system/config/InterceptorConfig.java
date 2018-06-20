package com.soqi.system.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.soqi.common.utils.ShiroUtils;
import com.soqi.common.utils.WebAddrUtils;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemuser;

public class InterceptorConfig implements HandlerInterceptor{
	private static final Logger logger = LoggerFactory.getLogger(InterceptorConfig.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println(">>>MyInterceptor1>>>>>>>在请求处理之前进行调用（Controller方法调用之前）");
		System.out.println(">>>"+request.getRequestURI()+"<<<");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView!=null){
			Map<String,Object> map = new HashMap<>();
			
			//支持客户端多模板
			if(request.getRequestURI().contains("client") || StringUtils.equals(request.getRequestURI(), "/") 
					|| (request.getRequestURI().contains("/login") && !request.getRequestURI().contains("/client"))){
				String templDir = "/clienttemplates/"+WebAddrUtils.getTemplateName(request)+"/";
				String newViewName = "clienttemplates/"+WebAddrUtils.getTemplateName(request) + modelAndView.getViewName();
				//当前模板目录
				map.put("templDir", templDir);
				modelAndView.setViewName(newViewName);
			}
			
			//统一处理传到前端分离的对像
			Map<String,Object> jsonData = (Map)modelAndView.getModel().get("jsonData");
			if(jsonData==null){
				jsonData = new HashMap<String,Object>();
				modelAndView.addObject("jsonData", jsonData);
			}
			Object obj = ShiroUtils.getUser();
			if(obj instanceof Customer){
				Customer ct = (Customer)obj;
				if(StringUtils.isBlank((String)jsonData.get("username"))){
					jsonData.put("username", ct.getCallname());
				}
				jsonData.put("customerid", ct.getCustomerid());
				jsonData.put("oemid", ct.getOemid());
			}else if(obj instanceof Oemuser){
				Oemuser ou = (Oemuser)obj;
				jsonData.put("username", ou.getCallname());
				jsonData.put("userid", ou.getUserid());
				jsonData.put("oemid", ou.getOemid());
			}
			jsonData.put("domain", WebAddrUtils.getDomain(request));
			jsonData.put("httpDomain", WebAddrUtils.getHttpDomain(request));
			modelAndView.addAllObjects(map);
			System.out.println(">>>MyInterceptor1>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println(">>>MyInterceptor1>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
	}
	
}
