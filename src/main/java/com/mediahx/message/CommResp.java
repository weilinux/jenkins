package com.mediahx.message;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.mediahx.bean.PageInfo;
import com.mediahx.util.AESCipher;
import com.mediahx.util.CommUtils;

/**
 * APP 端接口
 * 
 * @author ZHE
 *
 */
public class CommResp extends PageInfo {

	private static final long serialVersionUID = 1L;
	
	private static DecimalFormat decimalFormat = new DecimalFormat("0.00");

	/** 返回true或false */
	private boolean rtn = true;

	/** 返回信息 */
	private String rtnMsg;

	/** 总记录数 */
	private int count = 0;

	/** 记录集合 */
	private List<?> data;

	/** 单个对象 */
	private Object object;
	/**
	 * 返回码
	 */
	private String retCode;
	/**
	 * 返回描述信息
	 */
	private String retDesc;
	/**
	 * 版本
	 */
	private String version = "1.01";
	/**
	 * 应用ID
	 */
	private String appId;
	/**
	 * 终端
	 */
	private String terminal;
	/**
	 * 序号
	 */
	private String serNo;
	
	/**
     * 扩展参数
     */
    private Map<String,Object> ext;
    
    

	public Map<String, Object> getExt() {
		return ext;
	}

	public void setExt(Map<String, Object> ext) {
		this.ext = ext;
	}

	public CommResp() {
	}

	public CommResp(boolean rtn, String rtnMsg, String retCode) {
		super();
		this.rtn = rtn;
		this.rtnMsg = rtnMsg;
		this.retCode = retCode;
	}

	public CommResp(boolean rtn, String rtnMsg) {
		this.rtn = rtn;
		this.rtnMsg = rtnMsg;
	}

	public boolean getRtn() {
		return rtn;
	}

	public void setRtn(boolean rtn) {
		this.rtn = rtn;
	}

	public String getRtnMsg() {
		return rtnMsg;
	}

