package com.soqi.oem.dao;

import com.soqi.oem.gentry.Userpricetempl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserpricetemplMapper {
    int insert(Userpricetempl record);

    List<Userpricetempl> selectAll();
    
    List<Userpricetempl> qryUserpricetemplsByUserid(@Param("userid") Integer userid, @Param("searchtype") Integer searchtype);
    
    int batchInsert(List<Userpricetempl> records);
    
    int batchUpdate(List<Userpricetempl> records);
    
    int batchDelete(List<Userpricetempl> records);
}