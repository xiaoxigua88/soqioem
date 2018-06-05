package com.soqi.oem.dao;

import com.soqi.oem.gentry.Oembase;
import java.util.List;

public interface OembaseMapper {
    int deleteByPrimaryKey(Integer oemid);

    int insert(Oembase record);

    Oembase selectByPrimaryKey(Integer oemid);

    List<Oembase> selectAll();

    int updateByPrimaryKey(Oembase record);
}