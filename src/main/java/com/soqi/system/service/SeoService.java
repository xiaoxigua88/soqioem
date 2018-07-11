package com.soqi.system.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.apido.SeoDoBusiness;
import com.soqi.common.constants.Constant;
import com.soqi.common.utils.SeoWrapper;
import com.soqi.common.utils.SeopriceWrapper;
import com.soqi.oem.dao.OemaccountMapper;
import com.soqi.oem.dao.OemaccountdetailMapper;
import com.soqi.oem.dao.OembaseMapper;
import com.soqi.oem.dao.SeoMapper;
import com.soqi.oem.dao.SeobalanceMapper;
import com.soqi.oem.dao.SeopriceMapper;
import com.soqi.oem.dao.UseraccountMapper;
import com.soqi.oem.dao.UseraccountdetailMapper;
import com.soqi.oem.gentry.Oemaccount;
import com.soqi.oem.gentry.Oemaccountdetail;
import com.soqi.oem.gentry.Oembase;
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
	private OembaseMapper oembaseMapper;
	@Autowired
	private UseraccountdetailMapper userAcDetail;
	@Autowired
	private OemaccountMapper oemaccountMapper;
	@Autowired
	private OemaccountdetailMapper oemAcDetail;
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
	
	/**客户端查询关键词
	 * @param userid
	 * @param start
	 * @param size
	 * @return
	 */
	public Seo qrySeoByTaskid(Long taskid){
		return seoMapper.selectByPrimaryKey(taskid);
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
	public int addSeoTask(Map<String, List<Seo>> listMap){
		List<Seo> seos = new ArrayList<Seo>();
		for (Map.Entry<String, List<Seo>> entry : listMap.entrySet()) {
			seos.addAll(entry.getValue());
		}
		int count = seoMapper.batchInsert(seos);
		if(count == seos.size()){
			Long taskid = seos.get(0).getTaskid();
			for(int i = count-1 ; i >= 0; i--){
				seos.get(i).setTaskid(taskid - (count - i -1));
			}
		}
		//生成关键词价格数据
		piceMapper.batchInsertFormSeo(seos);
		//异步调用生成服务ID
		seoDoBusiness.createServiceIdOfRP(listMap);
		return count;
		
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
	public void stopSeoTasks(Integer[] taskIds, Integer oemid){
		//查看代理是不是要参与结算
		Oembase currentOem = oembaseMapper.selectByPrimaryKey(oemid);
		Oemaccount oemact = null;
		BigDecimal oemFreemount = BigDecimal.ZERO;
		if(currentOem.getParentoemid().intValue() != 0){
			oemact = oemaccountMapper.selectByPrimaryKey(oemid);
		}
		//按排名区间为1-10取保证取一条
		List<Seoprice> sps = piceMapper.selectByTaskids(taskIds);
		Map<String, List<Seoprice>> spuMap = SeopriceWrapper.getListbyUserid(sps);
		List<Useraccountdetail> uadList = new ArrayList<Useraccountdetail>();
		List<Oemaccountdetail> oadList = new ArrayList<Oemaccountdetail>();
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
				//任务停止时解冻金额=（关键词冻结总金额-消费金额 ）*关键词价格 或者 (冻结天数-消费天数)*关键词价格
				BigDecimal day = BigDecimal.valueOf(Constant.SEOFREEZEDAY - seoprice.getCostcount().intValue());
				
				//客户账户详情封装
				Useraccountdetail uad = new Useraccountdetail();
				uad.setUserid(seoprice.getUserid());
				uad.setAddtime(new Date());
				uad.setDescription("云排名停止");
				uad.setTradeid(seoprice.getTaskid().toString());
				uad.setTradetype(Constant.TRADE_TYPE_SEOSTOP);
				uad.setChange(BigDecimal.ZERO);
				//解冻值为负
				uad.setFreeze(BigDecimal.ZERO.subtract(seoprice.getPrice().multiply(day)));
				uad.setBalance(account.getTotalamount());
				uadList.add(uad);
				free = free.add(seoprice.getPrice().multiply(day));
				
				//代理封装部分
				Oemaccountdetail oad = new Oemaccountdetail();
				oad.setOemid(oemid);
				oad.setAddtime(new Date());
				oad.setDescription("云排名停止");
				oad.setTradeid(seoprice.getTaskid().toString());
				oad.setTradetype(Constant.TRADE_TYPE_SEOSTOP);
				oad.setChange(BigDecimal.ZERO);
				//解冻值为负
				oad.setFreeze(BigDecimal.ZERO.subtract(seoprice.getPriceoemchild().multiply(day)));
				oad.setBalance(oemact.getTotalamount());
				oadList.add(oad);
				//代理解冻金额
				oemFreemount = oemFreemount.add(seoprice.getPriceoemchild().multiply(day));
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
		//客户账户详变动
		userAcDetail.batchInsert(uadList);
		//更新客户账户信息
		useraccountMapper.batchUpdateAccountByDiffUser(actList);
		//更新代理账户信息
		if(null != oemact){
			if(oemact.getFreezeamount().compareTo(oemFreemount)==1){
				oemact.setFreezeamount(oemact.getFreezeamount().subtract(oemFreemount));
			}else{
				oemact.setFreezeamount(BigDecimal.ZERO);
			}
			oemaccountMapper.updateByPrimaryKey(oemact);
			//代理账户详细变动添加
			oemAcDetail.batchInsert(oadList);
		}
		//更新关键词任务的状态停止
		seoMapper.batchSeoFieldsByTaskids(taskIds, Constant.SEO_STATUS_STOP, BigDecimal.ZERO, null);
		//云服务上的任务也要做相应的删除操作
		seoDoBusiness.keywordRankDel(taskIds);
	}
	
	
	/**关键词购买、冻结金额
	 * 根据关键词任务ID获取该关建词价格表中的执行价格、并计算每个任务优化周期内需要冻结的资金
	 * @param taskIds
	 * @param userid
	 * @param oemid
	 * @param clientFreezeamount 冻结用户资金
	 * @return
	 */
	@Transactional("primaryTransactionManager")
	public void applySeoTasks(Integer[] taskIds, Integer userid, Integer oemid, BigDecimal clientFreezeamount){
		//查看代理是不是要参与结算
		Oembase currentOem = oembaseMapper.selectByPrimaryKey(oemid);
		Oemaccount oemact = null;
		BigDecimal oemFreezeamount = BigDecimal.ZERO;
		if(currentOem.getParentoemid().intValue() != 0){
			oemact = oemaccountMapper.selectByPrimaryKey(oemid);
		}
		
		Useraccount account = useraccountMapper.selectByPrimaryKey(userid);
		List<Seoprice> splist = piceMapper.selectByTaskids(taskIds);
		BigDecimal day = BigDecimal.valueOf(Constant.SEOFREEZEDAY);
		List<Useraccountdetail> uadList = new ArrayList<Useraccountdetail>();
		List<Oemaccountdetail> oadList = new ArrayList<Oemaccountdetail>();
		List<Seo> seoList = new ArrayList<Seo>();
		for (Seoprice seoprice : splist) {
			//客户添加交易明细
			Useraccountdetail uad = new Useraccountdetail();
			uad.setUserid(userid);
			uad.setAddtime(new Date());
			uad.setDescription("云排名购买");
			uad.setTradeid(seoprice.getTaskid().toString());
			uad.setTradetype(Constant.TRADE_TYPE_SEOBUY);
			uad.setChange(BigDecimal.ZERO);
			uad.setFreeze(seoprice.getPrice().multiply(day));
			uad.setBalance(account.getTotalamount());
			uadList.add(uad);
			
			//代理添加交易明细
			if(null != oemact){
				Oemaccountdetail oad = new Oemaccountdetail();
				oad.setOemid(oemid);
				oad.setAddtime(new Date());
				oad.setDescription("云排名购买");
				oad.setTradeid(seoprice.getTaskid().toString());
				oad.setTradetype(Constant.TRADE_TYPE_SEOBUY);
				oad.setChange(BigDecimal.ZERO);
				oemFreezeamount = oemFreezeamount.add(seoprice.getPriceoemchild().multiply(day));
				oad.setFreeze(seoprice.getPriceoemchild().multiply(day));
				oad.setBalance(oemact.getTotalamount());
				oadList.add(oad);
			}
			
			Seo seo = new Seo();
			//seo组装部分
			seo.setTaskid(seoprice.getTaskid());
			seo.setStatus(Constant.SEO_STATUS_DOING);
			seo.setFreezeamount(seoprice.getPrice().multiply(day));
			seo.setBuytime(new Date());
			seoList.add(seo);
		}
		//冻结用户资产
		useraccountMapper.updateFreezeAmountByUserId(userid, clientFreezeamount);
		//资金变动明细
		userAcDetail.batchInsert(uadList);
		
		//冻结代理资产
		if(!oadList.isEmpty()){
			oemaccountMapper.updateFreezeAmountByOemId(oemid, oemFreezeamount);
			//资金变动明细
			oemAcDetail.batchInsert(oadList);
		}
		
		//更新关键词任务的状态已购买
		//seoMapper.batchSeoFieldsByTaskids(taskIds, Constant.SEO_STATUS_DOING, freezeamount,new Date());
		//更新每一条关键启的启动金额、注意每条关键词的用户可能是不同的
		seoMapper.updateStatusByListSeo(seoList);
		//调用异步服务获取云排名监控服务ID
		seoDoBusiness.createServiceIdOfW(taskIds);
	}
	
	/**计算冻结支付金额
	 * @param taskIds
	 * @return
	 */
	public BigDecimal frozenAmountOfPayment(Integer[] taskIds, String usertype){
		List<Seoprice> splist = piceMapper.selectByTaskids(taskIds);
		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal day = BigDecimal.valueOf(Constant.SEOFREEZEDAY);
		if(StringUtils.equals(usertype, Constant.USERTYPE_USER)){
			for (Seoprice seoprice : splist) {
				amount = amount.add(seoprice.getPrice().multiply(day));
			}
		}else if(StringUtils.equals(usertype, Constant.USERTYPE_OEM)){
			for (Seoprice seoprice : splist) {
				amount = amount.add(seoprice.getPriceoemchild().multiply(day));
			}
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
	public void startSeoTasks(Integer[] taskIds, Integer oemid){
		//查看代理是不是要参与结算
		Oembase currentOem = oembaseMapper.selectByPrimaryKey(oemid);
		Oemaccount oemact = null;
		BigDecimal oemFreezeamount = BigDecimal.ZERO;
		if(currentOem.getParentoemid().intValue() != 0){
			oemact = oemaccountMapper.selectByPrimaryKey(oemid);
		}
		List<Seoprice> sps = piceMapper.selectByTaskids(taskIds);
		Map<String, List<Seoprice>> spuMap = SeopriceWrapper.getListbyUserid(sps);
		List<Useraccountdetail> uadList = new ArrayList<Useraccountdetail>();
		List<Oemaccountdetail> oadList = new ArrayList<Oemaccountdetail>();
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
				//客户资金明细组装部分
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
				
				//代理添加交易明细
				if(null != oemact){
					Oemaccountdetail oad = new Oemaccountdetail();
					oad.setOemid(oemid);
					oad.setAddtime(new Date());
					oad.setDescription("云排名购买");
					oad.setTradeid(seoprice.getTaskid().toString());
					oad.setTradetype(Constant.TRADE_TYPE_SEOBUY);
					oad.setChange(BigDecimal.ZERO);
					oemFreezeamount = oemFreezeamount.add(seoprice.getPriceoemchild().multiply(day));
					oad.setFreeze(seoprice.getPriceoemchild().multiply(day));
					oad.setBalance(oemact.getTotalamount());
					oadList.add(oad);
				}
				
				//seo组装部分
				seo.setTaskid(seoprice.getTaskid());
				seo.setStatus(Constant.SEO_STATUS_DOING);
				seo.setFreezeamount(seoprice.getPrice().multiply(day));
				seoList.add(seo);
			}
			account.setFreezeamount(account.getFreezeamount().add(frozen));
			actList.add(account);
		}
		//添加客户购买关键词记录
		userAcDetail.batchInsert(uadList);
		useraccountMapper.batchUpdateAccountByDiffUser(actList);
		//更新代理账户信息
		if(null != oemact){
			oemact.setFreezeamount(oemact.getFreezeamount().add(oemFreezeamount));
			oemaccountMapper.updateByPrimaryKey(oemact);
			//代理账户详细变动添加
			oemAcDetail.batchInsert(oadList);
		}
		//更新每一条关键启的启动金额、注意每条关键词的用户可能是不同的
		seoMapper.updateStatusByListSeo(seoList);
		//调用异步服务获取云排名监控服务ID
		seoDoBusiness.createServiceIdOfW(taskIds);
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
		BigDecimal frozen = frozenAmountOfPayment(taskIds,Constant.USERTYPE_USER);
		//可用金额
		BigDecimal availableamount = account.getTotalamount().subtract(account.getFreezeamount());
		//可用金额大于需要冻结金额则返回true
		if(availableamount.compareTo(frozen)==1){
        	return true;
        }
		return false;
	}
	
	/**功能：用户购买关键词时、同样需要对其上级代理资金做校验
	 * 如果是顶代则无需校验金额
	 * @param taskIds
	 * @param userid
	 * @return
	 */
	public boolean checkOemAmountBySeoApply(Integer[] taskIds, Integer oemid){
		Oembase currentOem = oembaseMapper.selectByPrimaryKey(oemid);
		if(currentOem.getParentoemid().intValue() == 0){
			//客户的当前代理为顶代、无须进行金额校验
			return true;
		}else{
			Oemaccount account = oemaccountMapper.selectByPrimaryKey(oemid);
			if(account == null){
				return false;
			}
			//购买优化任务需要冻结资金
			BigDecimal frozen = frozenAmountOfPayment(taskIds,Constant.USERTYPE_OEM);
			//可用金额
			BigDecimal availableamount = account.getTotalamount().subtract(account.getFreezeamount());
			//可用金额大于需要冻结金额则返回true
			if(availableamount.compareTo(frozen)==1){
	        	return true;
	        }
			return false;
		}
		
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
	public int batchSameSeoInsert(Map<String, List<Seo>> listMap){
		//注意此处listMap和seos中的每个seo对象是同一个对像。。所以设置taskid后,这两个集合中的对像也会同步更改，这应该就是地址引用传递、对像共享
		List<Seo> seos = new ArrayList<Seo>();
		for (Map.Entry<String, List<Seo>> entry : listMap.entrySet()) {
			seos.addAll(entry.getValue());
		}
		int count = seoMapper.batchInsert(seos);
		if(count == seos.size()){
			Long taskid = seos.get(0).getTaskid();
			for(int i = count-1 ; i >= 0; i--){
				seos.get(i).setTaskid(taskid - (count - i -1));
			}
		}
		int rankCount = piceMapper.batchInsertFormSeo(seos);
		//异步调用生成服务ID
		seoDoBusiness.createServiceIdOfRP(listMap);
		return rankCount > 0 ? count : 0;
	}
}
