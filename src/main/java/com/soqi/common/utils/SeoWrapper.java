package com.soqi.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import com.soqi.common.constants.Constant;
import com.soqi.oem.gentry.Seo;
import com.soqi.oem.gentry.Seoprice;

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
					seo.setPrice(BigDecimal.ZERO);
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
						seo.setPrice(BigDecimal.ZERO);
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
					seo.setPrice(BigDecimal.ZERO);
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
					seo.setPrice(BigDecimal.ZERO);
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
				seo.setPrice(BigDecimal.ZERO);
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
	
	/**相同价导入关键词转换
	 * @param req
	 * @param userId
	 * @param url
	 * @param keyword
	 * @param searchType
	 * @return
	 */
	public static List<Seo> bathConvertSameSeo(HttpServletRequest req, Integer userId, String url, String keyword, String[] searchType){
		List<Seo> seoList = new ArrayList<Seo>();
		String[] kws = keyword.split("\r\n");
		String[] urls = url.split("\r\n");
		for(int i = 0; i<kws.length; i++){
			for (String st : searchType) {
				String torank1_ = req.getParameter("torank1_"+st);
				String price1_ = req.getParameter("price1_"+st);
				String torank2_ = req.getParameter("torank2_"+st);
				String price2_ = req.getParameter("price2_"+st);
				if(torank1_ == null && price1_ == null && torank2_ == null && price2_ == null){
					continue;
				}
				Seo seo = new Seo();
				seo.setUrl(urls[i]);
				seo.setKeyword(kws[i]);
				seo.setSearchtype(Integer.valueOf(st));
				seo.setUserid(userId);
				seo.setAddtime(new Date());
				seo.setBuytime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//消费时间
				seo.setCosttime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//结算时间点、取配置
				seo.setSettlehour(10);
				//结算完成时间、结算开始时间
				seo.setSettletime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				seo.setSettlestart(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//模拟冻结资产
				seo.setFreezeamount(BigDecimal.ZERO);
				//关键词价格
				List<Seoprice> listspr = new ArrayList<Seoprice>();
				Seoprice spr1 = new Seoprice();
				Seoprice spr2 = new Seoprice();
				if(StringUtils.isBlank(torank2_)){
					BigDecimal price1 = new BigDecimal(price1_);
					spr1.setFromrank(1);
					spr1.setTorank(Integer.valueOf(torank1_));
					spr1.setPrice(price1);
					spr1.setPriceoem(price1);
					spr1.setPriceori(price1);
					listspr.add(spr1);
				}else{
					spr1.setTorank(Integer.valueOf(torank1_));
					spr2.setTorank(Integer.valueOf(torank2_));
					if(Integer.valueOf(torank2_).intValue() > Integer.valueOf(torank1_).intValue()){
						spr1.setFromrank(1);
						spr2.setFromrank(Integer.valueOf(torank1_)+1);
					}else{
						spr2.setFromrank(1);
						spr1.setFromrank(Integer.valueOf(torank2_)+1);
					}
					BigDecimal price1 = new BigDecimal(price1_);
					BigDecimal price2 = new BigDecimal(price2_);
					spr1.setPrice(price1);
					spr1.setPriceoem(price1);
					spr1.setPriceori(price1);
					spr2.setPrice(price2);
					spr2.setPriceoem(price2);
					spr2.setPriceori(price2);
					listspr.add(spr1);
					listspr.add(spr2);
				}
				seo.setListspr(listspr);
				//待付款
				seo.setStatus(Constant.SEO_STATUS_NEEDPAY);
				seoList.add(seo);
			}
		}
		return seoList;
	}
	
	/**不同价导入关键词转换
	 * @param req
	 * @param userId
	 * @param url
	 * @param keyword
	 * @param searchType
	 * @return
	 */
	public static List<Seo> bathConvertDiffSeo(HttpServletRequest req, Integer userId, String url, String keyword, String[] searchType){
		List<Seo> seoList = new ArrayList<Seo>();
		String[] kws = keyword.split("\r\n");
		String[] urls = url.split("\r\n");
		for(int i = 0; i<kws.length; i++){
			for (String st : searchType) {
				String torank_ = req.getParameter("torank_"+st);
				String price_ = req.getParameter("price_"+st);
				if(torank_ == null && price_ == null) {
					continue;
				}
				String[] price = price_.split("\r\n");
				Seo seo = new Seo();
				if(urls.length == 1){
					seo.setUrl(urls[0]);
				}else{
					seo.setUrl(urls[i]);
				}
				seo.setKeyword(kws[i]);
				seo.setSearchtype(Integer.valueOf(st));
				seo.setUserid(userId);
				seo.setAddtime(new Date());
				seo.setBuytime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//消费时间
				seo.setCosttime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//结算时间点、取配置
				seo.setSettlehour(10);
				//结算完成时间、结算开始时间
				seo.setSettletime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				seo.setSettlestart(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//模拟冻结资产
				seo.setFreezeamount(BigDecimal.ZERO);
				//关键词价格
				List<Seoprice> listspr = new ArrayList<Seoprice>();
				
				//组装价格表
				Seoprice spr = new Seoprice();
				BigDecimal b = new BigDecimal(price[i]);
				spr.setPrice(b);
				spr.setPriceoem(b);
				spr.setPriceori(b);
				spr.setFromrank(1);
				spr.setTorank(Integer.valueOf(torank_));
				listspr.add(spr);
			
				seo.setListspr(listspr);
				//待付款
				seo.setStatus(Constant.SEO_STATUS_NEEDPAY);
				seoList.add(seo);
			}
		}
		return seoList;
	}
	
	/**功能：处理查询的seos结果集转换处理
	 * @param seos
	 * @return
	 */
	public static List<Seo> dealWithSeoResult(List<Seo> seos){
		if(null == seos || seos.isEmpty()){
			return new ArrayList<Seo>();
		}
		Date endDate = new Date();
		for (Seo seo : seos) {
			//结算时间点是否更新
			int settle = DateUtil.getIntervalDay(seo.getSettletime(), endDate);
			if(settle != 0){
				//表示数据未结算、结算时间未更新
				seo.setSettlestatus(Constant.SETTLE_STATUS_WAITING);
			}else{
				//day=0表示数据已经结算过、结算时间已更新到当天
				//查看达标消费时间是否是当天的
				int cost = DateUtil.getIntervalDay(seo.getCosttime(), endDate);
				if(cost != 0){
					//未产生扣费时间更新、所以未达标
					seo.setSettlestatus(Constant.SETTLE_STATUS_FAILREACH);
				}else{
					seo.setSettlestatus(Constant.SETTLE_STATUS_HAVEREACH);
				}
			}
			//连续不达标天数达到标准用户可手动停止该会务
			int reach = DateUtil.getIntervalDay(seo.getCosttime(), seo.getSettletime());
			if(reach >= Constant.SEOFAILEDDAY){
				seo.setCanstop(true);
			}
		}
		return seos;
	}
}
