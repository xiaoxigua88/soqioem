package com.soqi.common.constants;

/**
 * @author 孙傲
 *
 */
public class Constant {

	/**
	 * 直客用户
	 */
	public static final String USERTYPE_USER = "1";
	/**
	 * 代理用户
	 */
	public static final String USERTYPE_OEM = "2";

	public static final int NO_PASSWORD = 2;

	public static final int HAS_PASSWORD = 1;

	/**
	 * 支付宝
	 */
	public static final int REHARGE_ALIPAY = 1;
	
	/**
	 * 微信
	 */
	public static final int REHARGE_WEIXIN = 2;
	
	/**
	 * 银行卡
	 */
	public static final int REHARGE_BANKCARD = 3;
	
	/**
	 * 现金
	 */
	public static final int REHARGE_CASH = 4;
	
	//充值金额
	public static final int TRADE_TYPE_CHARGE = 10;
	
	//购买优币
	public static final int TRADE_TYPE_GOLD = 19;
	
	//人工扣除
	public static final int TRADE_TYPE_CUTMANMADE = 21;
	
	//人工退款
	public static final int TRADE_TYPE_RETURNMANMADE = 22;
	
	//云排名购买
	public static final int TRADE_TYPE_SEOBUY = 31;
	
	//云排名消费
	public static final int TRADE_TYPE_SEOPAY = 32;
	
	//云排名停上
	public static final int TRADE_TYPE_SEOSTOP = 33;
	
	//云建站购买
	public static final int TRADE_TYPE_YUNSITBUY = 41;
	
	//云建站SEO购买
	public static final int TRADE_TYPE_YUNSIT_SEOBUY = 42;
	
	//云建站SEO消费
	public static final int TRADE_TYPE_YUNSIT_SEOPAY = 43;
	
	//云建站SEO停止
	public static final int TRADE_TYPE_YUNSIT_SEOSTOP = 45;
	
}
