package com.mediahx.weixin;

import java.io.Serializable;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.mediahx.util.StrUtil;

/**
 * 
 * @author ZHE
 *
 */
@SuppressWarnings("serial")
public class WeiXinResp implements Serializable{
	private String return_code;
	private String return_msg;
	private String sign;
	
	public static String getWeiXinRespXmlFile(WeiXinResp bean) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xml");
		root.addElement("return_code").setText(StrUtil.getStr(bean.getReturn_code()));
		root.addElement("return_msg").setText(StrUtil.getStr(bean.getReturn_msg()));
		root.addElement("sign").setText(StrUtil.getStr(bean.getSign()));
		return document.getRootElement().asXML();
	}
	
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
