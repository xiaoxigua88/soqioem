package com.soqi.system.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.common.utils.MD5Utils;
import com.soqi.common.utils.RandomUtil;
import com.soqi.oem.dao.CustomerMapper;
import com.soqi.oem.dao.CustomerroleMapper;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Customerrole;

@Service
public class OemCustomerService {

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private CustomerroleMapper customerroleMapper;
	
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
	
	/**功能：修改员工信息
	 * @param customer
	 * @param roleId
	 * @return
	 */
	@Transactional("primaryTransactionManager")
	public void updateCustomerInfo(Customer customer, Integer[] roleIds){
		customerMapper.updateByPrimaryKey(customer);
		//由于传入角色是数组、数据可以考虑先删除原来角色再新增角色
		List<Customerrole> crs = customerroleMapper.selectByCustomerId(customer.getCustomerid());
		for (Customerrole customerrole : crs) {
			customerroleMapper.deleteByCustomerrole(customerrole);
		}
		for (Integer roleid : roleIds) {
			Customerrole record = new Customerrole(customer.getCustomerid(),roleid);
			customerroleMapper.insert(record);
		}
	}
	/**功能：新增员工信息
	 * @param customer
	 * @param roleId
	 * @return
	 */
	@Transactional("primaryTransactionManager")
	public void saveCustomerInfo(Customer customer, Integer[] roleIds){
		customerMapper.insert(customer);
		//数据插入后会返回一个自增ID到customer的customerid上
		for (Integer roleid : roleIds) {
			Customerrole record = new Customerrole(customer.getCustomerid(),roleid);
			customerroleMapper.insert(record);
		}
	}
	
	/**密码重置
	 * @param customer
	 * @return
	 */
	public String InitPwd(Customer customer){
		int num = RandomUtil.getNotSimple(RandomUtil.SEEDARR, 6);
		String pwd = MD5Utils.encrypt(customer.getMobile(),"SQ." + num);
		Customer modCus = new Customer();
		modCus.setPwd(pwd);
		modCus.setCustomerid(customer.getCustomerid());
		int count = customerMapper.updateByPrimaryKey(modCus);
		return count >0 ? ("SQ." + num) : null;
	}
}
