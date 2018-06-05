package com.soqi.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soqi.oem.dao.RoleMapper;
import com.soqi.oem.gentry.Role;

@Service
public class RoleService {
	@Autowired
	private RoleMapper rm;
	
	public List<Role> qryRolesByOemid(Integer oemid){
		return rm.selectRolseByOemid(oemid);
	}
	
	public Role qryRoleByRoleidAndOemid(Integer roleid,Integer oemid){
		return rm.selectByPrimaryKey(roleid, oemid);
	}
}
