package com.soqi.system.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.common.exception.BDException;
import com.soqi.oem.dao.PricetemplMapper;
import com.soqi.oem.dao.PricetempldtailMapper;
import com.soqi.oem.gentry.Pricetempl;
import com.soqi.oem.gentry.Pricetempldtail;

@Service
public class PriceTemplService {
	
	private final Logger logger = LoggerFactory.getLogger(PriceTemplService.class);
	
	@Autowired
	private PricetemplMapper price;
	@Autowired
	private PricetempldtailMapper priceDetail;
	//系统超级管理员给顶代添加价格策略模板
	public void addPriceTemplToOemBySys(){
		
	}
	
	//顶代给其下面的二级代理设置价格策略模板
	@Transactional("primaryTransactionManager")
	public void addPriceTemplToChildOemByOem(Pricetempl pricetempl,List<Pricetempldtail> ptdsList){
		price.insert(pricetempl);
		if(pricetempl.getPricetemplid() == null){
			logger.debug("添加价格模板未返回主键、数据回滚");
			throw new BDException("添加价格模板未返回主键、数据回滚");
		}
		for(Pricetempldtail dt : ptdsList){
			dt.setPricetemplid(pricetempl.getPricetemplid());
		}
		priceDetail.batchInsert(ptdsList);
	}
	
	//顶代给其下面的二级代理设置价格策略模板更新
	@Transactional("primaryTransactionManager")
	public void updatePriceTemplToChildOemByOem(Pricetempl pricetempl,List<Pricetempldtail> ptdsList){
		priceDetail.batchUpdate(ptdsList);
	}
	
	//顶代给其下面的二级代理设置价格策略模板查询
	@Transactional("primaryTransactionManager")
	public List<Pricetempldtail> qryPriceTemplToChildOemByOem(Integer oemid){
		return priceDetail.selectListByOemid(oemid);
	}
	
	//二级代理给其客户重新调价
	public void addPriceTemplToUserByChildOem(){
		
	}
}
