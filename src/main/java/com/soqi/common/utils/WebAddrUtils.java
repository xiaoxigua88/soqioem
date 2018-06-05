package com.soqi.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soqi.oem.gentry.Webtemplate;
import com.soqi.system.config.ApplicationContextRegister;
import com.soqi.system.service.OemBaseService;

import javax.servlet.http.HttpServletRequest;

public class WebAddrUtils {
	private static Logger logger = LoggerFactory.getLogger(WebAddrUtils.class);

	/**
	 * 获取IP地址
	 * 
	 * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}
	
	/**获取url域名部分如www.baidu.com
	 * @param request
	 * @return
	 */
	public static String getDomain(HttpServletRequest request){
		return getServerName(request);
	}
	
	/**获取url域名部分如www.baidu.com
	 * @param request
	 * @return
	 */
	public static String getHttpDomain(HttpServletRequest request){
		return "http://" + getServerName(request) + "/";
	}
	
	private static String getServerName(HttpServletRequest request){
		return request.getServerName();
	}
	
	public static String getTemplateName(HttpServletRequest request){
		OemBaseService obs = ApplicationContextRegister.getBean(OemBaseService.class);
		Webtemplate wt = obs.selectTemplateByDomain(getServerName(request));
		return wt.getTempdir();
	}
}
