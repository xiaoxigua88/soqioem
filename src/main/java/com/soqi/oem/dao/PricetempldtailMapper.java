package com.soqi.oem.dao;

import com.soqi.oem.gentry.Pricetempldtail;
import java.util.List;

public interface PricetempldtailMapper {
    int deleteByPrimaryKey(Integer pricetemplid);

    int insert(Pricetempldtail record);

    int batchInsert(List<Pricetempldtail> records);
    
    int batchUpdate(List<Pricetempldtail> records);
    
    Pricetempldtail selectByPrimaryKey(Integer pricetemplid);

    List<Pricetempldtail> selectAll();

    List<Pricetempldtail> selectListByOemid(Integer oemid);
    
    int updateByPrimaryKey(Pricetempldtail record);
}