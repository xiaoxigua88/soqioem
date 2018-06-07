package com.soqi.oem.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Customerrole;
import com.soqi.oem.gentry.Privilege;
import com.soqi.oem.gentry.Role;

public interface CustomerroleMapper {
    int deleteByPrimaryKey(@Param("customerid") Integer customerid, @Param("roleid") Integer roleid);

    int deleteByCustomerrole(Customerrole record);
    
    int insert(Customerrole record);

    Customerrole selectByPrimaryKey(@Param("customerid") Integer customerid, @Param("roleid") Integer roleid);

    List<Customerrole> selectAll();
    
    List<Customerrole> selectByCustomerId(Integer customerid);
    
    List<Role> selectRoleListByCustomerId(Integer customerid);

    int updateByPrimaryKey(Customerrole record);
    
    /**获取系统代理和角色的对应关系
     * @param oemid
     * @param isoemmanager
     * @return
     */
    Customerrole selectSingleOemRelation(@Param("oemid") Integer oemid, @Param("isoemmanager") Integer isoemmanager);
    
}