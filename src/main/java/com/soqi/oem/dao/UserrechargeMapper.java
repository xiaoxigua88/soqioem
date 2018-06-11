package com.soqi.oem.dao;

import com.soqi.oem.gentry.Userrecharge;
import java.util.List;

public interface UserrechargeMapper {
    int deleteByPrimaryKey(Long orderid);

    int insert(Userrecharge record);

    Userrecharge selectByPrimaryKey(Long orderid);

    List<Userrecharge> selectAll();

    int updateByPrimaryKey(Userrecharge record);
}