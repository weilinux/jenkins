package com.mediahx.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;


/**
 * 
 * @author ZHE
 *
 */
public class AliSmsSender {
	private static Logger logger = LoggerFactory.getLogger(AliSmsSender.class);
	
	private final static String secret = "0ba9d9283a249655eb7baf23501b546c";
	private final static String appkey = "23632952";
	
//	private final static String appkey = "LTAIba1xs0OEUX5W";
//	private final static String secret = "mM0CqdDeExrXddaVwYDY2y78v2Bl4V";
	
	private final static String url = "http://gw.api.taobao.com/router/rest";
	

	
	/**
	 * 发送验证码短信
	 * @param smsFreeSignName  短信签名
	 * @param code  验证码
	 * @param product 产品名
	 * @param recNum 手机
	 * @param smsTemplateCode 模板代号
	 * @param strExtend  扩展参数
	 * @return
	 * @throws Exception
	 * 验证码${code}，您正在注册成为${product}用户，感谢您的支持！
	 */
	public static String send(String smsFreeSignName,String code, String product, String recNum,String smsTemplateCode,String strExtend) throws Exception {
		logger.info("###");
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend(strExtend );
		req.setSmsType( "normal" );
		req.setSmsFreeSignName( smsFreeSignName );
		req.setSmsParamString( "{code:'"+ code +"',product:'"+ product +"'}" );
		req.setRecNum( recNum );
		req.setSmsTemplateCode( smsTemplateCode );
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		System.out.println(rsp.getBody());
		return rsp.getBody();
	}
	
	public static void main(String[] args) throws Exception {
		String smsFreeSignName = "大鱼测试";
		String product = "区信";
		String smsTemplateCode = "SMS_46450005";
		String result = AliSmsSender.send(smsFreeSignName, "1234", product, "13537574377", smsTemplateCode, "");
		System.out.println(result);
	}
	
}
