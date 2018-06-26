package com.soqi.oem.dao;

import com.soqi.oem.gentry.Seo;
import com.soqi.oem.gentry.Seoprice;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SeopriceMapper {
    int deleteByPrimaryKey(@Param("taskid") Long taskid, @Param("fromrank") Integer fromrank, @Param("torank") Integer torank);

    int insert(Seoprice record);
    
    int batchInsertFormSeo(List<Seo> seos);

    Seoprice selectByPrimaryKey(@Param("taskid") Long taskid, @Param("fromrank") Integer fromrank, @Param("torank") Integer torank);

    List<Seoprice> selectAll();

    int updateByPrimaryKey(Seoprice record);
}