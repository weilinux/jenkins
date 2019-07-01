package com.mediahx.util;

import java.util.Random;

/**
 * 
 * @author ZHE
 *
 */
public class ImageCode {
	static char[] codeSequence = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	private static int codeCount = 4;

	public static String getCode() {
		StringBuffer randomCode = new StringBuffer();

		for (int i = 0; i < codeCount; i++) {
			// 创建一个随机数生成器类
			Random random = new Random();
			String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			randomCode.append(strRand);
		}

		return randomCode.toString();
	}

	public static void main(String[] args) {
		System.out.println(getCode());
	}
}
