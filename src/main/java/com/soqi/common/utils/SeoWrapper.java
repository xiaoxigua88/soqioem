package com.soqi.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public static Map<String, List<Seo>> singleUroToSeo(Integer userId, String url, String[] keyword, String[] searchType){
		Map<String, List<Seo>> listMap = new HashMap<String, List<Seo>>();
		for(String st : searchType){
			List<Seo> seoList = new ArrayList<Seo>();
			for(String kw : keyword){
				Seo seo = new Seo();
				seo.setUrl(url);
				seo.setKeyword(kw);
				seo.setSearchtype(Integer.valueOf(st));
				seo.setUserid(userId);
				//模拟冻结资产
				seo.setFreezeamount(BigDecimal.ZERO);
				seo.setOemfreezeamount(BigDecimal.ZERO);
				seo.setPrice(BigDecimal.ZERO);
				seo.setAddtime(new Date());
				//设置初始排名和最新排名初始化、客户端给默认值
				seo.setRankfirst(Constant.SEO_CLIENT_RANK_DEFAULT);
				seo.setRanklast(Constant.SEO_CLIENT_RANK_DEFAULT);
				//设置计费标准排名
				seo.setFromrank(Constant.SEO_CLIENT_RANK_FROM);
				seo.setTorank(Constant.SEO_CLIENT_RANK_TO);
				
				seo.setBuytime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//消费时间
				seo.setCosttime(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//结算时间点、取配置
				seo.setSettlehour(10);
				//结算完成时间、结算开始时间
				seo.setSettletime(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				seo.setSettlestart(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//排名更新时间
				seo.setRankupdatetime(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//检测中
				seo.setStatus(Constant.SEO_STATUS_CHECKING);
				//价格部分对像生成
				List<Seoprice> listspr = new ArrayList<Seoprice>();
				Seoprice spr1 = new Seoprice();
				spr1.setFromrank(Constant.SEO_CLIENT_RANK_FROM);
				spr1.setTorank(Constant.SEO_CLIENT_RANK_TO);
				spr1.setPrice(BigDecimal.ZERO);
				spr1.setPriceoem(BigDecimal.ZERO);
				spr1.setPriceori(BigDecimal.ZERO);
				spr1.setPriceoemchild(BigDecimal.ZERO);
				listspr.add(spr1);
				seo.setListspr(listspr);
				
				seoList.add(seo);
			}
			listMap.put(st, seoList);
		}
		
		return listMap;
	}
	/**一对一关键词关键词封装成seo对像
	 * @param url
	 * @param keyword
	 * @param searchType
	 * @return
	 */
	public static Map<String, List<Seo>> multipleUroToSeo(Integer userId, String[] url, String[] keyword, String searchType){
		Map<String, List<Seo>> listMap = new HashMap<String, List<Seo>>();
		List<Seo> seoList = new ArrayList<Seo>();
		for(int i = 0 ; i < keyword.length; i++){
			Seo seo = new Seo();
			if(url.length == 1){
				seo.setUrl(url[0]);
			}else{
				seo.setUrl(url[i]);
			}
			seo.setKeyword(keyword[i]);
			seo.setSearchtype(Integer.valueOf(searchType));
			seo.setUserid(userId);
			//模拟冻结资产
			seo.setFreezeamount(BigDecimal.ZERO);
			seo.setOemfreezeamount(BigDecimal.ZERO);
			seo.setPrice(BigDecimal.ZERO);
			seo.setAddtime(new Date());
			//设置初始排名和最新排名初始化、客户端给默认值
			seo.setRankfirst(Constant.SEO_CLIENT_RANK_DEFAULT);
			seo.setRanklast(Constant.SEO_CLIENT_RANK_DEFAULT);
			//设置计费标准排名
			seo.setFromrank(Constant.SEO_CLIENT_RANK_FROM);
			seo.setTorank(Constant.SEO_CLIENT_RANK_TO);
			seo.setBuytime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
			//消费时间
			seo.setCosttime(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
			//结算时间点、取配置
			seo.setSettlehour(10);
			//结算完成时间、结算开始时间
			seo.setSettletime(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
			seo.setSettlestart(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
			//排名更新时间
			seo.setRankupdatetime(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
			//检测中
			seo.setStatus(Constant.SEO_STATUS_CHECKING);
			//价格部分对像生成
			List<Seoprice> listspr = new ArrayList<Seoprice>();
			Seoprice spr1 = new Seoprice();
			spr1.setFromrank(Constant.SEO_CLIENT_RANK_FROM);
			spr1.setTorank(Constant.SEO_CLIENT_RANK_TO);
			spr1.setPrice(BigDecimal.ZERO);
			spr1.setPriceoem(BigDecimal.ZERO);
			spr1.setPriceori(BigDecimal.ZERO);
			spr1.setPriceoemchild(BigDecimal.ZERO);
			listspr.add(spr1);
			seo.setListspr(listspr);
			seoList.add(seo);
		}
		listMap.put(searchType, seoList);
		return listMap;
	}
	
	/**相同价导入关键词转换
	 * @param req
	 * @param userId
	 * @param url
	 * @param keyword
	 * @param searchType
	 * @return
	 */
	public static Map<String, List<Seo>> bathConvertSameSeo(HttpServletRequest req, Integer userId, String[] url, String[] keyword, String[] searchType){
		Map<String, List<Seo>> listMap = new HashMap<String, List<Seo>>();
		/*String[] kws = keyword.split("\r\n");
		String[] urls = url.split("\r\n");*/
		for (String st : searchType) {
			List<Seo> seoList = new ArrayList<Seo>();
			for(int i = 0; i<keyword.length; i++){
				String torank1_ = req.getParameter("torank1_"+st);
				String price1_ = req.getParameter("price1_"+st);
				String torank2_ = req.getParameter("torank2_"+st);
				String price2_ = req.getParameter("price2_"+st);
				if(torank1_ == null && price1_ == null && torank2_ == null && price2_ == null){
					continue;
				}
				Seo seo = new Seo();
				seo.setUrl(url[i]);
				seo.setKeyword(keyword[i]);
				seo.setSearchtype(Integer.valueOf(st));
				seo.setUserid(userId);
				seo.setAddtime(new Date());
				//设置初始排名和最新排名初始化、代理端给默认值
				seo.setRankfirst(Constant.SEO_CLIENT_RANK_DEFAULT);
				seo.setRanklast(Constant.SEO_OEM_RANK_OUTFIVE);
				seo.setBuytime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//消费时间
				seo.setCosttime(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//结算时间点、取配置
				seo.setSettlehour(10);
				//结算完成时间、结算开始时间
				seo.setSettletime(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				seo.setSettlestart(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//排名更新时间
				seo.setRankupdatetime(new Date());
				//模拟冻结资产
				seo.setFreezeamount(BigDecimal.ZERO);
				seo.setOemfreezeamount(BigDecimal.ZERO);
				//关键词价格
				List<Seoprice> listspr = new ArrayList<Seoprice>();
				Seoprice spr1 = new Seoprice();
				Seoprice spr2 = new Seoprice();
				if(StringUtils.isBlank(torank2_)){
					BigDecimal price1 = new BigDecimal(price1_);
					spr1.setFromrank(1);
					spr1.setTorank(Integer.valueOf(torank1_));
					spr1.setPrice(price1);
					spr1.setPriceoem(BigDecimal.ZERO);
					spr1.setPriceori(BigDecimal.ZERO);
					spr1.setPriceoemchild(BigDecimal.ZERO);
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
					spr1.setPriceoem(BigDecimal.ZERO);
					spr1.setPriceori(BigDecimal.ZERO);
					spr1.setPriceoemchild(BigDecimal.ZERO);
					spr2.setPrice(price2);
					spr2.setPriceoem(BigDecimal.ZERO);
					spr2.setPriceori(BigDecimal.ZERO);
					spr2.setPriceoemchild(BigDecimal.ZERO);
					listspr.add(spr1);
					listspr.add(spr2);
				}
				seo.setListspr(listspr);
				//待付款
				seo.setStatus(Constant.SEO_STATUS_NEEDPAY);
				seoList.add(seo);
			}
			if(!seoList.isEmpty()){
				listMap.put(st, seoList);
			}
		}
		return listMap;
	}
	
	/**不同价导入关键词转换
	 * @param req
	 * @param userId
	 * @param url
	 * @param keyword
	 * @param searchType
	 * @return
	 */
	public static Map<String, List<Seo>> bathConvertDiffSeo(HttpServletRequest req, Integer userId, String[] url, String[] keyword, String[] searchType){
		Map<String, List<Seo>> listMap = new HashMap<String, List<Seo>>();
		for (String st : searchType) {
			List<Seo> seoList = new ArrayList<Seo>();
			for(int i = 0; i<keyword.length; i++){
				String torank_ = req.getParameter("torank_"+st);
				String price_ = req.getParameter("price_"+st);
				if(torank_ == null && price_ == null) {
					continue;
				}
				String[] price = price_.split("\r\n");
				Seo seo = new Seo();
				if(url.length == 1){
					seo.setUrl(url[0]);
				}else{
					seo.setUrl(url[i]);
				}
				seo.setKeyword(keyword[i]);
				seo.setSearchtype(Integer.valueOf(st));
				seo.setUserid(userId);
				seo.setAddtime(new Date());
				seo.setBuytime(DateUtil.parse("3000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//设置初始排名和最新排名初始化、代理端给默认值
				seo.setRankfirst(Constant.SEO_CLIENT_RANK_DEFAULT);
				seo.setRanklast(Constant.SEO_OEM_RANK_OUTFIVE);
				//消费时间
				seo.setCosttime(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//结算时间点、取配置
				seo.setSettlehour(10);
				//结算完成时间、结算开始时间
				seo.setSettletime(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				seo.setSettlestart(DateUtil.parse("2000-01-01 00:00:00", DateUtil.DATE_TIME_FORMAT));
				//排名更新时间
				seo.setRankupdatetime(new Date());
				//模拟冻结资产
				seo.setFreezeamount(BigDecimal.ZERO);
				seo.setOemfreezeamount(BigDecimal.ZERO);
				//关键词价格
				List<Seoprice> listspr = new ArrayList<Seoprice>();
				
				//组装价格表
				Seoprice spr = new Seoprice();
				BigDecimal b = new BigDecimal(price[i]);
				spr.setPrice(b);
				spr.setPriceoem(BigDecimal.ZERO);
				spr.setPriceori(BigDecimal.ZERO);
				spr.setPriceoemchild(BigDecimal.ZERO);
				spr.setFromrank(1);
				spr.setTorank(Integer.valueOf(torank_));
				listspr.add(spr);
			
				seo.setListspr(listspr);
				//待付款
				seo.setStatus(Constant.SEO_STATUS_NEEDPAY);
				seoList.add(seo);
			}
			if(!seoList.isEmpty()){
				listMap.put(st, seoList);
			}
		}
		return listMap;
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
	
	/**把关键词对像集按照搜索引擎归类
	 * @param seos
	 * @return
	 */
	public static Map<String, List<Seo>> convertListToMapBySearchType(List<Seo> seos){
		Map<String, List<Seo>> listMap = new HashMap<String, List<Seo>>();
		for(Seo s : seos){
			String searchType = s.getSearchtype().toString();
			if(!listMap.containsKey(searchType)){
				List<Seo> list = new ArrayList<Seo>();
				list.add(s);
				listMap.put(searchType, list);
			}else{
				listMap.get(searchType).add(s);
			}
		}
		return listMap;
	}
}
