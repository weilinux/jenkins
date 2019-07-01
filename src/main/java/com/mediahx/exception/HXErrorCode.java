package com.mediahx.exception;
/**
 * 错误代码.
 * @author ZHE
 *
 */
public class HXErrorCode {

    /** 入参错误 */
    public static final HXErrorCode ERROR_ARGUMENT    = new HXErrorCode("ERROR_001", "入参错误");

    /** 系统错误 */
    public static final HXErrorCode ERROR_SYSTEM      = new HXErrorCode("ERROR_999", "系统错误");

    /** 调用后台服务异常 */
    public static final HXErrorCode ERROR_INVOKE_FAIL = new HXErrorCode("ERROR_998", "调用后台服务异常");

    /** 调用后台服务通信超时 */
    public static final HXErrorCode ERROR_TIME_OUT    = new HXErrorCode("ERROR_997", "调用后台通信超时");

    /** 错误代码 */
    private String                    errorCode;

    /** 错误信息 */
    private String                    errorMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public HXErrorCode() {
        super();
    }

    /**
     * 构造方法
     * <p>
     * 
     * @param errorCode 错误代码
     * @param errorMsg 错误信息
     */
    public HXErrorCode(String errorCode, String errorMsg) {
        this();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}