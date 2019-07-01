package com.mediahx.exception;

/**
 * 平台异常代码
 * 
 * @author ZHE
 *
 */
public class PlatformErrorCode extends HXErrorCode {

	/** 系统错误 */
	public static final PlatformErrorCode ERROR_PARAM_NULL = new PlatformErrorCode("ERROR_111", "{0}参数为空");

	/**
	 * 构造方法
	 * <p>
	 * 
	 * @param errorCode
	 *            错误代码
	 * @param errorMsg
	 *            错误信息
	 */
	public PlatformErrorCode(String errorCode, String errorMsg) {
		super(errorCode, errorMsg);
	}

}