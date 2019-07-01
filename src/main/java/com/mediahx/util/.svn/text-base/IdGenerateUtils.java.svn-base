package com.mediahx.util;

/**
 * UUID生成工具
 * 
 * @author ZHE
 *
 */
public class IdGenerateUtils {

	private static ThreadLocal<CommUtils> UUID = new ThreadLocal<CommUtils>();

	public static String getUUID() {
		CommUtils instance = UUID.get();
		String str = null;
		if (instance == null) {
			str = CommUtils.getUUID();
			UUID.set(instance);
		}
		return str;
	}

	public static void main(String[] args) {
		System.out.println(IdGenerateUtils.getUUID());
	}
}
