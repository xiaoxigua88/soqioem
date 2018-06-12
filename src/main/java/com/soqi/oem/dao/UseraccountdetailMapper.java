package com.soqi.oem.dao;

import com.soqi.oem.gentry.Useraccountdetail;
import java.util.List;

public interface UseraccountdetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Useraccountdetail record);

    Useraccountdetail selectByPrimaryKey(Long id);

    List<Useraccountdetail> selectAll();

    int updateByPrimaryKey(Useraccountdetail record);
}