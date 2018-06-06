package com.soqi.system.service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.oem.dao.CustomerroleMapper;
import com.soqi.oem.dao.PrivilegeMapper;
import com.soqi.oem.dao.RoleMapper;
import com.soqi.oem.dao.RoleprivilegeMapper;
import com.soqi.oem.gentry.Privilege;
import com.soqi.oem.gentry.Role;
import com.soqi.oem.gentry.Roleprivilege;

@Service
public class PrivilegeService {
	@Autowired
	private CustomerroleMapper customerroleMapper;
	@Autowired
	private RoleprivilegeMapper roleprivilegeMapper;
	@Autowired
	private PrivilegeMapper privilegeMapper;
	@Autowired
	private RoleMapper rm;
	
	public Set<String> qryPermisByCustId(Integer customerId){
		//根据员工ID获取员工权限
		List<Privilege> privileges = roleprivilegeMapper.selectPrivsByCustId(customerId);
		Set<String> permission = new HashSet<>();;
		for (Privilege privilege : privileges) {
			permission.add(privilege.getPriid()+":"+privilege.getValueconfig());
		}
		return permission;
	}
	
	public List<Privilege> qryPrivsByCustId(){
		return roleprivilegeMapper.selectAll();
	}
	
	public List<Privilege> getPrivsCompared(Integer customerId){
		List<Roleprivilege> rps = roleprivilegeMapper.selectRolePrisByCustId(customerId);
		List<Privilege> privs = qryPrivsByCustId();
		for (Privilege priv : privs) {
			for(Roleprivilege rp : rps){
				if(priv.getPriid().intValue()==rp.getPriid().intValue()){
					if(rp.getValue()!=null){
						priv.setValue(rp.getValue());
					}
				}
			}
			if(priv.getValue()==null){
				priv.setValue(0);
			}
		}
		return privs;
	}
	
	public List<Privilege> getRolePrivsCompared(Integer roleid){
		List<Roleprivilege> rps = roleprivilegeMapper.selectPrivsByRoleID(roleid);
		List<Privilege> privs = qryPrivsByCustId();
		for (Privilege priv : privs) {
			for(Roleprivilege rp : rps){
				if(priv.getPriid().intValue()==rp.getPriid().intValue()){
					if(rp.getValue()!=null){
						priv.setValue(rp.getValue());
					}
				}
			}
			if(priv.getValue()==null){
				priv.setValue(0);
			}
		}
		return privs;
	}
	
	@Transactional("primaryTransactionManager")
	public int PrivilegeBatchUpdate(List<Privilege> privileges){
		return privilegeMapper.updateBatch(privileges);
	}
	
	public Set<String> qryRoleByCustomerId(Integer customerId){
		//根据员工Id获取员工角色
		List<Role> roles = customerroleMapper.selectRoleListByCustomerId(customerId);
		Set<String> setRoles = new HashSet<>();
		for (Role role : roles) {
			setRoles.add(role.getRolename());
		}
		return setRoles;
	}
	
	//更新角色
	@Transactional("primaryTransactionManager")
	public boolean updateRoleAndRolePrivilege(Role role){
		int single = rm.updateByPrimaryKey(role);
		int batch = roleprivilegeMapper.updateBatch(role);
		return single>0 && batch>0 ? true:false;
	}
	
	//添加角色
	@Transactional("primaryTransactionManager")
	public boolean saveRoleAndRolePrivilege(Role role){
		//根据角色编号和oem查询是否有重复的角色创建
		Role hasRole = rm.selectByPrimaryKey(role.getRoleid(), role.getOemid());
		if(hasRole != null){
			return false;
		}
		//添加角色
		int single = rm.insert(role);
		//添加角色对应的权限
		int batch = roleprivilegeMapper.insertBatch(role);
		return single>0 && batch>0 ? true:false;
	}
}