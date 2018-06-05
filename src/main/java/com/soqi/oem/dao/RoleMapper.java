package com.soqi.oem.dao;

import com.soqi.oem.gentry.Role;
import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer roleid);

    int insert(Role record);

    Role selectByPrimaryKey(Integer roleid);

    List<Role> selectAll();
    
    List<Role> selectRolseByOemid(Integer oemid);
    
    int updateByPrimaryKey(Role record);
   
}