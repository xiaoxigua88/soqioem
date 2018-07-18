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
import com.soqi.oem.dao.OempricetemplMapper;
import com.soqi.oem.dao.OemuserMapper;
import com.soqi.oem.dao.PricetemplMapper;
import com.soqi.oem.dao.PricetempldtailMapper;
import com.soqi.oem.dao.UserpricetemplMapper;
import com.soqi.oem.gentry.Oembase;
import com.soqi.oem.gentry.Oempricetempl;
import com.soqi.oem.gentry.Oemuser;
import com.soqi.oem.gentry.Pricetempl;
import com.soqi.oem.gentry.Pricetempldtail;
import com.soqi.oem.gentry.Seoprice;
import com.soqi.oem.gentry.Userpricetempl;

@Service
public class PriceTemplService {
	
	private final Logger logger = LoggerFactory.getLogger(PriceTemplService.class);
	
	@Autowired
	private ApiPriceChangeService apcService;
	@Autowired
	private PricetemplMapper price;
	@Autowired
	private PricetempldtailMapper priceDetail;
	@Autowired
	private OemuserMapper ou;
	@Autowired
	private OembaseMapper oembaseMapper;
	@Autowired
	private OempricetemplMapper oempricetemplMapper;
	@Autowired
	private UserpricetemplMapper userpricetemplMapper;
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
	
	public void calculatesSeoPriceByPriceTempl(Seoprice seoprice, BigDecimal apiPrice, Integer searchType, Integer userid){
		//根据关键词客户的userid获取上级代理，再判断上级代理有没有上级代理
		Oemuser user = ou.selectByPrimaryKey(userid);
		Oembase currentOem = oembaseMapper.selectByPrimaryKey(user.getOemid());
		//原始接口价格调整
		BigDecimal oemPrice = apcService.apiPriceChange(currentOem, apiPrice);
		if(currentOem.getParentoemid().intValue() == 0){
			//该顶代的直客价格即为接口调整之后的价格oemPrice、获取该直客所用价格模板，如果没有则使用接口调价后的价格、如果有则在该调价基础上按模板进行计算
			List<Userpricetempl> uptList = userpricetemplMapper.qryUserpricetemplsByUserid(userid, searchType);
			if(null == uptList || uptList.isEmpty()){
				//客户没有配置调价模板、即该关键词现价等于代理接口之后的市价oemPrice
				seoprice.setPrice(oemPrice);
			}else{
				Userpricetempl upt = uptList.get(0);
				if(upt.getDiscounttype().intValue()==Constant.DISCOUNT_FIX_PRICE){
					//直客一口价
					seoprice.setPrice(upt.getFixprice());
				}else if(upt.getDiscounttype().intValue() == Constant.DISCOUNT_RATIO_PRICE){
					double a = oemPrice.multiply(upt.getRatio()).doubleValue();
					double b = upt.getMinprice().doubleValue();
					double c = upt.getMaxprice().doubleValue();
					if(b <= a && a <= c){
						seoprice.setPrice(oemPrice.multiply(upt.getRatio()));
					}else if(a < b){
						seoprice.setPrice(upt.getMinprice());
					}else if(c < a){
						seoprice.setPrice(upt.getMaxprice());
					}
				}
			}
			seoprice.setPriceori(apiPrice);
			seoprice.setPriceoem(oemPrice);
			seoprice.setPriceoemchild(BigDecimal.ZERO);
		}else{
			//可知当前代理为二级代理、要获取到厅顶代给二级代的模板、然后二级代给直客的模板
			//parentOem = oembaseMapper.selectByPrimaryKey(currentOem.getParentoemid());
			//获取二级代理个性化的价格模板
			List<Oempricetempl> optList = oempricetemplMapper.qryOempricetemplsByOemid(currentOem.getOemid(), searchType);
			//获取二级代下的直客价格模板
			List<Userpricetempl> uptList = userpricetemplMapper.qryUserpricetemplsByUserid(userid, searchType);
			//接口原始价
			seoprice.setPriceori(apiPrice);
			//系统调整后的市价
			seoprice.setPriceoem(oemPrice);
			if(null == optList || optList.isEmpty()){
				//如果顶代没有给二级代配的话、二级代的价格直接取自市价
				seoprice.setPriceoemchild(oemPrice);
			}else{
				//顶代给二级代配的有模板
				Oempricetempl opt = optList.get(0);
				if(opt.getDiscounttype().intValue() == Constant.DISCOUNT_FIX_PRICE){
					//一口价给代理
					seoprice.setPriceoemchild(opt.getFixprice());
				}else if(opt.getDiscounttype().intValue() == Constant.DISCOUNT_RATIO_PRICE){
					double a = oemPrice.multiply(opt.getRatio()).doubleValue();
					double b = opt.getMinprice().doubleValue();
					double c = opt.getMaxprice().doubleValue();
					if(b <= a && a <= c){
						seoprice.setPriceoemchild(oemPrice.multiply(opt.getRatio()));
					}else if(a < b){
						seoprice.setPriceoemchild(opt.getMinprice());
					}else if(c < a){
						seoprice.setPriceoemchild(opt.getMaxprice());
					}
				}
			}
			
			if(null == uptList || uptList.isEmpty()){
				//二级代理下的直客本身没有进行个性化的价格配置、则去获取二级代理统一给客户进行的价格模板去获取
				List<Pricetempldtail> childPdtail = priceDetail.selectListByConditions(currentOem.getOemid(), Constant.PRICETEMPL_USER, searchType);
				if(null == childPdtail || childPdtail.isEmpty()){
					//即没有直客个性化价格配置、又没有二级代模板配置、则向上继承自市场价
					seoprice.setPrice(oemPrice);
				}else{
					Pricetempldtail pdtail = childPdtail.get(0);
					if(pdtail.getDiscounttype().intValue()==Constant.DISCOUNT_FIX_PRICE){
						//顶代的客户一口价、顶代价和原价一样、由于不存在二级代，二级代字段可以设置为0
						seoprice.setPrice(pdtail.getFixprice());
					}else if(pdtail.getDiscounttype().intValue() == Constant.DISCOUNT_RATIO_PRICE){
						double a = oemPrice.multiply(pdtail.getRatio()).doubleValue();
						double b = pdtail.getMinprice().doubleValue();
						double c = pdtail.getMaxprice().doubleValue();
						if(b <= a && a <= c){
							seoprice.setPrice(oemPrice.multiply(pdtail.getRatio()));
						}else if(a < b){
							seoprice.setPrice(pdtail.getMinprice());
						}else if(c < a){
							seoprice.setPrice(pdtail.getMaxprice());
						}
					}
				}
			}else{
				//二级代理有进行个性化的价格配置优先进行个性化的价格配置
				Userpricetempl upt = uptList.get(0);
				if(upt.getDiscounttype().intValue()==Constant.DISCOUNT_FIX_PRICE){
					//直客一口价
					seoprice.setPrice(upt.getFixprice());
				}else if(upt.getDiscounttype().intValue() == Constant.DISCOUNT_RATIO_PRICE){
					double a = oemPrice.multiply(upt.getRatio()).doubleValue();
					double b = upt.getMinprice().doubleValue();
					double c = upt.getMaxprice().doubleValue();
					if(b <= a && a <= c){
						seoprice.setPrice(oemPrice.multiply(upt.getRatio()));
					}else if(a < b){
						seoprice.setPrice(upt.getMinprice());
					}else if(c < a){
						seoprice.setPrice(upt.getMaxprice());
					}
				}
			}
		}
	}
	
