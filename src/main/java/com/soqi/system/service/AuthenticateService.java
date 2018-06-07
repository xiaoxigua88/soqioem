package com.soqi.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soqi.oem.dao.CustomerMapper;
import com.soqi.oem.dao.OembaseMapper;
import com.soqi.oem.dao.OemuserMapper;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oembase;
import com.soqi.oem.gentry.Oemuser;

@Service
public class AuthenticateService {
	@Autowired
	private CustomerMapper cm;
	@Autowired
	private OemuserMapper om;
	@Autowired
	private OembaseMapper ob;
	
	public Customer qryCustomerByDomainAndMobile(String domain, String mobile){
		//查询当前登录人员信息
		Customer customer = cm.selectByDomainAndMobile(domain, mobile);
		//查询当前人员所属代是信息
		Oembase oembase = ob.selectByPrimaryKey(customer.getOemid());
		customer.setOembase(oembase);
		return customer;
	}
	public Oemuser qryOemuserByDomainAndMobile(String domain, String mobile){
		return om.selectByDomainAndMobile(domain, mobile);
	}
	
	public Oemuser selectByUserid(Integer userid){
		return om.selectByPrimaryKey(userid);
	}
}
