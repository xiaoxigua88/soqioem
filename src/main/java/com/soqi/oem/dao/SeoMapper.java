package com.soqi.oem.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.soqi.oem.gentry.Seo;
import com.soqi.system.vo.Filter;

public interface SeoMapper {
    int deleteByPrimaryKey(Long taskid);

    int insert(Seo record);
    
    int batchInsert(List<Seo> seos);
    
    int batchDel(Integer[] taskid);

    Seo selectByPrimaryKey(Long taskid);

    List<Seo> selectAll();
    
    List<Seo> selectByTaskids(Integer[] taskid);
    
    List<Seo> selectByApiTaskId(@Param("apiranktaskid") Long apiranktaskid, @Param("apipricetaskid") Long apipricetaskid, @Param("apiwatchtaskid") Long apiwatchtaskid);

    int updateByPrimaryKey(Seo record);
    
    int seoRankUpdateByPlatformId(Seo record);
    
    int batchSeoFieldsByTaskids(@Param("taskids") Integer[] taskids, @Param("status") Integer status, @Param("freezeamount") BigDecimal freezeamount,@Param("oemfreezeamount") BigDecimal oemfreezeamount, @Param("buytime") Date buytime);
        
    int updateStatusByListSeo(List<Seo> seos);
    
    /**更新从第三方区取的服务ID
     * @param seos
     * @return
     */
    int updateServiceIdByListSeo(List<Seo> seos);
    
    public List<Seo> qrySeoManageListByUserId(@Param("userid") Integer userid, @Param("start") int start, @Param("size") int size);
    
    public List<Seo> qrySeoApplyListByUserId(@Param("userid") Integer userid, @Param("start") int start, @Param("size") int size);
    
    public List<Seo> qrySeoManageListByOemId(@Param("oemid") Integer oemid, @Param("start") int start, @Param("size") int size, @Param("filter") Filter filter);
    
    public int qryCountSeoManageListByUserId(Integer userid);
    
    public int qryCountSeoApplyListByUserId(Integer userid);
    
    public int qryCountSeoManageListByOemId(@Param("oemid") Integer oemid, @Param("filter") Filter filter);

}