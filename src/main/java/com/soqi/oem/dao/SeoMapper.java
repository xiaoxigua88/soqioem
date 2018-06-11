package com.soqi.oem.dao;

import com.soqi.oem.gentry.Seo;
import java.util.List;

public interface SeoMapper {
    int deleteByPrimaryKey(Long taskid);

    int insert(Seo record);

    Seo selectByPrimaryKey(Long taskid);

    List<Seo> selectAll();

    int updateByPrimaryKey(Seo record);
}