package com.soqi.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soqi.oem.dao.OemserviceconfigMapper;
import com.soqi.oem.gentry.Oemserviceconfig;

@Service
public class OemServiceConfigService {

	@Autowired
	private OemserviceconfigMapper oscMapper;
	
	public List<Oemserviceconfig> getServiceConfigList(Integer oemid){
		return oscMapper.selectListByOemid(oemid);
	}
	
	public int saveServiceConfigList(List<Oemserviceconfig> oscList){
		return oscMapper.batchUpdate(oscList);
	}
}
