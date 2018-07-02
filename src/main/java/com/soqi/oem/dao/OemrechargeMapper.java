package com.soqi.oem.dao;

import com.soqi.oem.gentry.Oemrecharge;
import com.soqi.oem.gentry.Userrecharge;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OemrechargeMapper {
    int deleteByPrimaryKey(Long orderid);

    int insert(Oemrecharge record);

    Oemrecharge selectByPrimaryKey(Long orderid);

    List<Oemrecharge> selectAll();

    int updateByPrimaryKey(Oemrecharge record);
    
    public List<Oemrecharge> qryOemRechargesByOemid(@Param("oemid") Integer oemid, @Param("start") int start, @Param("size") int size);
    
    public int qryCountOemRechargesByOemid(Integer oemId);
}