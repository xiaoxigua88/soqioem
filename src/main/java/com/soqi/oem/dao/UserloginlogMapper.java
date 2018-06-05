package com.soqi.oem.dao;

import com.soqi.oem.gentry.Userloginlog;
import java.util.List;

public interface UserloginlogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Userloginlog record);

    Userloginlog selectByPrimaryKey(Long id);

    List<Userloginlog> selectAll();

    int updateByPrimaryKey(Userloginlog record);
}