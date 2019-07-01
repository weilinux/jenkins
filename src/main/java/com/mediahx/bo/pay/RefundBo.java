package com.mediahx.bo.pay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.dom4j.Document;
import org.springframework.stereotype.Component;

import com.mediahx.constant.RespCode;
import com.mediahx.controller.BaseController;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.util.Arith;
import com.mediahx.util.BeanToMapUtil;
import com.mediahx.util.CommUtils;
import com.mediahx.util.IdGenerateUtils;
import com.mediahx.util.PropertiesUtil;
import com.mediahx.weixin.ClientCustomSSL;
import com.mediahx.weixin.GenXmlFile;
import com.mediahx.weixin.MD5;
import com.mediahx.weixin.Refund;

/**
 *	退款
 * @author guomf
 * 2019年5月11日
 * project： 梦幻城堡
 */
@Component
public class RefundBo extends BaseController{
	
	private static String appid;
	private static String apikey;
	private static String mchid;
	private static String refundurl;
	@SuppressWarnings("unused")
	private static String timeout_express;
	static Properties pro = PropertiesUtil.getProperties("properties/miniprogram.properties");

	static {
		appid = pro.getProperty("weixin.appid");
		mchid = pro.getProperty("weixin.mchid");
		apikey = pro.getProperty("weixin.apikey");
		refundurl = pro.getProperty("weixin.refund.url");
		timeout_express = pro.getProperty("weixin.timeout_express");

	}
	public static final String REFUND_PART_DESCRI = "平台操作退还多余的预支付款项";
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CommResp refund(Map<String, Object> map, CommResp resp) throws Exception {
		logger.info("========================开始退款========================");

		if (CommUtils.isObjEmpty(map.get("orderId")) || CommUtils.isObjEmpty(map.get("total_fee")) || CommUtils.isObjEmpty(map.get("refund_fee"))) {
			// 退款失败
			resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "缺少参数请确认", RespCode.FAILURE_CODE.getRetCode());
			return resp ;
		}
		if (CommUtils.isObjEmpty(map.get("refund_desc"))){
			map.put("refund_desc", REFUND_PART_DESCRI);
		}
		double total_fee = Double.parseDouble(map.get("total_fee") + "");
		double refund_fee =Arith.reservedDecimal(map.get("refund_fee") + "",1);

		Refund refund = new Refund();
		refund.setAppid(appid);
		refund.setMch_id(mchid);
		refund.setNonce_str(IdGenerateUtils.getUUID().toUpperCase());
		refund.setOut_trade_no(map.get("orderId") + "");
		refund.setOut_refund_no(System.currentTimeMillis() + ""); // 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@
																	// ，同一退款单号多次请求只退一笔。
		refund.setTotal_fee(((int) (Arith.mul(total_fee, 100.0))) + "");
		refund.setRefund_fee(((int) (Arith.mul(refund_fee, 100.0))) + "");
		refund.setRefund_desc(map.get("refund_desc")+"");

		logger.info("加签前字符串：" + GenXmlFile.getRefundOrderXmlFile(refund));

		TreeMap tm = (TreeMap) BeanToMapUtil.convertSortMap(refund);
		String stringA = GenXmlFile.buildParam(tm);
		String stringSignTemp = stringA + "&key=" + apikey;
		logger.info("加签字符串：" + stringSignTemp);
		String sign = MD5.MD5Encode(stringSignTemp).toUpperCase();

		refund.setSign(sign);
		logger.info("加签后字符串：" + GenXmlFile.getRefundOrderXmlFile(refund));

		Document document = refundHttpPost(GenXmlFile.getRefundOrderXmlFile(refund));

		if (!CommUtils.isObjEmpty(document)) {
			String result_code = GenXmlFile.getNodeText(document, "/xml/result_code");
			if (!CommUtils.isEmpty(result_code) && result_code.equals("FAIL")) {
				// 退款失败
				resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "此次失败，可能已经退过了，检查一下吧", RespCode.FAILURE_CODE.getRetCode());
				return resp ;
			} else if (!CommUtils.isEmpty(result_code) && result_code.equals("SUCCESS")) {
				// 退款成功
				resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), "退款成功", RespCode.SUCCESS_CODE.getRetCode());
				return resp ;
			}
		}

		resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "退款失败", RespCode.FAILURE_CODE.getRetCode());

		logger.info("========================结束退款========================");
		return resp;
	}
	
	private Document refundHttpPost(String stringEntity)
			throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException,
			CertificateException, KeyManagementException, UnrecoverableKeyException, ClientProtocolException {
		Document document = null;

		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream instream = new FileInputStream(
				new File(ClientCustomSSL.class.getResource("").getPath().substring(0) + "apiclient_cert.p12"));

		try {
			keyStore.load(instream, mchid.toCharArray());
		} finally {
			instream.close();
		}

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchid.toCharArray()).build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpPost = new HttpPost(refundurl);

			httpPost.setEntity(new StringEntity(stringEntity, ContentType.create("text/xml", "utf-8")));

			logger.info("executing request" + httpPost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httpPost);

			InputStream is = response.getEntity().getContent();
			document = GenXmlFile.getXmlDocumentText(is);

		} finally {
			httpclient.close();
		}

		return document;
	}
}
