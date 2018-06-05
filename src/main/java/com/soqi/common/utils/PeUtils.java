package com.soqi.common.utils;

import at.pollux.thymeleaf.shiro.processor.ShiroFacade;

/**
 * @author 孙傲
 * 功能：权限要解校验工具
 */
public class PeUtils {
	/*public static boolean isExistPrivilege(String priid){
		Object obj =  SecurityUtils.getSubject().getPrincipal();
		if(obj instanceof Customer){
			System.out.println(FastJsonUtil.toJSONString(obj));
		}
		return false;
	}
	public static String getPrivilege(String priid){
		Object obj =  SecurityUtils.getSubject().getPrincipal();
		Set<String> perms = null;
		if(obj instanceof Customer){
			Customer cust = (Customer)obj;
			PrivilegeService ps =  ApplicationContextRegister.getBean(PrivilegeService.class);
			perms = ps.qryPrivsByCustId(cust.getCustomerid());
			System.out.println(FastJsonUtil.toJSONString(perms));
		}
		return FastJsonUtil.toJSONString(perms);
	}
	public static Object getPrivilegeObj(String priid){
		return  SecurityUtils.getSubject().getPrincipal();
	}*/
	
	public static boolean hasPermission(final String permission) {
		return ShiroFacade.hasPermission(permission);
	}
}