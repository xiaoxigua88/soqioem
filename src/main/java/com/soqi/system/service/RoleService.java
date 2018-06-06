package com.soqi.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soqi.common.utils.BusinessUtil;
import com.soqi.oem.dao.CustomerroleMapper;
import com.soqi.oem.dao.RoleMapper;
import com.soqi.oem.gentry.Role;

@Service
public class RoleService {
	@Autowired
	private RoleMapper rm;
	@Autowired
	private CustomerroleMapper customerroleMapper;
	
	public List<Role> qryRolesByOemid(Integer oemid){
		return rm.selectRolseByOemid(oemid);
	}
	
	public Role qryRoleByRoleidAndOemid(Integer roleid,Integer oemid){
		return rm.selectByPrimaryKey(roleid, oemid);
	}
	
	public List<Role> selectCheckedRoleList(Integer customerId,Integer oemid){
		//根据员工Id获取员工角色
		List<Role> custRoles = customerroleMapper.selectRoleListByCustomerId(customerId);
		List<Role> oemRoles = rm.selectRolseByOemid(oemid);
		return BusinessUtil.checkedForRoles(custRoles, oemRoles);
	}
}
