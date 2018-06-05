package com.soqi.system.service.impl;

import org.springframework.stereotype.Component;

import com.soqi.system.service.YbCloudApiService;

@Component
public class YbCloudApiServiceImpl implements YbCloudApiService {

	@Override
	public String apiDo(String action, String data) {
		System.out.println("进入方法体啦");
		return null;
	}

}
