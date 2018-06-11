package com.soqi.oem.dao;

import com.soqi.oem.gentry.Oemrecharge;
import java.util.List;

public interface OemrechargeMapper {
    int deleteByPrimaryKey(Long orderid);

    int insert(Oemrecharge record);

    Oemrecharge selectByPrimaryKey(Long orderid);

    List<Oemrecharge> selectAll();

    int updateByPrimaryKey(Oemrecharge record);
}