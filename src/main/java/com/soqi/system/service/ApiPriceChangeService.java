package com.soqi.system.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.common.exception.BDException;
import com.soqi.oem.dao.ApipricechangeMapper;
import com.soqi.oem.dao.ApipricechangedetailMapper;
import com.soqi.oem.gentry.Apipricechange;
import com.soqi.oem.gentry.Apipricechangedetail;
import com.soqi.oem.gentry.Oembase;

/**
 * @author 孙傲
 * 系统价格调价
 */
@Service
public class ApiPriceChangeService {
	private final Logger logger = LoggerFactory.getLogger(ApiPriceChangeService.class);
	@Autowired
	private ApipricechangeMapper apcm;
	@Autowired
	private ApipricechangedetailMapper apcdm;
	//根据当前关键词所属代理获取代理的调价模板、从而对出厂价进行调整 
	public BigDecimal apiPriceChange(Oembase seoOem,  BigDecimal apiPrice){
		Apipricechange apc = getApipricechange(seoOem.getParentoemid().intValue() == 0 ? seoOem.getOemid() : seoOem.getParentoemid());
		BigDecimal changePrice = BigDecimal.ZERO;
		if(apc == null || apc.getAcdList()==null || apc.getAcdList().isEmpty()){
			//取不到代理调价则直接返回原始价格
			return apiPrice;
		}
		for(int i = 0; i<apc.getAcdList().size(); i++){
			Apipricechangedetail detail = apc.getAcdList().get(i);
			BigDecimal oneRange = detail.getRange().add(BigDecimal.ONE);
			if(apc.getAcdList().size()-1==i){
				//（原始价格-起始价格）*（1+浮动比率）
				BigDecimal endPartPrice = apiPrice.subtract(detail.getPricefirst()).multiply(oneRange);
				changePrice = changePrice.add(endPartPrice);
			}else{
				//计算前段区间段的价格调整：（区间止价格-区间起价格）*（1+浮动比率）
				BigDecimal partPrice = detail.getPricelast().subtract(detail.getPricefirst()).multiply(oneRange);
				changePrice = changePrice.add(partPrice);
			}
		}
		if(changePrice.doubleValue() < apc.getMinprice().doubleValue()){
			return apc.getMinprice();
		}else if(changePrice.doubleValue() > apc.getMaxprice().doubleValue()){
			return apc.getMaxprice();
		}
		return changePrice;
	}
	
	/**根据代理编号获取顶代接口调价列表
	 * @param oemid
	 */
	public Apipricechange getApipricechange(Integer oemid){
		Apipricechange apc = apcm.selectByOemid(oemid);
		if(apc == null){
			return null;
		}
		//根据id区取价格区间详情
		List<Apipricechangedetail> apcdList = apcdm.selectListById(apc.getId());
		apc.setAcdList(apcdList);
		return apc;
	}
	
	@Transactional("primaryTransactionManager")
	public void saveApipricechange(Apipricechange apc){
		apcm.insert(apc);
		if(apc.getId() == null){
			logger.debug("添加接口价格调整未返回主键、数据回滚");
			throw new BDException("添加接口价格调整未返回主键、数据回滚");
		}
		for(Apipricechangedetail d : apc.getAcdList()){
			d.setId(apc.getId());
		}
		apcdm.batchInsert(apc.getAcdList());
	}
	
	@Transactional("primaryTransactionManager")
	public void updateApipricechange(Apipricechange apc){
		//更新主表
		apcm.updateByPrimaryKey(apc);
		apcdm.batchDelete(apc.getId());
		for(Apipricechangedetail d : apc.getAcdList()){
			d.setId(apc.getId());
		}
		apcdm.batchInsert(apc.getAcdList());
	}
	
}
