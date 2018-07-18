package com.soqi.oem.dao;

import com.soqi.oem.gentry.Apipricechangedetail;

import java.util.List;

public interface ApipricechangedetailMapper {
	
    int insert(Apipricechangedetail record);
    
    int batchInsert(List<Apipricechangedetail> record);

    int batchDelete(Integer id);
    
    List<Apipricechangedetail> selectAll();
    
    List<Apipricechangedetail> selectListById(Integer id);
}