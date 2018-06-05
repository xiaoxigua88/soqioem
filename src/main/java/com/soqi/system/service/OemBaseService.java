package com.soqi.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soqi.oem.dao.WebtemplateMapper;
import com.soqi.oem.gentry.Webtemplate;
@Service
public class OemBaseService {
	@Autowired
	private WebtemplateMapper web;
	
	public Webtemplate selectTemplateByDomain(String domain){
		return web.selectByDomain(domain);
	}
}
