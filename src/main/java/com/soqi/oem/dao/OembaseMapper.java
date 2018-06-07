package com.soqi.oem.dao;

import com.soqi.oem.gentry.Oembase;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OembaseMapper {
    int deleteByPrimaryKey(Integer oemid);

    int insert(Oembase record);

    Oembase selectByPrimaryKey(Integer oemid);

    List<Oembase> selectAll();
    
    List<Oembase> qryChildOemsByOemId(@Param("parentoemid") Integer parentoemid, @Param("start") int start, @Param("size") int size);
    
    int qryCountChildOemsByOemId(@Param("parentoemid") Integer parentoemid);
    
    int updateByPrimaryKey(Oembase record);
}