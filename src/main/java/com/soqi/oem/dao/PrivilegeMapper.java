package com.soqi.oem.dao;

import com.soqi.oem.gentry.Privilege;
import java.util.List;

public interface PrivilegeMapper {
    int deleteByPrimaryKey(Integer priid);

    int insert(Privilege record);

    Privilege selectByPrimaryKey(Integer priid);
    
    int updateBatch(List<Privilege> privileges);

    List<Privilege> selectAll();

    int updateByPrimaryKey(Privilege record);
}