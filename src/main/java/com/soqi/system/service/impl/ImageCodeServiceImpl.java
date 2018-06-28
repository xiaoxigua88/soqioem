package com.soqi.system.service.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.jhlabs.image.RippleFilter;
import com.soqi.common.utils.CookieUtils;
import com.soqi.common.utils.EncrypDES;
import com.soqi.system.service.IImageCodeService;


/**
 * 功能说明: 图片验证码<br>
 * 系统版本: v1.0<br>
 * 开发人员: @author kongdy<br>
 * 开发时间: 2015年8月3日<br>
 */
@Service
public class ImageCodeServiceImpl implements IImageCodeService {

	private final Logger logger = LoggerFactory.getLogger(ImageCodeServiceImpl.class);
	
	@Autowired
	private ApplicationContext context;

	// 图片的宽度
	private final static int IMAGEWIDTH = 20;
	// 图片的高度
	private final static int IMAGEHEIGHT = 35;

	// 字体大小
	private final static int FONTSIZE = 22;

	// 字符串长度
	private final static int CODE_LENGTH = 4;

	// 随机字符范围
	private final static char[] CHAR_RANGE = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', '2', '3', '4', '5', '6', '7', '8', '9'};

	// 随机字母范围
	private final static char[] CHARACTER_RANGE = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y'};

	// 随机数字范围
	private final static char[] NUMBER_RANGE = {'2', '3', '4', '5', '6', '7', '8', '9'};

