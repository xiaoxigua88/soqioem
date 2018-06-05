package com.soqi.oem.dao;

import com.soqi.oem.gentry.Privilege;
import com.soqi.oem.gentry.Roleprivilege;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface RoleprivilegeMapper {
    int deleteByPrimaryKey(@Param("roleid") Integer roleid, @Param("priid") Integer priid);

    int insert(Roleprivilege record);

    Roleprivilege selectByPrimaryKey(@Param("roleid") Integer roleid, @Param("priid") Integer priid);

    List<Privilege> selectPrivsByCustId(Integer customerid);
    
    List<Roleprivilege> selectPrivsByRoleID(Integer roleid);
    
    List<Roleprivilege> selectRolePrisByCustId(Integer customerid);
    
    List<Privilege> selectAll();

    int updateByPrimaryKey(Roleprivilege record);
}