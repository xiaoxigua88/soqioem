package com.soqi.oem.dao;

import com.soqi.oem.gentry.Useraccountdetail;
import java.util.List;

public interface UseraccountdetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Useraccountdetail record);

    Useraccountdetail selectByPrimaryKey(Integer id);

    List<Useraccountdetail> selectAll();

    int updateByPrimaryKey(Useraccountdetail record);
}