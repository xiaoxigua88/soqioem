package com.soqi.system.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.apido.SeoDoBusiness;
import com.soqi.common.constants.Constant;
import com.soqi.common.utils.SeoWrapper;
import com.soqi.common.utils.SeopriceWrapper;
import com.soqi.oem.dao.SeoMapper;
import com.soqi.oem.dao.SeobalanceMapper;
import com.soqi.oem.dao.SeopriceMapper;
import com.soqi.oem.dao.UseraccountMapper;
import com.soqi.oem.dao.UseraccountdetailMapper;
import com.soqi.oem.gentry.Seo;
import com.soqi.oem.gentry.Seoprice;
import com.soqi.oem.gentry.Useraccount;
import com.soqi.oem.gentry.Useraccountdetail;

@Service
public class SeoService {
	
	@Autowired
	private SeoMapper seoMapper;
	@Autowired
	private SeopriceMapper piceMapper;
	@Autowired
	private SeobalanceMapper banlanceMapper;
	@Autowired
	private UseraccountMapper useraccountMapper;
	@Autowired
	private SeoDoBusiness seoDoBusiness;
	@Autowired
	private UseraccountdetailMapper userAcDetail;
	/**客户端查询云排名管理列表
	 * @param userid
	 * @param start
	 * @param size
	 * @return
	 */
	public List<Seo> qrySeoManageListByUserId(Integer userid, int start, int size){
		List<Seo> seoResult = seoMapper.qrySeoManageListByUserId(userid, start, size);
		return SeoWrapper.dealWithSeoResult(seoResult);
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
		List<Seo> seoResult = seoMapper.qrySeoManageListByOemId(oemid, start, size);
		return SeoWrapper.dealWithSeoResult(seoResult);
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
	public void deleteSeoTasks(Integer[] taskIds){
		//删除关键词价表数据
		 piceMapper.batchDeleteByTaskIds(taskIds);
		//删除关键词表数据
		seoMapper.batchDel(taskIds);
		
	}
	/**功能：关键词任务停止、不仅把任务从优化中调整为停止
	 * 也要在客户资金明细表中添加解冻记录
	 * 如果该客户是二级代理下的客户、同样需要在二级代理客户中添加相应的解冻记录
	 * @param taskIds
	 * @return
	 */
	@Transactional("primaryTransactionManager")
	public void stopSeoTasks(Integer[] taskIds){
		List<Seoprice> sps = piceMapper.selectByTaskids(taskIds);
		Map<String, List<Seoprice>> spuMap = SeopriceWrapper.getListbyUserid(sps);
		List<Useraccountdetail> uadList = new ArrayList<Useraccountdetail>();
		List<Useraccount> actList = new ArrayList<Useraccount>();
		for (Map.Entry<String, List<Seoprice>> entry : spuMap.entrySet()) {
			String userid = entry.getKey();
			List<Seoprice> splist = entry.getValue();
			Useraccount account = useraccountMapper.selectByPrimaryKey(Integer.valueOf(userid));
			BigDecimal free = BigDecimal.ZERO;
			for (Seoprice seoprice : splist) {
				//如果消费的天数经大于冻结天数、此时停止关键词任务无需解冻任何金
				if(seoprice.getCostcount().intValue() >= Constant.SEOFREEZEDAY){
					continue;
				}
				Useraccountdetail uad = new Useraccountdetail();
				uad.setUserid(seoprice.getUserid());
				uad.setAddtime(new Date());
				uad.setDescription("云排名停止");
				uad.setTradeid(seoprice.getTaskid().toString());
				uad.setTradetype(Constant.TRADE_TYPE_SEOSTOP);
				//uad.setId(Long.valueOf(IDUtils.createID()));
				uad.setChange(BigDecimal.ZERO);
				//任务停止时解冻金额=（关键词冻结总金额-消费金额 ）*关键词价格 或者 (冻结天数-消费天数)*关键词价格
				BigDecimal day = BigDecimal.valueOf(Constant.SEOFREEZEDAY - seoprice.getCostcount().intValue());
				//解冻值为负
				uad.setFreeze(BigDecimal.ZERO.subtract(seoprice.getPrice().multiply(day)));
				uad.setBalance(account.getTotalamount());
				uadList.add(uad);
				free = free.add(seoprice.getPrice().multiply(day));
			}
			//如果冻结金额总数小于解冻金额：比如两个关键词消耗掉冻结金额，开始消耗剩余账户金额、此时又发生购买关键词，把剩余账户的钱全部给冻结了，然后过段时间，该关键词又发生解冻行为
			//此时由于还有其它关键词在消耗冻结金额，会发生冻结金额小于该关键词解冻金额的行为，此时只把剩余冻结的解冻即可
			if(account.getFreezeamount().compareTo(free)==1){
				account.setFreezeamount(account.getFreezeamount().subtract(free));
			}else{
				account.setFreezeamount(BigDecimal.ZERO);
			}
			actList.add(account);
		}
		userAcDetail.batchInsert(uadList);
		useraccountMapper.batchUpdateAccountByDiffUser(actList);
		//更新关键词任务的状态停止
		seoMapper.batchSeoFieldsByTaskids(taskIds, Constant.SEO_STATUS_STOP, BigDecimal.ZERO, null);
	}
	
	
	/**关键词购买、冻结金额
	 * @param taskIds
	 * @param userid
	 * @param frozen
	 * @return
	 */
	@Transactional("primaryTransactionManager")
	public void applySeoTasks(Integer[] taskIds, Integer userid, BigDecimal freezeamount){
		//根据关键词任务ID获取该关建词价格表中的执行价格、并计算每个任务优化周期内需要冻结的资金
		Useraccount account = useraccountMapper.selectByPrimaryKey(userid);
		List<Seoprice> splist = piceMapper.selectByTaskids(taskIds);
		BigDecimal day = BigDecimal.valueOf(Constant.SEOFREEZEDAY);
		List<Useraccountdetail> uadList = new ArrayList<Useraccountdetail>();
		List<Seo> seoList = new ArrayList<Seo>();
		for (Seoprice seoprice : splist) {
			//添加交易明细
			Useraccountdetail uad = new Useraccountdetail();
			uad.setUserid(userid);
			uad.setAddtime(new Date());
			uad.setDescription("云排名购买");
			uad.setTradeid(seoprice.getTaskid().toString());
			uad.setTradetype(Constant.TRADE_TYPE_SEOBUY);
			//uad.setId(Long.valueOf(IDUtils.createID()));
			uad.setChange(BigDecimal.ZERO);
			uad.setFreeze(seoprice.getPrice().multiply(day));
			uad.setBalance(account.getTotalamount());
			uadList.add(uad);
			Seo seo = new Seo();
			//seo组装部分
			seo.setTaskid(seoprice.getTaskid());
			seo.setStatus(Constant.SEO_STATUS_DOING);
			seo.setFreezeamount(seoprice.getPrice().multiply(day));
			seoList.add(seo);
		}
		//冻结用户资产
		useraccountMapper.updateFreezeAmountByUserId(userid, freezeamount);
		//资金变动明细
		userAcDetail.batchInsert(uadList);
		//更新关键词任务的状态已购买
		seoMapper.batchSeoFieldsByTaskids(taskIds, Constant.SEO_STATUS_DOING, freezeamount,new Date());
	}
	
	/**计算冻结支付金额
	 * @param taskIds
	 * @return
	 */
	public BigDecimal frozenAmountOfPayment(Integer[] taskIds){
		List<Seoprice> splist = piceMapper.selectByTaskids(taskIds);
		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal day = BigDecimal.valueOf(Constant.SEOFREEZEDAY);
		for (Seoprice seoprice : splist) {
			amount = amount.add(seoprice.getPrice().multiply(day));
		}
		return amount;
	}
	/**功能：关键词任务启动、不仅把任务从停止调整为优化中状态
	 * 也要计算启动的任务总冻结金额是否够用
	 * 也要在客户资金明细表中添加冻结记录
	 * 如果该客户是二级代理下的客户、同样需要在二级代理客户中添加相应的冻结记录
	 * @param taskIds
	 * @return
	 */
	@Transactional("primaryTransactionManager")
	public void startSeoTasks(Integer[] taskIds){
		List<Seoprice> sps = piceMapper.selectByTaskids(taskIds);
		Map<String, List<Seoprice>> spuMap = SeopriceWrapper.getListbyUserid(sps);
		List<Useraccountdetail> uadList = new ArrayList<Useraccountdetail>();
		List<Useraccount> actList = new ArrayList<Useraccount>();
		List<Seo> seoList = new ArrayList<Seo>();
		for (Map.Entry<String, List<Seoprice>> entry : spuMap.entrySet()) {
			String userid = entry.getKey();
			List<Seoprice> splist = entry.getValue();
			Useraccount account = useraccountMapper.selectByPrimaryKey(Integer.valueOf(userid));
			BigDecimal frozen = BigDecimal.ZERO;
			for (Seoprice seoprice : splist) {
				Useraccountdetail uad = new Useraccountdetail();
				Seo seo = new Seo();
				//资金明细组装部分
				uad.setUserid(seoprice.getUserid());
				uad.setAddtime(new Date());
				uad.setDescription("云排名启动");
				uad.setTradeid(seoprice.getTaskid().toString());
				uad.setTradetype(Constant.TRADE_TYPE_SEOBUY);
				//uad.setId(Long.valueOf(IDUtils.createID()));
				uad.setChange(BigDecimal.ZERO);
				//从配置中获取
				BigDecimal day = BigDecimal.valueOf(Constant.SEOFREEZEDAY);
				//解冻值为负
				uad.setFreeze(seoprice.getPrice().multiply(day));
				uad.setBalance(account.getTotalamount());
				uadList.add(uad);
				frozen = frozen.add(seoprice.getPrice().multiply(day));
				//seo组装部分
				seo.setTaskid(seoprice.getTaskid());
				seo.setStatus(Constant.SEO_STATUS_DOING);
				seo.setFreezeamount(seoprice.getPrice().multiply(day));
				seoList.add(seo);
			}
			account.setFreezeamount(account.getFreezeamount().add(frozen));
			actList.add(account);
		}
		userAcDetail.batchInsert(uadList);
		useraccountMapper.batchUpdateAccountByDiffUser(actList);
		//更新每一条关键启的启动金额、注意每条关键词的用户可能是不同的
		seoMapper.updateStatusByListSeo(seoList);
	}
	/**功能：用户购买关键启时，需要对自身充值金额进地检查、资金不够要提示
	 * @param taskIds
	 * @param userid
	 * @return
	 */
	public boolean checkAmountForSeoApply(Integer[] taskIds, Integer userid){
		Useraccount account = useraccountMapper.selectByPrimaryKey(userid);
		if(account == null){
			return false;
		}
		//购买优化任务需要冻结资金
		BigDecimal frozen = frozenAmountOfPayment(taskIds);
		//可用金额
		BigDecimal availableamount = account.getTotalamount().subtract(account.getFreezeamount());
		//可用金额大于需要冻结金额则返回true
		if(availableamount.compareTo(frozen)==1){
        	return true;
        }
		return false;
	}
	/**检查用户是否有资格批量启动关键词、这些关键词可能分属不同的用户
	 * @param taskIds
	 * @param forbidden
	 * @return
	 */
	public boolean checkUsersAllowForSeoApply(Integer[] taskIds, StringBuffer text){
		List<Seoprice> sps = piceMapper.selectByTaskids(taskIds);
		Map<String, List<Seoprice>> spuMap = SeopriceWrapper.getListbyUserid(sps);
		for (Map.Entry<String, List<Seoprice>> entry : spuMap.entrySet()) {
			String userid = entry.getKey();
			List<Seoprice> splist = entry.getValue();
			Useraccount account = useraccountMapper.selectByPrimaryKey(Integer.valueOf(userid));
			//购买优化任务需要冻结资金
			BigDecimal frozen = BigDecimal.ZERO;
			BigDecimal day = BigDecimal.valueOf(Constant.SEOFREEZEDAY);
			for (Seoprice seoprice : splist) {
				frozen = frozen.add(seoprice.getPrice().multiply(day));
			}
			//可用金额
			BigDecimal availableamount = account.getTotalamount().subtract(account.getFreezeamount());
			//冻结金额如果大于可用金额直接退出并返回false
			if(frozen.compareTo(availableamount)==1){
				text.append("编号为<b class='text-red'>" + userid + "</b>用户！可用金额不足需再充值：<b class='text-red'>￥"+frozen.subtract(availableamount)+"</b>元才能启所选任务！");
	        	return false;
	        }
		}
		return true;
	}
	@Transactional("primaryTransactionManager")
	public int batchSameSeoInsert(List<Seo> seos){
		//调用接口获取关键词任务id
		seoDoBusiness.keywordRankSearchAdd(seos);
		//批量操作过后、返回最后一条数据的自增主键值
		int count = seoMapper.batchInsert(seos);
		if(count == seos.size()){
			Long taskid = seos.get(0).getTaskid();
			for(int i = count-1 ; i >= 0; i--){
				seos.get(i).setTaskid(taskid - (count - i -1));
			}
		}
		int rankCount = piceMapper.batchInsertFormSeo(seos);
		return rankCount > 0 ? count : 0;
	}
}
