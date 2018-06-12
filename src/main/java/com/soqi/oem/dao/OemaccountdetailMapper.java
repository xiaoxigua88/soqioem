package com.soqi.oem.dao;

import com.soqi.oem.gentry.Oemaccountdetail;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OemaccountdetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Oemaccountdetail record);

    Oemaccountdetail selectByPrimaryKey(Long id);

    List<Oemaccountdetail> selectAll();

    int updateByPrimaryKey(Oemaccountdetail record);
    
    public List<Oemaccountdetail> qryOemAcDtlsByOemid(@Param("oemid") Integer oemid, @Param("start") int start, @Param("size") int size);
    
    public int qryCountOemAcDtlsByOemid(Integer oemid);
}