package com.soqi.oem.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.soqi.oem.gentry.Customerrole;
import com.soqi.oem.gentry.Privilege;
import com.soqi.oem.gentry.Role;

public interface CustomerroleMapper {
    int deleteByPrimaryKey(@Param("customerid") Integer customerid, @Param("roleid") Integer roleid);

    int insert(Customerrole record);

    Customerrole selectByPrimaryKey(@Param("customerid") Integer customerid, @Param("roleid") Integer roleid);

    List<Customerrole> selectAll();
    
    List<Role> selectRoleListByCustomerId(Integer customerid);

    int updateByPrimaryKey(Customerrole record);
}