package com.soqi.common.utils;


/**
 * 工具：流水号生成工具
 * 
 * @author 孙傲
 *
 */
public class IDUtils {

	private static byte[] lock = new byte[0];

	// 位数，默认是6位
	private final static long w = 1000000;

	public static String createID() {
		long r = 0;
		synchronized (lock) {
			r = (long) ((Math.random() + 1) * w);
		}

		return System.currentTimeMillis() + String.valueOf(r).substring(1);
	}

	public static void main(String[] args) {
		String id = IDUtils.createID();
		System.out.println(id);
		System.out.println(Long.valueOf(id));
	}
}
