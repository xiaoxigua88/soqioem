package com.soqi.system.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.common.constants.Constant;
import com.soqi.common.utils.IDUtils;
import com.soqi.oem.dao.UseraccountMapper;
import com.soqi.oem.dao.UseraccountdetailMapper;
import com.soqi.oem.dao.UserrechargeMapper;
import com.soqi.oem.gentry.Useraccount;
import com.soqi.oem.gentry.Useraccountdetail;
import com.soqi.oem.gentry.Userrecharge;

@Service
public class RechargeService {

	@Autowired
	private UserrechargeMapper userCharge;;
	@Autowired
	private UseraccountMapper userAccount;
	@Autowired
	private UseraccountdetailMapper userAcDetail;
	
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
		}
		//总额加上充值金额
		ua.setTotalamount(ua.getTotalamount().add(charge.getAmount()));
		//更新资金表
		userAccount.updateByPrimaryKey(ua);
		
		//明细表
		uadl.setBalance(ua.getTotalamount());
		userAcDetail.insert(uadl);
		
		return userCharge.insert(charge);
	}
}
