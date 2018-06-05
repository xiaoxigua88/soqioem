package com.soqi.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soqi.oem.dao.CustomerMapper;
import com.soqi.oem.dao.OemuserMapper;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemuser;

@Service
public class AuthenticateService {
	@Autowired
	private CustomerMapper cm;
	@Autowired
	private OemuserMapper om;
	
	public Customer qryCustomerByDomainAndMobile(String domain, String mobile){
		return cm.selectByDomainAndMobile(domain, mobile);
	}
	public Oemuser qryOemuserByDomainAndMobile(String domain, String mobile){
		return om.selectByDomainAndMobile(domain, mobile);
	}
	
	public Oemuser selectByUserid(Integer userid){
		return om.selectByPrimaryKey(userid);
	}
}
