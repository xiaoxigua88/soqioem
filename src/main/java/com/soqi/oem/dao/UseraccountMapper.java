package com.soqi.oem.dao;

import com.soqi.oem.gentry.Useraccount;
import java.util.List;

public interface UseraccountMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(Useraccount record);

    Useraccount selectByPrimaryKey(Integer userid);

    List<Useraccount> selectAll();

    int updateByPrimaryKey(Useraccount record);
}