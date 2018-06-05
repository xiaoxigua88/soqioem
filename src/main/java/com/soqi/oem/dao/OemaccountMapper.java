package com.soqi.oem.dao;

import com.soqi.oem.gentry.Oemaccount;
import java.util.List;

public interface OemaccountMapper {
    int deleteByPrimaryKey(Integer oemid);

    int insert(Oemaccount record);

    Oemaccount selectByPrimaryKey(Integer oemid);

    List<Oemaccount> selectAll();

    int updateByPrimaryKey(Oemaccount record);
}