package com.soqi.apido;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.apido.gentry.KeyPrice;
import com.soqi.common.constants.Constant;
import com.soqi.common.utils.DateUtil;
import com.soqi.oem.dao.SeoMapper;
import com.soqi.oem.dao.SeopriceMapper;
import com.soqi.oem.gentry.Seo;
import com.soqi.oem.gentry.Seoprice;

/**数据平台回调服务、数据校验、数据更新等
 * @author 孙傲
 *
 */
@Service
public class DataPlatformCallbackService {
	private final Logger logger = LoggerFactory.getLogger(DataPlatformCallbackService.class);
	@Autowired
	private SeoMapper seoMapper;
	@Autowired
	private SeopriceMapper piceMapper;
	/**关键词排名数据更新
	 * @param seo
	 */
	public void seoRankUpdateByPlatformId(Seo seo){
		//校验该数据是否已经更新过
		List<Seo> lstseo = seoMapper.selectByApiTaskId(seo.getApiranktaskid(), null, seo.getApiwatchtaskid());
		for(Seo s : lstseo){
			int daycount = DateUtil.getIntervalDay(seo.getRankupdatetime(), s.getRankupdatetime());
			if(daycount == 0){
				logger.debug("关键词排名实时查本地数据已是最新无需再更新");
				return;
			}
		}
		logger.debug("关键词排名更新本地排名数据开始");
		seoMapper.seoRankUpdateByPlatformId(seo);
		logger.debug("关键词排名更新本地排名数据结束");
	}
	
	/**关键词价格实时查数据更新
	 * @param kp
	 */
	@Transactional("primaryTransactionManager")
	public void seoPriceUpdatePlatFormId(KeyPrice kp){
		List<Seo> lstseo = seoMapper.selectByApiTaskId(null, kp.getTaskid(), null);
		List<Seoprice> splst = new ArrayList<Seoprice>();
		List<Integer> ids = new ArrayList<Integer>();
		for(Seo s : lstseo){
			ids.add(s.getTaskid().intValue());
			Seoprice sp = new Seoprice();
			sp.setTaskid(s.getTaskid());
			sp.setFromrank(1);
			sp.setTorank(10);
			switch (s.getSearchtype().intValue()) {
			case Constant.SEARCH_TYPE_360_PC:
				sp.setPriceori(kp.getPricesopc());
				//后面两个价格需要计算
				sp.setPrice(kp.getPricesopc());
				sp.setPriceoem(kp.getPricesopc());
				break;
			case Constant.SEARCH_TYPE_360_WAP:
				sp.setPriceori(kp.getPricesowap());
				//后面两个价格需要计算
				sp.setPrice(kp.getPricesowap());
				sp.setPriceoem(kp.getPricesowap());
				break;
			case Constant.SEARCH_TYPE_BAIDU_PC:
				sp.setPriceori(kp.getPricebaidupc());
				//后面两个价格需要计算
				sp.setPrice(kp.getPricebaidupc());
				sp.setPriceoem(kp.getPricebaidupc());
				break;
			case Constant.SEARCH_TYPE_BAIDU_WAP:
				sp.setPriceori(kp.getPricebaiduwap());
				//后面两个价格需要计算
				sp.setPrice(kp.getPricebaiduwap());
				sp.setPriceoem(kp.getPricebaiduwap());
				break;
			case Constant.SEARCH_TYPE_SOGOU_PC:
				sp.setPriceori(kp.getPricesogoupc());
				//后面两个价格需要计算
				sp.setPrice(kp.getPricesogoupc());
				sp.setPriceoem(kp.getPricesogoupc());
				break;
			case Constant.SEARCH_TYPE_SOGOU_WAP:
				sp.setPriceori(kp.getPricesogouwap());
				//后面两个价格需要计算
				sp.setPrice(kp.getPricesogouwap());
				sp.setPriceoem(kp.getPricesogouwap());
				break;
			case Constant.SEARCH_TYPE_SM_WAP:
				sp.setPriceori(kp.getPricesm());
				//后面两个价格需要计算
				sp.setPrice(kp.getPricesm());
				sp.setPriceoem(kp.getPricesm());
				break;
			default:
				break;
			}
			splst.add(sp);
		}
		if(!splst.isEmpty()){
			piceMapper.batchUpdateFormSeoPrice(splst);
			Integer[] taskids = ids.toArray(new Integer[ids.size()]);
			seoMapper.batchSeoFieldsByTaskids(taskids, Constant.SEO_STATUS_NEEDPAY, null, null);
		}
	}
}
