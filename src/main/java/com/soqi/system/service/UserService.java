package com.soqi.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.common.utils.MD5Utils;
import com.soqi.common.utils.RandomUtil;
import com.soqi.oem.dao.OemuserMapper;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemuser;
@Service
public class UserService {
	@Autowired
	private OemuserMapper ou;
	/*@Transactional("primaryTransactionManager")
	public int insert1(Student st){
		System.out.println("第一组user");
		return sm.insert(st);
	}
	@Transactional("primaryTransactionManager")
	public int insert2(Student st){
		System.out.println("第二组user");
		return sm.insert(st);
	}*/
	
	/**
	 * @param oemid 代理商ID号
	 * @param start 查询起始点
	 * @param size 查询步长
	 * @return
	 */
	public List<Oemuser> qryUsersByOemId(Integer oemid, int start, int size){
		return ou.selectByOemId(oemid, start, size);
	}
	
	public int selectCountByOemId(Integer oemid){
		return ou.selectCountByOemId(oemid);
	}
	
	public int updateOemuser(Oemuser oemuser){
		return ou.updateByPrimaryKey(oemuser);
	}
	@Transactional("primaryTransactionManager")
	public int saveOemuser(Oemuser oemuser){
		return ou.insert(oemuser);
	}
	
	public boolean hasSameMobileOfOEM(Integer oemid,String mobile,Integer userid){
		Oemuser oemuser = ou.selectOemuserByMobileAndOemid(oemid, mobile, userid);
		return oemuser == null ? true : false;
	}
	
	public Oemuser qryOemuser(Integer userid){
		return ou.selectByPrimaryKey(userid);
	}
	
	/**密码重置
	 * @param customer
	 * @return
	 */
	public String InitPwd(Oemuser oemuser){
		//int num = RandomUtil.getNotSimple(RandomUtil.SEEDARR, 6);
		String pwd = MD5Utils.encrypt(oemuser.getMobile(),"12345678");
		Oemuser moduser = new Oemuser();
		moduser.setPwd(pwd);
		moduser.setUserid(oemuser.getUserid());
		int count = ou.updateByPrimaryKey(moduser);
		return count >0 ? "12345678" : null;
	}
}
