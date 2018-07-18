package com.soqi.oem.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.soqi.oem.gentry.Oempricetempl;

public interface OempricetemplMapper {
    int insert(Oempricetempl record);

    List<Oempricetempl> selectAll();
    
    List<Oempricetempl> qryOempricetemplsByOemid(@Param("oemid") Integer oemid, @Param("searchtype") Integer searchtype);
    
    int batchInsert(List<Oempricetempl> records);
    
    int batchUpdate(List<Oempricetempl> records);
    
    int batchDelete(List<Oempricetempl> records);
}