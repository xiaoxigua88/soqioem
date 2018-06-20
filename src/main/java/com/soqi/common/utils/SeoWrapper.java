package com.soqi.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.soqi.common.constants.Constant;
import com.soqi.oem.gentry.Seo;

/**seo包装类
 * @author 孙傲
 *
 */
public class SeoWrapper {
	
	/**单一网址对多关键词封装成seo对像
	 * @param url
	 * @param keyword
	 * @param searchType
	 * @return
	 */
	public static List<Seo> singleUroToSeo(Integer userId, String url, String keyword, String[] searchType){
		List<Seo> seoList = new ArrayList<Seo>();
		if(keyword.contains("\r\n")){
			for(String kw : keyword.split("\r\n")){
				for(String st : searchType){
					Seo seo = new Seo();
					seo.setUrl(url);
					seo.setKeyword(kw);
					seo.setSearchtype(Integer.valueOf(st));
					seo.setUserid(userId);
					//模拟冻结资产
					seo.setFreezeamount(BigDecimal.ZERO);
					seo.setPrice(BigDecimal.valueOf(20.12));
					seo.setPriceori(BigDecimal.valueOf(10.26));
					seo.setPriceoem(BigDecimal.valueOf(8.00));
					seo.setAddtime(new Date());
					seo.setBuytime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
					//消费时间
					seo.setCosttime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
					//结算时间点、取配置
					seo.setSettlehour(10);
					//结算完成时间、结算开始时间
					seo.setSettletime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
					seo.setSettlestart(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
					//检测中
					seo.setStatus(Constant.SEO_STATUS_CHECKING);
					seoList.add(seo);
				}
			}
		}
		return seoList;
	}
	/**一对一关键词关键词封装成seo对像
	 * @param url
	 * @param keyword
	 * @param searchType
	 * @return
	 */
	public static List<Seo> multipleUroToSeo(Integer userId, String url, String keyword, String searchType){
		List<Seo> seoList = new ArrayList<Seo>();
		if(keyword.contains("\r\n")){
			for(String kw : keyword.split("\r\n")){
				if(url.contains("\r\n")){
					for(String u : url.split("\r\n")){
						Seo seo = new Seo();
						seo.setUrl(u);
						seo.setKeyword(kw);
						seo.setSearchtype(Integer.valueOf(searchType));
						seo.setUserid(userId);
						//模拟冻结资产
						seo.setFreezeamount(BigDecimal.ZERO);
						seo.setPrice(BigDecimal.valueOf(20.12));
						seo.setPriceori(BigDecimal.valueOf(10.26));
						seo.setPriceoem(BigDecimal.valueOf(8.00));
						seo.setAddtime(new Date());
						seo.setBuytime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
						//消费时间
						seo.setCosttime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
						//结算时间点、取配置
						seo.setSettlehour(10);
						//结算完成时间、结算开始时间
						seo.setSettletime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
						seo.setSettlestart(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
						//检测中
						seo.setStatus(Constant.SEO_STATUS_CHECKING);
						seoList.add(seo);
					}
				}else{
					Seo seo = new Seo();
					seo.setUrl(url);
					seo.setKeyword(kw);
					seo.setSearchtype(Integer.valueOf(searchType));
					seo.setUserid(userId);
					//模拟冻结资产
					seo.setFreezeamount(BigDecimal.ZERO);
					seo.setPrice(BigDecimal.valueOf(20.12));
					seo.setPriceori(BigDecimal.valueOf(10.26));
					seo.setPriceoem(BigDecimal.valueOf(8.00));
					seo.setAddtime(new Date());
					seo.setBuytime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
					//消费时间
					seo.setCosttime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
					//结算时间点、取配置
					seo.setSettlehour(10);
					//结算完成时间、结算开始时间
					seo.setSettletime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
					seo.setSettlestart(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
					//检测中
					seo.setStatus(Constant.SEO_STATUS_CHECKING);
					seoList.add(seo);
				}
			}
		}else{
			if(url.contains("\r\n")){
				for(String u : url.split("\r\n")){
					Seo seo = new Seo();
					seo.setUrl(u);
					seo.setKeyword(keyword);
					seo.setSearchtype(Integer.valueOf(searchType));
					seo.setUserid(userId);
					//模拟冻结资产
					seo.setFreezeamount(BigDecimal.ZERO);
					seo.setPrice(BigDecimal.valueOf(20.12));
					seo.setPriceori(BigDecimal.valueOf(10.26));
					seo.setPriceoem(BigDecimal.valueOf(8.00));
					seo.setAddtime(new Date());
					seo.setBuytime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
					//消费时间
					seo.setCosttime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
					//结算时间点、取配置
					seo.setSettlehour(10);
					//结算完成时间、结算开始时间
					seo.setSettletime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
					seo.setSettlestart(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
					//检测中
					seo.setStatus(Constant.SEO_STATUS_CHECKING);
					seoList.add(seo);
				}
			}else{
				Seo seo = new Seo();
				seo.setUrl(url);
				seo.setKeyword(keyword);
				seo.setSearchtype(Integer.valueOf(searchType));
				seo.setUserid(userId);
				//模拟冻结资产
				seo.setFreezeamount(BigDecimal.ZERO);
				seo.setPrice(BigDecimal.valueOf(20.12));
				seo.setPriceori(BigDecimal.valueOf(10.26));
				seo.setPriceoem(BigDecimal.valueOf(8.00));
				seo.setAddtime(new Date());
				seo.setBuytime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//消费时间
				seo.setCosttime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//结算时间点、取配置
				seo.setSettlehour(10);
				//结算完成时间、结算开始时间
				seo.setSettletime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				seo.setSettlestart(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//检测中
				seo.setStatus(Constant.SEO_STATUS_CHECKING);
				seoList.add(seo);
			}
		}
		return seoList;
	}
}
