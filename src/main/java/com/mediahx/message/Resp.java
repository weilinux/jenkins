package com.mediahx.message;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * APP 端接口
 * @author ZHE
 *
 */
public class Resp implements Serializable{

    private static final long serialVersionUID = 1L;

    /** 返回true或false*/
    private boolean rtn = true;

    /** 返回信息 */
    private String rtnMsg;

    /**总记录数*/
    private int count=0;

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
     *  版本
     */
    private String version="1.01";
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

    public Resp(){
    }
    
    public Resp(boolean rtn, String rtnMsg, String retCode) {
		super();
		this.rtn = rtn;
		this.rtnMsg = rtnMsg;
		this.retCode = retCode;
	}

	public Resp(boolean rtn,String rtnMsg){
        this.rtn=rtn;
        this.rtnMsg=rtnMsg;
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
	
	public static void printJson(Resp resp,PrintWriter out){
		out.print(JSON.toJSONString(resp,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullStringAsEmpty));
		resp=null;
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

}