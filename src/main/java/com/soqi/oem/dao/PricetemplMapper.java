package com.soqi.oem.dao;

import java.util.List;

import com.soqi.oem.gentry.Pricetempl;

public interface PricetemplMapper {
    int deleteByPrimaryKey(Integer pricetemplid);

    int insert(Pricetempl record);

    Pricetempl selectByPrimaryKey(Integer pricetemplid);

    List<Pricetempl> selectAll();
    
    int updateByPrimaryKey(Pricetempl record);
}