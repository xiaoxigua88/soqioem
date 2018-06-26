package com.soqi.common.utils;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**功能：科学计数工具类
 * @author 孙傲
 *
 */
public class BigDecimalUtil {
	private final static Logger logger = LoggerFactory.getLogger(BigDecimalUtil.class);
	/** 
     * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。 
     */  
    private static final int DEF_DIV_SCALE = 10; // 这个类不能实例化  
  
    private BigDecimalUtil() {  
    }  
  
    /** 
     * 提供精确的加法运算。 
     *  
     * @param v1 
     *            被加数 
     * @param v2 
     *            加数 
     * @return 两个参数的和 
     */  
    public static double add(double v1, double v2) {  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        return b1.add(b2).doubleValue();  
    }  
  
    /** 
     * 提供精确的减法运算。 
     *  
     * @param v1 
     *            被减数 
     * @param v2 
     *            减数 
     * @return 两个参数的差 
     */  
    public static double sub(double v1, double v2) {  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        return b1.subtract(b2).doubleValue();  
    }  
  
    /** 
     * 提供精确的乘法运算。 
     *  
     * @param v1 
     *            被乘数 
     * @param v2 
     *            乘数 
     * @return 两个参数的积 
     */  
    public static double mul(double v1, double v2) {  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        return b1.multiply(b2).doubleValue();  
    }  
  
    /** 
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。 
     *  
     * @param v1 
     *            被除数 
     * @param v2 
     *            除数 
     * @return 两个参数的商 
     */  
    public static double div(double v1, double v2) {  
        return div(v1, v2, DEF_DIV_SCALE);  
    }  
  
    /** 
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。 
     *  
     * @param v1 
     *            被除数 
     * @param v2 
     *            除数 
     * @param scale 
     *            表示表示需要精确到小数点以后几位。 
     * @return 两个参数的商 
     */  
    public static double div(double v1, double v2, int scale) {  
        if (scale < 0) {  
            throw new IllegalArgumentException("The scale must be a positive integer or zero");  
        }  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        return b2.divide(b1, scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
    }  
  
    /** 
     * 提供精确的小数位四舍五入处理。 
     *  
     * @param v 
     *            需要四舍五入的数字 
     * @param scale 
     *            小数点后保留几位 
     * @return 四舍五入后的结果 
     */  
    public static double round(double v, int scale) {  
        if (scale < 0) {  
            throw new IllegalArgumentException("The scale must be a positive integer or zero");  
        }  
        BigDecimal b = new BigDecimal(Double.toString(v));  
        BigDecimal one = new BigDecimal("1");  
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
    }  
	
    /**比较两数的大小若前者大于后者则返回true
     * @param v1
     * @param v2
     * @return
     */
    public static boolean compareSize(double v1, double v2){
    	BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        if(b1.compareTo(b2)==1){
        	return true;
        }else{
        	return false;
        }
    }
    
    public static boolean isBigDecimal(String v1){
    	try{
    		BigDecimal b1 = new BigDecimal(Double.valueOf(v1));
    		if(b1 != null){
    			return true;
    		}
    	}catch(NumberFormatException e){
    		logger.debug("字符串转BigDecimal数字失败");
    	}
    	return false;
    }
    
	public static void main(String[] args) {
		System.out.println(isBigDecimal("1.0"));
	}

}
