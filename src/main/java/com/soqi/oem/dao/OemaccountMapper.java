package com.soqi.oem.dao;

import com.soqi.oem.gentry.Oemaccount;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OemaccountMapper {
    int deleteByPrimaryKey(Integer oemid);

    int insert(Oemaccount record);

    Oemaccount selectByPrimaryKey(Integer oemid);

    List<Oemaccount> selectAll();

    int updateByPrimaryKey(Oemaccount record);
    
    int updateFreezeAmountByOemId(@Param("oemid") Integer oemid, @Param("freezeamount") BigDecimal freezeamount);
}