	// 字体
	private static Font font;
	// {3,10,3,20,10,1} {5,5,5,8,10,1}
	private final static int[] FILTER_ARGS_1 = {3, 4, 3, 20, 10, 1};
	private final static int[] FILTER_ARGS_2 = {5, 4, 5, 8, 10, 1};
	private static RippleFilter rippleFilter = new RippleFilter();
	private static RippleFilter rippleFilter1 = new RippleFilter();
	static {
		try {
			rippleFilter.setWaveType(FILTER_ARGS_1[0]);
			rippleFilter.setXAmplitude(FILTER_ARGS_1[1]);
			rippleFilter.setYAmplitude(FILTER_ARGS_1[2]);
			rippleFilter.setXWavelength(FILTER_ARGS_1[3]);
			rippleFilter.setYWavelength(FILTER_ARGS_1[4]);
			rippleFilter.setEdgeAction(FILTER_ARGS_1[5]);
			rippleFilter1.setWaveType(FILTER_ARGS_2[0]);
			rippleFilter1.setXAmplitude(FILTER_ARGS_2[1]);
			rippleFilter1.setYAmplitude(FILTER_ARGS_2[2]);
			rippleFilter1.setXWavelength(FILTER_ARGS_2[3]);
			rippleFilter1.setYWavelength(FILTER_ARGS_2[4]);
			rippleFilter1.setEdgeAction(FILTER_ARGS_2[5]);
			String holidayPath = ImageCodeServiceImpl.class.getClassLoader().getResource("swisse_0.ttf").getPath();
			File file = new File(holidayPath);
			java.io.FileInputStream fi;
			fi = new java.io.FileInputStream(file);
			java.io.BufferedInputStream fb = new java.io.BufferedInputStream(fi);
			Font nf = Font.createFont(Font.TRUETYPE_FONT, file);
			nf = nf.deriveFont(Font.PLAIN, FONTSIZE);
			font = nf;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Random random = new Random();

	@Override
	public BufferedImage createVerifyCodeImage(String verifyCode) {

		// 生成画布
		BufferedImage image = new BufferedImage(IMAGEWIDTH * CODE_LENGTH, IMAGEHEIGHT, BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文 （生成画笔）
		Graphics graphics = image.getGraphics();

		// 设置背景色（）
		graphics.setColor(new Color(255, 255, 255));
		// 填充矩形区域 ，作为背景
		graphics.fillRect(0, 0, IMAGEWIDTH * CODE_LENGTH, IMAGEHEIGHT);
		// 画出边框
		graphics.drawRect(0, 0, IMAGEWIDTH * CODE_LENGTH, IMAGEHEIGHT);
		// 画字符串
		graphics.setColor(new Color(50, 50, 50));
		for (int i = 0; i < CODE_LENGTH; i++) {
			String temp = verifyCode.substring(i, i + 1);
			graphics.setFont(font);
			graphics.setColor(getRandomColor(1));
			graphics.drawString(temp, 18 * i + 6, 28);
		}
		// 波纹扭曲{3 10 3 20 10 1} {1 2 2 10 10 1}{2 2 2 10 10 1}{5 5 5 10 10 1}
		RippleFilter rippleFilter = getRippleFilter();
		BufferedImage b = rippleFilter.filter(image, null);
		graphics.drawImage(b, 0, 0, null);
		// 产生80个干扰点
		graphics.setColor(new Color(100, 100, 100));
		for (int i = 0; i < 80; i++) {
			graphics.setColor(getRandomColor(2));
			int x = random.nextInt(IMAGEWIDTH * CODE_LENGTH);
			int y = random.nextInt(IMAGEHEIGHT);
			graphics.drawOval(x, y, 1, 1);
		}
		// 设置边框
		graphics.setColor(new Color(0xCF, 0xCE, 0xCA));
		((Graphics2D)graphics).setStroke(new BasicStroke(1.0f));
		graphics.drawRect(0, 0, IMAGEWIDTH * CODE_LENGTH - 1, IMAGEHEIGHT - 1);
		graphics.dispose();

		return image;
	}

	/**
	 * 生成随机字符串
	 * @return 随机字符串
	 */
	public String getRandString() {

		// edit by luojw 2014.09.01
		// 后台登陆页面验证码根据配置显示数字或字母
		StringBuilder sb = new StringBuilder();
		char[] randomNum = new char[] {};
		randomNum = NUMBER_RANGE;
		/*String validateCodeType = PropertiesUtils.get("validateCode.type", "number");

		if ("number".equalsIgnoreCase(validateCodeType)) {// 数字
			randomNum = NUMBER_RANGE;
		} else {// 字母
			randomNum = CHARACTER_RANGE;
		}*/

		for (int i = 0; i < CODE_LENGTH; i++) {
			sb.append(randomNum[random.nextInt(randomNum.length)]);
		}
		return sb.toString();
	}

	/**
	 * 生成随即颜色
	 * @param type 1：返回字体颜色 2:返回噪点颜色
	 * @return
	 */
	private static Color getRandomColor(int type) {
		Color color = null;
		if (type == 1) {
			Random random = new Random();
			int i = random.nextInt(4);
			// 单个文字颜色（随机4种）
			// 黑：000000
			// 褐：d6a971
			// 蓝：66c3d6
			// 灰：aaaaaa
			Color[] colors = {new Color(0, 0, 0), new Color(0xd6, 0xa9, 0x71), new Color(0x66, 0xc3, 0xd6), new Color(0xaa, 0xaa, 0xaa)};
			color = colors[i];
		} else if (type == 2) {
			Random random = new Random();
			int i = random.nextInt(3);
			// 噪点颜色：3
			// 3c3c3c
			// ff8e49
			// 6dd781
			Color[] colors = {new Color(0x3c, 0x3c, 0x3c), new Color(0xff, 0x8e, 0x49), new Color(0x6d, 0xd7, 0x81)};
			color = colors[i];
		} else {
			color = new Color(255, 255, 255);
		}
		return color;
	}

	// 得到波纹扭曲处理器
	private static RippleFilter getRippleFilter() {
		// 波纹扭曲{3 10 3 20 10 1} {1 2 2 10 10 1}{2 2 2 10 10 1}{5 5 5 10 10 1}
		Random random = new Random();
		int i = random.nextInt(2);
		if (i == 0)
			return rippleFilter;
		else
			return rippleFilter1;
	}

	/**
	 * 生成的随机验证码缓存入redis，3分钟有效
	 */
	@Override
	public void saveValidateCode(String verifyCode, String sessionId) {
		/*if (SpringContext.useConfigCache(context)) {
			RedisClientUtil.set(StringUtils.upperCase(sessionId), verifyCode, 180);
		} else {
			RequestUtil.getRequest().getSession().setAttribute(StringUtils.upperCase(sessionId), verifyCode);
		}*/
	}

	/**
	 * 验证输入的验证码与缓存是否一致
	 */
	@Override
	public boolean checkVerifycode(String verifyCode, String captchaCode) {
		//装redis后请用如下注释代码部分
		/*String cachedCode = null;
		if (SpringContext.useConfigCache(context)) {
			cachedCode = RedisClientUtil.get(StringUtils.upperCase(sessionId));
		} else {
			cachedCode = (String)RequestUtil.getRequest().getSession().getAttribute(StringUtils.upperCase(sessionId));
		}
		//删除缓存中的验证码
		RedisClientUtil.del(StringUtils.upperCase(sessionId));
		return StringUtils.equalsIgnoreCase(verifyCode, cachedCode);*/
		try {
			captchaCode = EncrypDES.decryption(captchaCode, EncrypDES.DES_SECRETKEY);
			if(StringUtils.equals(verifyCode, captchaCode)){
				return true;
			}
		} catch (Exception e) {
			logger.debug("验证码验证出现异常请查找原因verifyCode:" + verifyCode);
			return false;
		}
		
		return false;
	}

}