	public void setRtnMsg(String rtnMsg) {
		this.rtnMsg = rtnMsg;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetDesc() {
		return retDesc;
	}

	public void setRetDesc(String retDesc) {
		this.retDesc = retDesc;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getSerNo() {
		return serNo;
	}

	public void setSerNo(String serNo) {
		this.serNo = serNo;
	}

	public static void printJson(CommResp resp,PrintWriter out){
		out.print(JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.DisableCircularReferenceDetect));
		resp=null;
	}
	

	@SuppressWarnings("rawtypes")
	public static void printStrJson(CommResp resp,PrintWriter out){
		if(resp.getData()==null){
			resp.setData(new ArrayList());
		}
		if(CommUtils.isObjEmpty(resp.getObject())){
			resp.setObject(new Object());
		}
		if(CommUtils.isObjEmpty(resp.getExt())){
			Map<String, Object> ext = new HashMap<String, Object>();
			
			ext.put("sysdate", CommUtils.getCurrDateTime(new Date()));
			resp.setExt(ext);
		}else{
			resp.getExt().put("sysdate", CommUtils.getCurrDateTime(new Date()));
		}
		
		SerializeFilter filter = new ValueFilter(){
			@Override
			public Object process(Object object, String name, Object value) {
				if(value==null){
						/*if(value instanceof Number){
								if(value instanceof Double || value instanceof Float){
									return "0.00";
								}else{
									return "0";
								}
						}*/
						
						return "";
						
				}else{
						/*if (value instanceof Number) {
							if (value instanceof Double || value instanceof Float) {
								return decimalFormat.format(value);
							}else{
								return value+"";
							}
						}*/
					if (value instanceof Number) {
						if (value instanceof Double || value instanceof Float) {
							if(Double.parseDouble(value+"") % 1.0 == 0){
								return String.valueOf((long)Double.parseDouble(value+""));
							}
						}else{
							return value+"";
						}
					}
					if(!CommUtils.isEmpty(name) && name.equals("content") && value.toString().length()>300){
						value = value.toString().substring(0, 300);
					}
						
						return value;
				}
				
			}
			  
		  };
		out.print(JSON.toJSONString(resp,filter,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.DisableCircularReferenceDetect));
		resp=null;
	}
	@SuppressWarnings("rawtypes")
	public static void printStrJson2(CommResp resp,PrintWriter out){
		if(resp.getData()==null){
			resp.setData(new ArrayList());
		}
		if(CommUtils.isObjEmpty(resp.getObject())){
			resp.setObject(new Object());
		}
		if(CommUtils.isObjEmpty(resp.getExt())){
			Map<String, Object> ext = new HashMap<String, Object>();
			
			ext.put("sysdate", CommUtils.getCurrDateTime(new Date()));
			resp.setExt(ext);
		}else{
			resp.getExt().put("sysdate", CommUtils.getCurrDateTime(new Date()));
		}
		
		SerializeFilter filter = new ValueFilter(){
			@Override
			public Object process(Object object, String name, Object value) {
				if(value==null){
						/*if(value instanceof Number){
								if(value instanceof Double || value instanceof Float){
									return "0.00";
								}else{
									return "0";
								}
						}*/
						
						return "";
						
				}else{
						/*if (value instanceof Number) {
							if (value instanceof Double || value instanceof Float) {
								return decimalFormat.format(value);
							}else{
								return value+"";
							}
						}*/
					if (value instanceof Number) {
						if (value instanceof Double || value instanceof Float) {
							if(Double.parseDouble(value+"") % 1.0 == 0){
								return String.valueOf((long)Double.parseDouble(value+""));
							}
						}else{
							return value+"";
						}
					}
//					if(!CommUtils.isEmpty(name) && name.equals("content") && value.toString().length()>300){
//						value = value.toString().substring(0, 300);
//					}
						
						return value;
				}
				
			}
			  
		  };
		out.print(JSON.toJSONString(resp,filter,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.DisableCircularReferenceDetect));
		resp=null;
	}
	
	@SuppressWarnings("rawtypes")
	public static void printAESStr(CommResp resp,PrintWriter out, String iv_string, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
		if(resp.getData()==null){
			resp.setData(new ArrayList());
		}
		if(CommUtils.isObjEmpty(resp.getObject())){
			resp.setObject(new Object());
		}
		if(CommUtils.isObjEmpty(resp.getExt())){
			Map<String, Object> ext = new HashMap<String, Object>();
			
			ext.put("sysdate", CommUtils.getCurrDateTime(new Date()));
			resp.setExt(ext);
		}else{
			resp.getExt().put("sysdate", CommUtils.getCurrDateTime(new Date()));
		}
		
		SerializeFilter filter = new ValueFilter(){
			@Override
			public Object process(Object object, String name, Object value) {
				if(value==null){
						if(value instanceof Number){
								if(value instanceof Double || value instanceof Float){
									return "0.00";
								}else{
									return "0";
								}
						}
						
						return "";
						
				}else{
						if (value instanceof Number) {
							if (value instanceof Double || value instanceof Float) {
								return decimalFormat.format(value);
							}else{
								return value+"";
							}
						}
						
						return value;
				}
				
			}
			  
		  };
		String content = AESCipher.aesEncryptString(iv_string, JSON.toJSONString(resp,filter,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.DisableCircularReferenceDetect), key);
		out.print(content);
		resp=null;
	}
	
	@SuppressWarnings("rawtypes")
	public static void printStrDoubleJson(CommResp resp, PrintWriter out) {
		if (resp.getData() == null) {
			resp.setData(new ArrayList());
		}
		SerializeFilter filter = new ValueFilter() {
			@Override
			public Object process(Object object, String name, Object value) {
				if (value == null) {
					return "";
				}
				if (value instanceof Integer || value instanceof Long || value instanceof Double || value instanceof Float || value instanceof Byte || value instanceof Short
						|| value instanceof Boolean) {
					if (value instanceof Double || value instanceof Float) {
						DecimalFormat decimalFormat = new DecimalFormat("0.00");
						if (decimalFormat != null) {
							return decimalFormat.format(value);
						}
					}

					return value + "";
				}
				return value;
			}

		};
		out.print(JSON.toJSONString(resp, filter, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty));
		resp = null;
	}

}