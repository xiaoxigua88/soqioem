package com.soqi.system.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.common.constants.Constant;
import com.soqi.common.exception.BDException;
import com.soqi.oem.dao.OembaseMapper;
import com.soqi.oem.dao.OemuserMapper;
import com.soqi.oem.dao.PricetemplMapper;
import com.soqi.oem.dao.PricetempldtailMapper;
import com.soqi.oem.gentry.Oembase;
import com.soqi.oem.gentry.Oemuser;
import com.soqi.oem.gentry.Pricetempl;
import com.soqi.oem.gentry.Pricetempldtail;
import com.soqi.oem.gentry.Seoprice;

@Service
public class PriceTemplService {
	
	private final Logger logger = LoggerFactory.getLogger(PriceTemplService.class);
	
	@Autowired
	private PricetemplMapper price;
	@Autowired
	private PricetempldtailMapper priceDetail;
	@Autowired
	private OemuserMapper ou;
	@Autowired
	private OembaseMapper oembaseMapper;
	
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
	public List<Pricetempldtail> qryPriceTempl(Integer oemid, Byte templtype){
		return priceDetail.selectListByConditions(oemid, templtype, null);
	}
	
	
	//二级代理给其客户重新调价
	public void addPriceTemplToUserByChildOem(){
		
	}
	
	/**根据策略模板计算关键词的原始价格、顶级代理看到的策略价格、顶级代理级二级代理设置的价格、直客价格\二级代理下的直客价格
	 * @param seoprice
	 * @param apiPrice 注此处的价格是接口出来价格，需要先进行一次系统调价方能给顶代
	 * @param searchType
	 */
	public void calculatesSeoPriceByPriceTempl(Seoprice seoprice, BigDecimal apiPrice, Integer searchType, Integer userid){
		//根据关键词客户的userid获取上级代理，再判断上级代理有没有上级代理
		Oemuser user = ou.selectByPrimaryKey(userid);
		Oembase currentOem = oembaseMapper.selectByPrimaryKey(user.getOemid());
		Oembase parentOem = null;
		if(currentOem.getParentoemid().intValue() == 0){
			//可知当前代理为顶代客户为其直客、获取顶代给直客的模板
			List<Pricetempldtail> pdtlst = priceDetail.selectListByConditions(currentOem.getOemid(), Constant.PRICETEMPL_USER, searchType);
			if(null == pdtlst || pdtlst.isEmpty()){
				//没有配置价格模板的自动继承上级的价格,即：现价、原始价、顶代价 都一样，由于不存在二级代理价，则该价格为0
				seoprice.setPrice(apiPrice);
			}else{
				Pricetempldtail pdtail = pdtlst.get(0);
				if(pdtail.getDiscounttype().intValue()==Constant.DISCOUNT_FIX_PRICE){
					//顶代的客户一口价、顶代价和原价一样、由于不存在二级代，二级代字段可以设置为0
					seoprice.setPrice(pdtail.getFixprice());
				}else if(pdtail.getDiscounttype().intValue() == Constant.DISCOUNT_RATIO_PRICE){
					double a = apiPrice.multiply(pdtail.getRatio()).doubleValue();
					double b = pdtail.getMinprice().doubleValue();
					double c = pdtail.getMaxprice().doubleValue();
					if(b <= a && a <= c){
						seoprice.setPrice(apiPrice.multiply(pdtail.getRatio()));
					}else if(a < b){
						seoprice.setPrice(pdtail.getMinprice());
					}else if(c < a){
						seoprice.setPrice(pdtail.getMaxprice());
					}
				}
			}
			seoprice.setPriceori(apiPrice);
			seoprice.setPriceoem(apiPrice);
			seoprice.setPriceoemchild(BigDecimal.ZERO);
		}else{
			//可知当前代理为二级代理、要获取到厅顶代给二级代的模板、然后二级代给直客的模板
			parentOem = oembaseMapper.selectByPrimaryKey(currentOem.getParentoemid());
			//获取顶代给二级代的模板
			List<Pricetempldtail> parentPdtlst = priceDetail.selectListByConditions(parentOem.getOemid(), Constant.PRICETEMPL_OEM, searchType);
			//获取二级代给直客的模板
			List<Pricetempldtail> childPdtail = priceDetail.selectListByConditions(currentOem.getOemid(), Constant.PRICETEMPL_USER, searchType);
			seoprice.setPriceoem(apiPrice);
			seoprice.setPriceori(apiPrice);
			if(null == parentPdtlst || parentPdtlst.isEmpty()){
				//顶代没有配的话二级代理的代理价直接取自、顶代所获取的价格
				seoprice.setPriceoemchild(apiPrice);
			}else{
				//顶代给二级代配的有模板
				Pricetempldtail parPdtail = parentPdtlst.get(0);
				if(parPdtail.getDiscounttype().intValue() == Constant.DISCOUNT_FIX_PRICE){
					//一口价给代理
					seoprice.setPriceoemchild(parPdtail.getFixprice());
				}else if(parPdtail.getDiscounttype().intValue() == Constant.DISCOUNT_RATIO_PRICE){
					double a = apiPrice.multiply(parPdtail.getRatio()).doubleValue();
					double b = parPdtail.getMinprice().doubleValue();
					double c = parPdtail.getMaxprice().doubleValue();
					if(b <= a && a <= c){
						seoprice.setPriceoemchild(apiPrice.multiply(parPdtail.getRatio()));
					}else if(a < b){
						seoprice.setPriceoemchild(parPdtail.getMinprice());
					}else if(c < a){
						seoprice.setPriceoemchild(parPdtail.getMaxprice());
					}
				}
			}
			
			//二级代理下的直客
			if(null == childPdtail || childPdtail.isEmpty()){
				//没有配置价格模板的自动继承上级的价格,即：现价、原始价、顶代价 都一样，由于不存在二级代理价，则该价格为0
				seoprice.setPrice(apiPrice);
			}else{
				Pricetempldtail pdtail = childPdtail.get(0);
				if(pdtail.getDiscounttype().intValue()==Constant.DISCOUNT_FIX_PRICE){
					//顶代的客户一口价、顶代价和原价一样、由于不存在二级代，二级代字段可以设置为0
					seoprice.setPrice(pdtail.getFixprice());
				}else if(pdtail.getDiscounttype().intValue() == Constant.DISCOUNT_RATIO_PRICE){
					double a = apiPrice.multiply(pdtail.getRatio()).doubleValue();
					double b = pdtail.getMinprice().doubleValue();
					double c = pdtail.getMaxprice().doubleValue();
					if(b <= a && a <= c){
						seoprice.setPrice(apiPrice.multiply(pdtail.getRatio()));
					}else if(a < b){
						seoprice.setPrice(pdtail.getMinprice());
					}else if(c < a){
						seoprice.setPrice(pdtail.getMaxprice());
					}
				}
			}
			
		}
	}
}
