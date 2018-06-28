package com.soqi.system.service;

import java.awt.image.BufferedImage;


/**
 * 功能说明: 图片验证码<br>
 * 系统版本: v1.0<br>
 * 开发人员: @author kongdy<br>
 * 开发时间: 2015年8月3日<br>
 */
public interface IImageCodeService {

	/**
	 * 根据验证码生成图片
	 * @param verifyCode
	 * @return
	 */
	public BufferedImage createVerifyCodeImage(String verifyCode);
	
	/**
	 * 生成随机字符串
	 * @return 随机字符串
	 */
	public String getRandString();

	/**
	 * 缓存生成的随机验证码
	 * @param verifyCode
	 * @param sessionId
	 */
	public void saveValidateCode(String verifyCode, String sessionId);

	/**
	 * 验证填写的验证码是否正确
	 * @param verifyCode
	 * @param sessionId
	 * @return
	 */
	public boolean checkVerifycode(String verifyCode, String captchaCode);
}
