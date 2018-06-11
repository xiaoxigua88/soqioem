package com.soqi.oem.dao;

import com.soqi.oem.gentry.Seobalance;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SeobalanceMapper {
    int deleteByPrimaryKey(@Param("taskid") Long taskid, @Param("consumedate") Date consumedate);

    int insert(Seobalance record);

    Seobalance selectByPrimaryKey(@Param("taskid") Long taskid, @Param("consumedate") Date consumedate);

    List<Seobalance> selectAll();

    int updateByPrimaryKey(Seobalance record);
}