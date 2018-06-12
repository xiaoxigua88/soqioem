package com.soqi.oem.dao;

import com.soqi.oem.gentry.Oemaccountdetail;
import java.util.List;

public interface OemaccountdetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Oemaccountdetail record);

    Oemaccountdetail selectByPrimaryKey(Long id);

    List<Oemaccountdetail> selectAll();

    int updateByPrimaryKey(Oemaccountdetail record);
}