	/**根据策略模板计算关键词的原始价格、顶级代理看到的策略价格、顶级代理级二级代理设置的价格、直客价格\二级代理下的直客价格
	 * @param seoprice
	 * @param apiPrice 注此处的价格是接口出来价格，需要先进行一次系统调价方能给顶代
	 * @param searchType
	 */
	public void calculatesSeoPriceByPriceTempl1(Seoprice seoprice, BigDecimal apiPrice, Integer searchType, Integer userid){
		//根据关键词客户的userid获取上级代理，再判断上级代理有没有上级代理
		Oemuser user = ou.selectByPrimaryKey(userid);
		Oembase currentOem = oembaseMapper.selectByPrimaryKey(user.getOemid());
		//原始接口价格调整
		BigDecimal oemPrice = apcService.apiPriceChange(currentOem, apiPrice);
		double piceOri = apiPrice.doubleValue();
		apiPrice = oemPrice;
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
			seoprice.setPriceori(BigDecimal.valueOf(piceOri));
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
			seoprice.setPriceori(BigDecimal.valueOf(piceOri));
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
	
	
   /***************************************************************************以下为具体到每一个代理、用户的调整部分**************************************************/
	public List<Oempricetempl> qryOempricetemplsByOemid(Integer oemid){
		return this.oempricetemplMapper.qryOempricetemplsByOemid(oemid, null);
	}
	
	public int batchInsertOemPriceTempls(List<Oempricetempl> records){
		return this.oempricetemplMapper.batchInsert(records);
	}
	
	public int batchUpdateOemPriceTempls(List<Oempricetempl> records){
		return this.oempricetemplMapper.batchUpdate(records);
	}
	
	public List<Userpricetempl> qryUserpricetemplsByUserid(Integer userid){
		return this.userpricetemplMapper.qryUserpricetemplsByUserid(userid, null);
	}
	public int batchInsertUserPriceTempls(List<Userpricetempl> records){
		return this.userpricetemplMapper.batchInsert(records);
	}
	
	public int batchUpdateUserPriceTempls(List<Userpricetempl> records){
		return this.userpricetemplMapper.batchUpdate(records);
	}
}
