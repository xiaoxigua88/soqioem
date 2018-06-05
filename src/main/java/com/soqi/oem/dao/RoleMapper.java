package com.soqi.oem.dao;

import com.soqi.oem.gentry.Role;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer roleid);

    int insert(Role record);

    Role selectByPrimaryKey(@Param("roleid") Integer roleid, @Param("oemid") Integer oemid);

    List<Role> selectAll();
    
    List<Role> selectRolseByOemid(Integer oemid);
    
    int updateByPrimaryKey(Role record);
   
}