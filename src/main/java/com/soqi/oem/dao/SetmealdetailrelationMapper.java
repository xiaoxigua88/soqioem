package com.soqi.oem.dao;

import com.soqi.oem.gentry.Setmealdetailrelation;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SetmealdetailrelationMapper {
    int deleteByPrimaryKey(@Param("mealid") Integer mealid, @Param("itemid") Integer itemid);

    int insert(Setmealdetailrelation record);

    List<Setmealdetailrelation> selectAll();
}