package com.soqi.oem.dao;

import com.soqi.oem.gentry.Seo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SeoMapper {
    int deleteByPrimaryKey(Long taskid);

    int insert(Seo record);
    
    int batchInsert(List<Seo> seos);
    
    int batchDel(Integer[] taskid);

    Seo selectByPrimaryKey(Long taskid);

    List<Seo> selectAll();
    
    List<Seo> selectByTaskids(Integer[] taskid);

    int updateByPrimaryKey(Seo record);
    
    int batchSeoFieldsByTaskids(@Param("taskids") Integer[] taskids, @Param("status") Integer status, @Param("freezeamount") BigDecimal freezeamount, @Param("buytime") Date buytime);
    
    int updateStatusByListSeo(List<Seo> seos);
    
    /**更新从第三方区取的服务ID
     * @param seos
     * @return
     */
    int updateServiceIdByListSeo(List<Seo> seos);
    
    public List<Seo> qrySeoManageListByUserId(@Param("userid") Integer userid, @Param("start") int start, @Param("size") int size);
    
    public List<Seo> qrySeoApplyListByUserId(@Param("userid") Integer userid, @Param("start") int start, @Param("size") int size);
    
    public List<Seo> qrySeoManageListByOemId(@Param("oemid") Integer oemid, @Param("start") int start, @Param("size") int size);
    
    public int qryCountSeoManageListByUserId(Integer userid);
    
    public int qryCountSeoApplyListByUserId(Integer userid);
    
    public int qryCountSeoManageListByOemId(Integer oemid);
}