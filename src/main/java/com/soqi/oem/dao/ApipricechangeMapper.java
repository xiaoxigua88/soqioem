package com.soqi.oem.dao;

import com.soqi.oem.gentry.Apipricechange;
import java.util.List;

public interface ApipricechangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Apipricechange record);

    Apipricechange selectByPrimaryKey(Integer id);
    
    Apipricechange selectByOemid(Integer oemid);

    List<Apipricechange> selectAll();

    int updateByPrimaryKey(Apipricechange record);
}