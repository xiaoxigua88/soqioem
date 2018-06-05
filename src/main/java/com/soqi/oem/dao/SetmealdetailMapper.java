package com.soqi.oem.dao;

import com.soqi.oem.gentry.Setmealdetail;
import java.util.List;

public interface SetmealdetailMapper {
    int deleteByPrimaryKey(Integer itemid);

    int insert(Setmealdetail record);

    Setmealdetail selectByPrimaryKey(Integer itemid);

    List<Setmealdetail> selectAll();

    int updateByPrimaryKey(Setmealdetail record);
}