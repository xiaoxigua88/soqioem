package com.soqi.oem.dao;

import com.soqi.oem.gentry.Userloginlog;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserloginlogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Userloginlog record);

    Userloginlog selectByPrimaryKey(Long id);

    List<Userloginlog> selectAll();

    int updateByPrimaryKey(Userloginlog record);
    
    public List<Userloginlog> qryUserLogsByOemid(@Param("oemid") Integer oemid, @Param("start") int start, @Param("size") int size);
    
    public int qryUserLogsCountByOemid(Integer oemid);
}