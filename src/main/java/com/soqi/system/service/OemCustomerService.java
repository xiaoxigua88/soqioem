package com.soqi.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soqi.oem.dao.CustomerMapper;
import com.soqi.oem.gentry.Customer;

@Service
public class OemCustomerService {

	@Autowired
	private CustomerMapper customerMapper;

	//根据员工ID和所属代理编号获取当前代理系统下的员工信息表
	public List<Customer> qryCustomersByCusIdAndOemid(int customerid, int oemid, int start, int size){
		return customerMapper.qryCustomersByCusIdAndOemid(customerid, oemid, start, size);
	}
	
	public int qryCountByCusIdAndOemid(int customerid, int oemid){
		return customerMapper.qryCountByCusIdAndOemid(customerid, oemid);
	}
	
	public Customer selectByCustomerId(Integer customerid){
		return customerMapper.selectByPrimaryKey(customerid);
	}
}
