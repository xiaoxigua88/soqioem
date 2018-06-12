package com.soqi.oem.dao;

import com.soqi.oem.gentry.Userrecharge;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserrechargeMapper {
    int deleteByPrimaryKey(Long orderid);

    int insert(Userrecharge record);

    Userrecharge selectByPrimaryKey(Long orderid);

    List<Userrecharge> selectAll();

    int updateByPrimaryKey(Userrecharge record);
    
    public List<Userrecharge> qryUserRechargesByOemid(@Param("oemid") Integer oemid, @Param("start") int start, @Param("size") int size);
    
    public int qryCountUserRechargesByOemid(Integer oemid);
}