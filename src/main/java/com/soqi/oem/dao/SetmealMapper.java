package com.soqi.oem.dao;

import com.soqi.oem.gentry.Setmeal;
import java.util.List;

public interface SetmealMapper {
    int deleteByPrimaryKey(Integer mealid);

    int insert(Setmeal record);

    Setmeal selectByPrimaryKey(Integer mealid);

    List<Setmeal> selectAll();

    int updateByPrimaryKey(Setmeal record);
}