package com.soqi.oem.dao;

import com.soqi.oem.gentry.Oemuser;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OemuserMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(Oemuser record);

    Oemuser selectByPrimaryKey(Integer userid);

    Oemuser selectByDomainAndMobile(@Param("domain") String domain, @Param("mobile") String mobile);
    
    List<Oemuser> selectByOemId(@Param("oemid") int oemid, @Param("start") int start, @Param("size") int size);
    
    int selectCountByOemId(Integer oemid);
    
    Oemuser selectOemuserByMobileAndOemid(@Param("oemid") Integer oemid, @Param("mobile") String mobile, @Param("userid") Integer userid);
    
    Oemuser selectOemuserByUseridAndOemid(@Param("oemid") Integer oemid, @Param("userid") Integer userid);
    
    Oemuser qryOemuserByOemidAndField(@Param("oemid") Integer oemid, @Param("field") String field);
    
    List<Oemuser> selectAll();

    int updateByPrimaryKey(Oemuser record);
}