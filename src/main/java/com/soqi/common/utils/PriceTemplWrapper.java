package com.soqi.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

import com.soqi.oem.gentry.Pricetempl;
import com.soqi.oem.gentry.Pricetempldtail;

public class PriceTemplWrapper {
	
	public static Pricetempl converParamToPricetempl(String[] searchType,HttpServletRequest req, Integer oemid, String[] pricetemplid){
		Pricetempl pt = new Pricetempl();
		pt.setOemid(oemid);
		pt.setTemplname("顶级代理"+oemid+"手动配置下级代理模板");
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
}
