package com.soqi.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		//调用接口对任务进行处理如原始价格
		//调用该用户的执行价格方式是打折还是统一一口价
		return seoMapper.batchInsert(seos);
	}
	@Transactional("primaryTransactionManager")
	public int deleteSeoTasks(Integer[] taskIds){
		//删除关键词价表数据
		//删除关键词表数据
		return seoMapper.batchDel(taskIds);
	}
}
