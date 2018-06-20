package com.soqi.common.utils;

import java.util.regex.Pattern;

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
	
	/**获取模板地址
	 * @param request
	 * @return
	 */
	public static String getTemplateName(HttpServletRequest request){
		OemBaseService obs = ApplicationContextRegister.getBean(OemBaseService.class);
		Webtemplate wt = obs.selectTemplateByDomain(getServerName(request));
		return wt.getTempdir();
	}
	
	/** 
     * 获取操作系统,浏览器及浏览器版本信息 
     * @param request 
     * @return 
     */  
	public static String[] getOsAndBrowserInfo(HttpServletRequest request) {
		String browserDetails = request.getHeader("User-Agent");
		String userAgent = browserDetails;
		String user = userAgent.toLowerCase();

		String os = "";
		String browser = "";

		// =================OS Info=======================
		if (userAgent.toLowerCase().indexOf("windows") >= 0) {
			os = "Windows";
		} else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
			os = "Mac";
		} else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
			os = "Unix";
		} else if (userAgent.toLowerCase().indexOf("android") >= 0) {
			os = "Android";
		} else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
			os = "IPhone";
		} else {
			os = "UnKnown, More-Info: " + userAgent;
		}
		// ===============Browser===========================
		if (user.contains("edge")) {
			browser = (userAgent.substring(userAgent.indexOf("Edge"))
					.split(" ")[0]).replace("/", "-");
		} else if (user.contains("msie")) {
			String substring = userAgent.substring(userAgent.indexOf("MSIE"))
					.split(";")[0];
			browser = substring.split(" ")[0].replace("MSIE", "IE") + "-"
					+ substring.split(" ")[1];
		} else if (user.contains("safari") && user.contains("version")) {
			browser = (userAgent.substring(userAgent.indexOf("Safari")).split(
					" ")[0]).split("/")[0]
					+ "-"
					+ (userAgent.substring(userAgent.indexOf("Version")).split(
							" ")[0]).split("/")[1];
		} else if (user.contains("opr") || user.contains("opera")) {
			if (user.contains("opera")) {
				browser = (userAgent.substring(userAgent.indexOf("Opera"))
						.split(" ")[0]).split("/")[0]
						+ "-"
						+ (userAgent.substring(userAgent.indexOf("Version"))
								.split(" ")[0]).split("/")[1];
			} else if (user.contains("opr")) {
				browser = ((userAgent.substring(userAgent.indexOf("OPR"))
						.split(" ")[0]).replace("/", "-")).replace("OPR",
						"Opera");
			}

		} else if (user.contains("chrome")) {
			browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(
					" ")[0]).replace("/", "-");
		} else if ((user.indexOf("mozilla/7.0") > -1)
				|| (user.indexOf("netscape6") != -1)
				|| (user.indexOf("mozilla/4.7") != -1)
				|| (user.indexOf("mozilla/4.78") != -1)
				|| (user.indexOf("mozilla/4.08") != -1)
				|| (user.indexOf("mozilla/3") != -1)) {
			browser = "Netscape-?";

		} else if (user.contains("firefox")) {
			browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(
					" ")[0]).replace("/", "-");
		} else if (user.contains("rv")) {
			String IEVersion = (userAgent.substring(userAgent.indexOf("rv"))
					.split(" ")[0]).replace("rv:", "-");
			browser = "IE" + IEVersion.substring(0, IEVersion.length() - 1);
		} else {
			browser = "UnKnown, More-Info: " + userAgent;
		}
		String [] arr = {os, browser};
		return arr; 
	}
	
	// 将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理    
    public static long ipToLong(String strIp) {    
        long[] ip = new long[4];    
        // 先找到IP地址字符串中.的位置    
        int position1 = strIp.indexOf(".");    
        int position2 = strIp.indexOf(".", position1 + 1);    
        int position3 = strIp.indexOf(".", position2 + 1);    
        // 将每个.之间的字符串转换成整型    
        ip[0] = Long.parseLong(strIp.substring(0, position1));    
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));    
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));    
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));    
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];    
    }    
    
    // 将十进制整数形式转换成127.0.0.1形式的ip地址    
    public static String longToIP(long longIp) {    
        StringBuffer sb = new StringBuffer("");    
        // 直接右移24位    
        sb.append(String.valueOf((longIp >>> 24)));    
        sb.append(".");    
        // 将高8位置0，然后右移16位    
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));    
        sb.append(".");    
        // 将高16位置0，然后右移8位    
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));    
        sb.append(".");    
        // 将高24位置0    
        sb.append(String.valueOf((longIp & 0x000000FF)));    
        return sb.toString();    
    }
    
    public static boolean isUrlRight(String url){
    	Pattern pattern = Pattern.compile("");
    	return pattern.matcher(url).matches();
    }
    
    public static void main(String args[]){
    	if(isUrlRight("htt/www.baidu.com?")){
    		System.out.println("是正确的网址");
    	}else{
    		System.out.println("是错误的网址");
    	}
    }
}
