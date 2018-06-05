package com.soqi.oem.dao;

import com.soqi.oem.gentry.Oemaccountdetail;
import java.util.List;

public interface OemaccountdetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Oemaccountdetail record);

    Oemaccountdetail selectByPrimaryKey(Integer id);

    List<Oemaccountdetail> selectAll();

    int updateByPrimaryKey(Oemaccountdetail record);
}