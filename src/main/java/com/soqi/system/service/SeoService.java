package com.soqi.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.apido.SeoDoBusiness;
import com.soqi.oem.dao.SeoMapper;
import com.soqi.oem.dao.SeobalanceMapper;
import com.soqi.oem.dao.SeopriceMapper;
import com.soqi.oem.gentry.Seo;

@Service
public class SeoService {
	
	@Autowired
	private SeoMapper seoMapper;
	@Autowired
	private SeopriceMapper piceMapper;
	@Autowired
	private SeobalanceMapper banlanceMapper;
	@Autowired
	private SeoDoBusiness seoDoBusiness;
	/**客户端查询云排名管理列表
	 * @param userid
	 * @param start
	 * @param size
	 * @return
	 */
	public List<Seo> qrySeoManageListByUserId(Integer userid, int start, int size){
		return seoMapper.qrySeoManageListByUserId(userid, start, size);
	}
	
	/**客户查询云排名任务管理数目
	 * @param userid
	 * @return
	 */
	public int qryCountSeoManageListByUserId(Integer userid){
		return seoMapper.qryCountSeoManageListByUserId(userid);
	}
	
	/**客户端查询云排名购买列表
	 * @param userid
	 * @param start
	 * @param size
	 * @return
	 */
	public List<Seo> qrySeoApplyListByUserId(Integer userid, int start, int size){
		return seoMapper.qrySeoApplyListByUserId(userid, start, size);
	}
	
	/**客户查询云排名购买条目
	 * @param userid
	 * @return
	 */
	public int qryCountSeoApplyListByUserId(Integer userid){
		return seoMapper.qryCountSeoApplyListByUserId(userid);
	}
	
	/**代理端查询云排名管理列表
	 * @param userid
	 * @param start
	 * @param size
	 * @return
	 */
	public List<Seo> qrySeoManageListByOemId(Integer oemid, int start, int size){
		return seoMapper.qrySeoManageListByOemId(oemid, start, size);
	}
	
	/**代理端查询云排名任务管理数目
	 * @param userid
	 * @return
	 */
	public int qryCountSeoManageListByOemId(Integer oemid){
		return seoMapper.qryCountSeoManageListByOemId(oemid);
	}
	
	@Transactional("primaryTransactionManager")
	public int addSeoTask(List<Seo> seos){
		//TODO
		//调用接口获取关键词任务id
		seoDoBusiness.keywordRankSearchAdd(seos);
		//调用接口对接关键词价格id
		seoDoBusiness.KeywordPriceSearchAdd(seos);
		return seoMapper.batchInsert(seos);
	}
	@Transactional("primaryTransactionManager")
	public int deleteSeoTasks(Integer[] taskIds){
		//删除关键词价表数据
		//删除关键词表数据
		return seoMapper.batchDel(taskIds);
	}
}
