package com.mediahx.message;

/**
 * 设置返回对象
 * @author ZHE
 *
 */
public class SetCommRespMsg {
	
	private static CommResp resp = null;

	public SetCommRespMsg() {
		resp = new CommResp(false, "操作失败！", "0");
	}
	
	public SetCommRespMsg(boolean rtn, String rtnMsg, String retCode) {
		resp = new CommResp(rtn, rtnMsg, retCode);
	}

	/***
	 * 默认设置方法
	 * @param rtn
	 * @param rtnMsg
	 * @param retCode
	 * @return
	 */
	public static CommResp defaultSet(boolean rtn, String rtnMsg, String retCode) {
		resp = new CommResp(rtn, rtnMsg, retCode);
		return resp;
	}
	
	public static CommResp defaultSet(CommResp resp, boolean rtn, String rtnMsg, String retCode) {
		resp.setRtn(rtn);
		resp.setRtnMsg(rtnMsg);
		resp.setRetCode(retCode);
		return resp;
	}
}
