package com.soqi.oem.dao;

import com.soqi.oem.gentry.Oemloginlog;
import java.util.List;

public interface OemloginlogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Oemloginlog record);

    Oemloginlog selectByPrimaryKey(Long id);

    List<Oemloginlog> selectAll();

    int updateByPrimaryKey(Oemloginlog record);
}