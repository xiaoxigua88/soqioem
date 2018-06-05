package com.soqi.oem.dao;

import com.soqi.oem.gentry.Webtemplate;
import java.util.List;

public interface WebtemplateMapper {
    int deleteByPrimaryKey(Integer tempid);

    int insert(Webtemplate record);

    Webtemplate selectByPrimaryKey(Integer tempid);

    Webtemplate selectByDomain(String domain);
    
    List<Webtemplate> selectAll();

    int updateByPrimaryKey(Webtemplate record);
}