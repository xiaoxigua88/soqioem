package com.soqi.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.common.utils.MD5Utils;
import com.soqi.oem.dao.CustomerMapper;
import com.soqi.oem.dao.CustomerroleMapper;
import com.soqi.oem.dao.OembaseMapper;
import com.soqi.oem.dao.OemserviceconfigMapper;
import com.soqi.oem.dao.RoleMapper;
import com.soqi.oem.dao.WebtemplateMapper;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Customerrole;
import com.soqi.oem.gentry.Oembase;
import com.soqi.oem.gentry.Oemserviceconfig;
import com.soqi.oem.gentry.Role;
import com.soqi.oem.gentry.Webtemplate;
@Service
public class OemBaseService {
	@Autowired
	private WebtemplateMapper web;
	@Autowired
	private OembaseMapper oembaseMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private CustomerroleMapper customerroleMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private OemserviceconfigMapper oscMapper;
	
	public Webtemplate selectTemplateByDomain(String domain){
		return web.selectByDomain(domain);
	}
	
	/**根据当前代理系统的代理编号查询子代理列表
	 * @param oemid
	 * @param start
	 * @param size
	 * @return
	 */
	public List<Oembase> qryChildOemsByOemId(Integer oemid, int start, int size){
		return oembaseMapper.qryChildOemsByOemId(oemid, start, size);
	}
	
	/**功能：根据当前代理系统的代理编号查询子代理数目
	 * @param oemid
	 * @return
	 */
	public int qryCountChildOemsByOemId(Integer oemid){
		return oembaseMapper.qryCountChildOemsByOemId(oemid);
	}
	
	private void boundData(Oembase source, Oembase target){
		target.setMealid(source.getMealid());
		target.setTempid(source.getTempid());
		target.setExpirytime(source.getExpirytime());
		target.setOemlevel(1);
		target.setStatus(source.getStatus());
		target.setMealstatus(source.getMealstatus());
		target.setHasnextoem(0);
		target.setTotaltaskcount(0);
		target.setTotaluserscount(0);
	}
	
	public Oembase qryOembaseInfo(Integer oemid){
		return oembaseMapper.selectByPrimaryKey(oemid);
	}
	
	@Transactional("primaryTransactionManager")
	public void saveOemBase(Customer customer, Oembase oembase){
		//查询当前的系统的oembase
		Oembase currentBase = oembaseMapper.selectByPrimaryKey(oembase.getOemid());
		boundData(currentBase, oembase);
		//保存Oembase数据、数据表中主键自增此处数据插入成功会返回自增主键到oembase中
		if(StringUtils.isBlank(oembase.getPhone())){
			oembase.setPhone(customer.getMobile());
		}
		oembaseMapper.insert(oembase);
		//保存customer代理员工数据
		customer.setOemid(oembase.getOemid());
		customer.setPwd(MD5Utils.encrypt(customer.getMobile(),customer.getPwd()));
		customer.setStatus(1);
		customerMapper.insert(customer);
		
		//如果parentoemid不为0复制parentoemid的角色
		if(oembase.getParentoemid().intValue()!=0){
			//根据parentid和IsOemManager两个字段找到代理所属的管理员角色 
			Customerrole customerrole =customerroleMapper.selectSingleOemRelation(oembase.getParentoemid(), 1);
			Role role = roleMapper.selectByPrimaryKey(customerrole.getRoleid(), oembase.getParentoemid());
			if(customerrole != null && role != null){
				//替换成当前需要生成的角色和角色关系
				role.setAddtime(new Date());
				role.setOemid(oembase.getOemid());
				roleMapper.insert(role);
				customerrole.setCustomerid(customer.getCustomerid());
				customerroleMapper.insert(customerrole);
			}
			//二级代理添加时默认继承上级代理所属服务
			List<Oemserviceconfig> oscList = oscMapper.selectListByOemid(oembase.getParentoemid());
			if(null != oscList && !oscList.isEmpty()){
				for(Oemserviceconfig osc : oscList){
					osc.setOemid(oembase.getOemid());
				}
			}
			oscMapper.batchInsert(oscList);
		}else{
			//顶级代理、复制一份默认服务
			List<Oemserviceconfig> oscList = oscMapper.selectAll();
			if(null != oscList && !oscList.isEmpty()){
				for(Oemserviceconfig osc : oscList){
					osc.setOemid(oembase.getOemid());
				}
			}
			oscMapper.batchInsert(oscList);
		}
	}
	
	public int childoemUpdate(Oembase oembase){
		return oembaseMapper.updateByPrimaryKey(oembase);
	}
	
}
