package com.mediahx.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author ZHE
 *
 */
public class StrUtil {
	/**
	 * 判断字符串是否为空.
	 * @param str		字符串
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * null 替换 为 “”
	 * @param str
	 * @return
	 */
	public static String getStr(String str){
		if(!isEmpty(str)){
			return str;
		}
		return "";
	}
	
	/**
	 * JSON 转换
	 * @param str
	 * @return
	 */
	public static String replacePagerJsonKey(String str){
		if(!CommUtils.isEmpty(str)){
			return str.replace("totalCount","totalRow").replace("pageNum", "pageCurrent");
		}
		return null;
	}
	
	
	/**
	 * 获取图片路径
	 * @param request
	 * @param part
	 * @return
	 */
	public static String getImgPath(HttpServletRequest request,String part){
		String contextPath = request.getContextPath();
		StringBuilder sb = new StringBuilder();
		sb.append(File.separatorChar)
		  .append(contextPath.substring(1))
		  .append(File.separatorChar)
		  .append("upload")
		  .append(File.separatorChar)
		  .append(part);
		
		return sb.toString();
	}
	
	public static Integer strToInt(String str){
		 Integer i = new Integer(0);
		 if(!CommUtils.isEmpty(str)){
			 i = Integer.parseInt(str);
		 }
		 
		 return i;
	}
}
