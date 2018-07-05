package com.soqi.oem.dao;

import com.soqi.oem.gentry.Oemserviceconfig;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OemserviceconfigMapper {
    int deleteByPrimaryKey(@Param("oemid") Integer oemid, @Param("serviceid") Integer serviceid);

    int insert(Oemserviceconfig record);

    Oemserviceconfig selectByPrimaryKey(@Param("oemid") Integer oemid, @Param("serviceid") Integer serviceid);

    List<Oemserviceconfig> selectAll();

    int updateByPrimaryKey(Oemserviceconfig record);
}