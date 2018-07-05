package com.soqi.oem.dao;

import com.soqi.oem.gentry.Serviceconfig;
import java.util.List;

public interface ServiceconfigMapper {
    int deleteByPrimaryKey(Integer serviceid);

    int insert(Serviceconfig record);

    Serviceconfig selectByPrimaryKey(Integer serviceid);

    List<Serviceconfig> selectAll();

    int updateByPrimaryKey(Serviceconfig record);
}