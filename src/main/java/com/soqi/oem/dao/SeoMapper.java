package com.soqi.oem.dao;

import com.soqi.oem.gentry.Seo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SeoMapper {
    int deleteByPrimaryKey(Long taskid);

    int insert(Seo record);
    
    int batchInsert(List<Seo> seos);
    
    int batchDel(Integer[] taskid);

    Seo selectByPrimaryKey(Long taskid);

    List<Seo> selectAll();

    int updateByPrimaryKey(Seo record);
    
    public List<Seo> qrySeoManageListByUserId(@Param("userid") Integer userid, @Param("start") int start, @Param("size") int size);
    
    public List<Seo> qrySeoManageListByOemId(@Param("oemid") Integer oemid, @Param("start") int start, @Param("size") int size);
    
    public int qryCountSeoManageListByUserId(Integer userid);
    
    public int qryCountSeoManageListByOemId(Integer oemid);
}