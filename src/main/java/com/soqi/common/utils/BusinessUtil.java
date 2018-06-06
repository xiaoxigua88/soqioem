package com.soqi.common.utils;

import java.util.List;

import com.soqi.oem.gentry.Role;

public class BusinessUtil {

	public static List<Role> checkedForRoles(List<Role> source, List<Role> target){
		for (Role role : target) {
			for(Role sr :source){
				if(sr.getRoleid().intValue() == role.getRoleid().intValue()){
					role.setExist(1);
				}else{
					role.setExist(0);
				}
			}
		}
		return target;
	}
}
