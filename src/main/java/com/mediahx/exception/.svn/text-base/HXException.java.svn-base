package com.mediahx.exception;

import java.text.MessageFormat;

/**
 * 异常处理基类
 * @author ZHE
 *
 */
public class HXException extends RuntimeException {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private boolean rtn=false;
	
	private String message;

	/** 错误代码 */
	private String errorCode;

	/** 错误代码 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * 构造方法.
	 * 
	 * @param errorCode
	 *            错误代码
	 * @param message
	 *            错误信息
	 * @param cause
	 *            异常
	 */
	public HXException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}
	
	public HXException(boolean rtn,String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		this.message=message;
		this.rtn=rtn;
	}

	/**
	 * 构造方法.
	 * 
	 * @param errorCode
	 *            错误代码
	 * @param message
	 *            错误信息
	 */
	public HXException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * 构造方法.
	 * 
	 * @param errorCode
	 *            错误代码
	 */
	public HXException(HXErrorCode errorCode) {
		this(errorCode.getErrorCode(), errorCode.getErrorMsg());
	}

	/**
	 * 构造方法.
	 * 
	 * @param errorCode
	 *            错误代码
	 */
	public HXException(HXErrorCode errorCode, Throwable throwable) {
		this(errorCode.getErrorCode(), errorCode.getErrorMsg(), throwable);
	}

	/**
	 * 构造方法.
	 * 
	 * @param errorCode
	 *            错误代码
	 * @param args
	 *            参数
	 */
	public HXException(HXErrorCode errorCode, Object[] args) {
		this(errorCode.getErrorCode(), MessageFormat.format(
				errorCode.getErrorMsg(), args));
	}

	public boolean isRtn() {
		return rtn;
	}

	public void setRtn(boolean rtn) {
		this.rtn = rtn;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
