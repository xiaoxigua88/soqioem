package com.soqi.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.soqi.common.constants.Constant;
import com.soqi.oem.gentry.Apipricechange;
import com.soqi.oem.gentry.Apipricechangedetail;
import com.soqi.oem.gentry.Oempricetempl;
import com.soqi.oem.gentry.Pricetempl;
import com.soqi.oem.gentry.Pricetempldtail;
import com.soqi.oem.gentry.Userpricetempl;

public class PriceTemplWrapper {
	
	public static Pricetempl converParamToPricetempl(Byte templtype, String[] searchType,HttpServletRequest req, Integer oemid, String[] pricetemplid){
		Pricetempl pt = new Pricetempl();
		pt.setOemid(oemid);
		if(Constant.PRICETEMPL_OEM.intValue() == templtype.intValue()){
			pt.setTemplname("顶级代理"+oemid+"手动配置下级代理价格模板");
		}else if(Constant.PRICETEMPL_USER.intValue() == templtype.intValue()){
			pt.setTemplname("顶级代理"+oemid+"手动配置直客价格模板");
		}
		pt.setTempltype(templtype);
		List<Pricetempldtail> ptdlst = new ArrayList<Pricetempldtail>();
		for (String st : searchType) {
			Pricetempldtail pdt = new Pricetempldtail();
			if(null != pricetemplid){
				pdt.setPricetemplid(Integer.valueOf(pricetemplid[0]));
			}
			String discounttype_ = req.getParameter("discounttype_"+st);
			String fixprice_ = req.getParameter("fixprice_"+st);
			String minprice_ = req.getParameter("minprice_"+st);
			String maxprice_ = req.getParameter("maxprice_"+st);
			String ratio_ = req.getParameter("ratio_"+st);
			pdt.setDiscounttype(Byte.valueOf(discounttype_));
			pdt.setSearchtype(Short.valueOf(st));
			if(StringUtils.equals(discounttype_, "1")){
				//一口价
				pdt.setFixprice(new BigDecimal(fixprice_));
			}else if(StringUtils.equals(discounttype_, "2")){
				//折扣价、最小、最大、比率
				pdt.setMinprice(new BigDecimal(minprice_));
				pdt.setMaxprice(new BigDecimal(maxprice_));
				pdt.setRatio(new BigDecimal(ratio_));
			}
			ptdlst.add(pdt);
		}
		pt.setPtdsList(ptdlst);
		return pt;
	}
	
	public static void convertParamToApiPriceChange(Byte groupLength,HttpServletRequest req,Apipricechange tosave, Integer oemid){
		tosave.setOemid(oemid);
		List<Apipricechangedetail> adcdList = new ArrayList<Apipricechangedetail>();
		for(int i = 1 ; i<groupLength+1 ; i++){
			Apipricechangedetail detail = new Apipricechangedetail();
			if(null != tosave.getId()){
				detail.setId(tosave.getId());
			}
			String pricefirst_ = req.getParameter("pricefirst_"+i);
			String pricelast_ = req.getParameter("pricelast_"+i);
			String range_ = req.getParameter("range_"+i);
			detail.setPricefirst(new BigDecimal(pricefirst_));
			detail.setPricelast(new BigDecimal(pricelast_));
			detail.setRange(new BigDecimal(range_));
			adcdList.add(detail);
		}
		tosave.setAcdList(adcdList);
	}
	
	public static List<Oempricetempl> convertOempricetempls(String[] searchType, HttpServletRequest req, Integer oemid){
		List<Oempricetempl> optlst = new ArrayList<Oempricetempl>();
		for (String st : searchType) {
			Oempricetempl opt = new Oempricetempl();
			opt.setOemid(oemid);
			String discounttype_ = req.getParameter("discounttype_"+st);
			String fixprice_ = req.getParameter("fixprice_"+st);
			String minprice_ = req.getParameter("minprice_"+st);
			String maxprice_ = req.getParameter("maxprice_"+st);
			String ratio_ = req.getParameter("ratio_"+st);
			opt.setDiscounttype(Byte.valueOf(discounttype_));
			opt.setSearchtype(Short.valueOf(st));
			if(StringUtils.equals(discounttype_, "1")){
				//一口价
				opt.setFixprice(new BigDecimal(fixprice_));
			}else if(StringUtils.equals(discounttype_, "2")){
				//折扣价、最小、最大、比率
				opt.setMinprice(new BigDecimal(minprice_));
				opt.setMaxprice(new BigDecimal(maxprice_));
				opt.setRatio(new BigDecimal(ratio_));
			}
			optlst.add(opt);
		}
		return optlst;
	}
	
	public static List<Userpricetempl> convertUserpricetempls(String[] searchType, HttpServletRequest req, Integer userid){
		List<Userpricetempl> uptlst = new ArrayList<Userpricetempl>();
		for (String st : searchType) {
			Userpricetempl upt = new Userpricetempl();
			upt.setUserid(userid);
			String discounttype_ = req.getParameter("discounttype_"+st);
			String fixprice_ = req.getParameter("fixprice_"+st);
			String minprice_ = req.getParameter("minprice_"+st);
			String maxprice_ = req.getParameter("maxprice_"+st);
			String ratio_ = req.getParameter("ratio_"+st);
			upt.setDiscounttype(Byte.valueOf(discounttype_));
			upt.setSearchtype(Short.valueOf(st));
			if(StringUtils.equals(discounttype_, "1")){
				//一口价
				upt.setFixprice(new BigDecimal(fixprice_));
			}else if(StringUtils.equals(discounttype_, "2")){
				//折扣价、最小、最大、比率
				upt.setMinprice(new BigDecimal(minprice_));
				upt.setMaxprice(new BigDecimal(maxprice_));
				upt.setRatio(new BigDecimal(ratio_));
			}
			uptlst.add(upt);
		}
		return uptlst;
	}
}
