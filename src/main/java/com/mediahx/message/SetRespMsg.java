package com.mediahx.message;

/**
 * 设置返回对象
 * @author ZHE
 *
 */
public class SetRespMsg {
	
	private static Resp resp = null;

	public SetRespMsg() {
		resp = new Resp(false, "操作失败！", "0");
	}
	
	public SetRespMsg(boolean rtn, String rtnMsg, String retCode) {
		resp = new Resp(rtn, rtnMsg, retCode);
	}

	/***
	 * 默认设置方法
	 * @param rtn
	 * @param rtnMsg
	 * @param retCode
	 * @return
	 */
	public static Resp defaultSet(boolean rtn, String rtnMsg, String retCode) {
		resp = new Resp(rtn, rtnMsg, retCode);
		return resp;
	}
}
