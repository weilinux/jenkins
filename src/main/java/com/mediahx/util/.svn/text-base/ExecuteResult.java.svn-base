package com.mediahx.util;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * BJUI结果处理类
 * @author ZHE
 *
 */
public class ExecuteResult {
	/**
	 * 处理成功
	 */
	public static final int SUCCESS_CODE = 200;
	/**
	 * 处理失败
	 */
	public static final int FAILURE_CODE = 300;
	
	/**
	 * 
	 * @param statusCode 200 操作成功； 300 操作失败
	 * @return
	 */
	public static String jsonReturn(int statusCode) {
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		if(statusCode == 200) {
			jsonObj.put("statusCode", "200");
			jsonObj.put("message", "操作成功");
			jsonObj.put("filename", "zzz");
		} else if (statusCode == 300) {
			jsonObj.put("statusCode", "300");
			jsonObj.put("message", "操作失败，请重试");
		}
		jsonObj.put("closeCurrent", true);
		return JSON.toJSONString(jsonObj);
	}
	
	public static String jsonReturn(int statusCode, boolean closeCurrent) {
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		if(statusCode == 200) {
			jsonObj.put("statusCode", "200");
			jsonObj.put("message", "操作成功");
		} else if (statusCode == 300) {
			jsonObj.put("statusCode", "300");
			jsonObj.put("message", "操作失败，请重试");
		}
		jsonObj.put("closeCurrent", closeCurrent);
		return JSON.toJSONString(jsonObj);
	}
	
	/**
	 * 自定义返回消息
	 * @param statusCode
	 * @param msg
	 * @return
	 */
	public static String jsonReturn(int statusCode, String msg) {
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		if(statusCode == 200) {
			jsonObj.put("statusCode", "200");
			jsonObj.put("message", msg);
		} else if (statusCode == 300) {
			jsonObj.put("statusCode", "300");
			jsonObj.put("message", msg);
		}
		jsonObj.put("closeCurrent", true);
		
		return JSON.toJSONString(jsonObj);
	}
	
	public static String jsonReturn(int statusCode, String msg, boolean closeCurrent) {
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		if(statusCode == 200) {
			jsonObj.put("statusCode", "200");
			jsonObj.put("message", "操作成功 " + msg);
		} else if (statusCode == 300) {
			jsonObj.put("statusCode", "300");
			jsonObj.put("message", "操作失败:" + msg);
		}
		jsonObj.put("closeCurrent", closeCurrent);
		return JSON.toJSONString(jsonObj);
	}
	
	public static String uploadPic(int statusCode, String msg,String url){
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		if(statusCode == 200) {
			jsonObj.put("error", 0);
		} else if (statusCode == 300) {
			jsonObj.put("error", 1);
			jsonObj.put("message", "操作失败:" + msg);
		}
		jsonObj.put("url", url);
		return JSON.toJSONString(jsonObj);
	}
	public static void printJson(String str,PrintWriter out){
		out.print(str);
		str=null;
	}
	
	public static void printJson(int statusCode,PrintWriter out){
		out.print(ExecuteResult.jsonReturn(statusCode));
	}
	
	public static void printJson(int statusCode,boolean closeCurrent,PrintWriter out){
		out.print(ExecuteResult.jsonReturn(statusCode,closeCurrent));
	}
	
	public static void printJson(int statusCode,String msg,PrintWriter out){
		out.print(ExecuteResult.jsonReturn(statusCode,msg));
	}
	
	public static void printJson(int statusCode,String msg,boolean closeCurrent,PrintWriter out){
		out.print(ExecuteResult.jsonReturn(statusCode,closeCurrent));
	}
	
}
