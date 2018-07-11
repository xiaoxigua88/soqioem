package com.soqi.oem.dao;

import com.soqi.oem.gentry.Pricetempldtail;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface PricetempldtailMapper {
    int deleteByPrimaryKey(Integer pricetemplid);

    int insert(Pricetempldtail record);

    int batchInsert(List<Pricetempldtail> records);
    
    int batchUpdate(List<Pricetempldtail> records);
    
    Pricetempldtail selectByPrimaryKey(Integer pricetemplid);

    List<Pricetempldtail> selectAll();

    List<Pricetempldtail> selectListByConditions(@Param("oemid") Integer oemid, @Param("templtype") Byte templtype, @Param("searchtype") Integer searchtype);
    
    int updateByPrimaryKey(Pricetempldtail record);
}