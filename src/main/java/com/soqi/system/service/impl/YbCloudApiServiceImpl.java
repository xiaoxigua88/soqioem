package com.soqi.system.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.soqi.system.service.UserCountService;

@Component
public class YbCloudApiServiceImpl implements UserCountService {

	@Override
	public String apiDo(String action, String data) {
		System.out.println("进入方法体啦");
		return null;
	}

	public static void main(String args[]){
		String a = " \r\n  b\r\n  \r\n    \n\r";
		a = a.trim();
		System.out.println(StringUtils.isBlank(a));
		System.out.println(a.split("\r\n").length);
	}
}
