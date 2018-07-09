package com.soqi.system.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.common.constants.Constant;
import com.soqi.common.utils.IDUtils;
import com.soqi.oem.dao.OemaccountMapper;
import com.soqi.oem.dao.OemaccountdetailMapper;
import com.soqi.oem.dao.OemrechargeMapper;
import com.soqi.oem.dao.UseraccountMapper;
import com.soqi.oem.dao.UseraccountdetailMapper;
import com.soqi.oem.dao.UserrechargeMapper;
import com.soqi.oem.gentry.Oemaccount;
import com.soqi.oem.gentry.Oemaccountdetail;
import com.soqi.oem.gentry.Oemrecharge;
import com.soqi.oem.gentry.Useraccount;
import com.soqi.oem.gentry.Useraccountdetail;
import com.soqi.oem.gentry.Userrecharge;

@Service
public class FinanceService {

	@Autowired
	private UserrechargeMapper userCharge;;
	@Autowired
	private UseraccountMapper userAccount;
	@Autowired
	private UseraccountdetailMapper userAcDetail;
	
	@Autowired
	private OemrechargeMapper oemCharge;;
	@Autowired
	private OemaccountMapper oemAccount;
	@Autowired
	private OemaccountdetailMapper oemAcDetail;
	
	@Transactional("primaryTransactionManager")
	public int oemRecharge(Oemrecharge charge){
		//查询用户资金
		Oemaccount ua = oemAccount.selectByPrimaryKey(charge.getOemid());
		
		//资金明细初始化
		Oemaccountdetail uadl = new Oemaccountdetail();
		uadl.setOemid(charge.getOemid());
		uadl.setAddtime(charge.getAddtime());
		uadl.setDescription("手工充值");
		//业务单号
		uadl.setTradeid(charge.getOrderid().toString());
		//业务类型
		uadl.setTradetype(Constant.TRADE_TYPE_CHARGE);
		uadl.setId(Long.valueOf(IDUtils.createID()));
		uadl.setChange(charge.getAmount());
		uadl.setFreeze(BigDecimal.ZERO);
		if(ua == null){
			//添加用户资金
			ua = new Oemaccount();
			ua.setFreezeamount(BigDecimal.ZERO);
			ua.setSeoamountneed(BigDecimal.ZERO);
			ua.setTotalamount(charge.getAmount());
			ua.setOemid(charge.getOemid());
			oemAccount.insert(ua);
			//账户余额
			uadl.setBalance(charge.getAmount());
		}
		//总额加上充值金额
		ua.setTotalamount(ua.getTotalamount().add(charge.getAmount()));
		//更新资金表
		oemAccount.updateByPrimaryKey(ua);
		
		//明细表
		uadl.setBalance(ua.getTotalamount());
		oemAcDetail.insert(uadl);
		
		return oemCharge.insert(charge);
	}
	
	@Transactional("primaryTransactionManager")
	public int userRecharge(Userrecharge charge){
		//查询用户资金
		Useraccount ua = userAccount.selectByPrimaryKey(charge.getUserid());
		
		//资金明细初始化
		Useraccountdetail uadl = new Useraccountdetail();
		uadl.setUserid(charge.getUserid());
		uadl.setAddtime(charge.getAddtime());
		uadl.setDescription("系统充值");
		//业务单号
		uadl.setTradeid(charge.getOrderid().toString());
		//业务类型
		uadl.setTradetype(Constant.TRADE_TYPE_CHARGE);
		uadl.setId(Long.valueOf(IDUtils.createID()));
		uadl.setChange(charge.getAmount());
		uadl.setFreeze(BigDecimal.ZERO);
		if(ua == null){
			//添加用户资金
			ua = new Useraccount();
			ua.setFreezeamount(BigDecimal.ZERO);
			ua.setSeoamountneed(BigDecimal.ZERO);
			ua.setTotalamount(charge.getAmount());
			ua.setUserid(charge.getUserid());
			userAccount.insert(ua);
			//账户余额
			uadl.setBalance(charge.getAmount());
		}else{
			//总额加上充值金额
			ua.setTotalamount(ua.getTotalamount().add(charge.getAmount()));
			//更新资金表
			userAccount.updateByPrimaryKey(ua);
		}
		
		//明细表
		uadl.setBalance(ua.getTotalamount());
		userAcDetail.insert(uadl);
		
		return userCharge.insert(charge);
	}
	
	public List<Useraccountdetail> qryUserAcDtlsByOemid(Integer oemId, int start, int size){
		return userAcDetail.qryUserAcDtlsByOemid(oemId, start, size);
	}
	
	public int qryCountUserAcDtlsByOemid(Integer oemId){
		return userAcDetail.qryCountUserAcDtlsByOemid(oemId);
	}
	
	public List<Useraccountdetail> qryUserAcDtlsByUserid(Integer userId, int start, int size){
		return userAcDetail.qryUserAcDtlsByUserid(userId, start, size);
	}
	
	public int qryCountUserAcDtlsByUserid(Integer userId){
		return userAcDetail.qryCountUserAcDtlsByUserid(userId);
	}
	
	public List<Oemaccountdetail> qryOemAcDtlsByOemid(Integer oemId, int start, int size){
		return oemAcDetail.qryOemAcDtlsByOemid(oemId, start, size);
	}
	
	/**查义二级代理个人账单
	 * @param oemId
	 * @param start
	 * @param size
	 * @return
	 */
	public List<Oemaccountdetail> qrySecondOemPersonBill(Integer oemId, int start, int size){
		return oemAcDetail.qrySecondOemPersonBill(oemId, start, size);
	}
	
	/**查询二级代理个人账单条目
	 * @param oemId
	 * @return
	 */
	public int qryCountSecondOemPersonBill(Integer oemId){
		return oemAcDetail.qryCountSecondOemPersonBill(oemId);
	}
	
	public int qryCountOemAcDtlsByOemid(Integer oemId){
		return oemAcDetail.qryCountOemAcDtlsByOemid(oemId);
	}
	
	public List<Userrecharge> qryUserRechargesByOemid(Integer oemId, int start, int size){
		List<Userrecharge> lst = userCharge.qryUserRechargesByOemid(oemId, start, size);
		return lst == null ? new ArrayList<Userrecharge>() : lst;
	}
	
	public int qryCountUserRechargesByOemid(Integer oemId){
		return userCharge.qryCountUserRechargesByOemid(oemId);
	}
	
	public List<Userrecharge> qryUserRechargesByUserid(Integer userId, int start, int size){
		List<Userrecharge> lst = userCharge.qryUserRechargesByUserid(userId, start, size);
		return lst == null ? new ArrayList<Userrecharge>() : lst;
	}
	
	public int qryCountUserRechargesByUserid(Integer userId){
		return userCharge.qryCountUserRechargesByUserid(userId);
	}
	
	public List<Oemrecharge> qryOemRechargesByOemid(Integer oemId, int start, int size){
		List<Oemrecharge> lst = oemCharge.qryOemRechargesByOemid(oemId, start, size);
		return lst == null ? new ArrayList<Oemrecharge>() : lst;
	}
	
	public int qryCountOemRechargesByOemid(Integer oemId){
		return oemCharge.qryCountOemRechargesByOemid(oemId);
	}
}
