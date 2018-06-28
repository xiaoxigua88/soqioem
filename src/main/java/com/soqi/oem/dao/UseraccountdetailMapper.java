package com.soqi.oem.dao;

import com.soqi.oem.gentry.Useraccountdetail;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UseraccountdetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Useraccountdetail record);

    Useraccountdetail selectByPrimaryKey(Long id);

    List<Useraccountdetail> selectAll();

    int updateByPrimaryKey(Useraccountdetail record);
    
    public List<Useraccountdetail> qryUserAcDtlsByOemid(@Param("oemid") Integer oemid, @Param("start") int start, @Param("size") int size);
    
    public int qryCountUserAcDtlsByOemid(Integer oemid);
    
    public List<Useraccountdetail> qryUserAcDtlsByUserid(@Param("userid") Integer userid, @Param("start") int start, @Param("size") int size);
    
    public int qryCountUserAcDtlsByUserid(Integer oemid);
    
    public int batchInsert(List<Useraccountdetail> uadList);
}