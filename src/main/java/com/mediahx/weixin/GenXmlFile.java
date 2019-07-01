package com.mediahx.weixin;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.mediahx.util.BeanToMapUtil;
import com.mediahx.util.CommUtils;
import com.mediahx.util.IdGenerateUtils;
import com.mediahx.util.StrUtil;

/**
 * 
 * @author ZHE
 *
 */
public class GenXmlFile {
	public CloseableHttpClient httpClient;
	
	public static String getRefundOrderXmlFile(Refund bean) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xml");
		root.addElement("appid").setText(StrUtil.getStr(bean.getAppid()));
		root.addElement("mch_id").setText(StrUtil.getStr(bean.getMch_id()));
		root.addElement("nonce_str").setText(StrUtil.getStr(bean.getNonce_str()));
		root.addElement("sign").setText(StrUtil.getStr(bean.getSign()));
		root.addElement("out_trade_no").setText(StrUtil.getStr(bean.getOut_trade_no()));
		root.addElement("out_refund_no").setText(StrUtil.getStr(bean.getOut_refund_no()));
		root.addElement("total_fee").setText(StrUtil.getStr(bean.getTotal_fee()));
		root.addElement("refund_fee").setText(StrUtil.getStr(bean.getRefund_fee()));
		root.addElement("refund_desc").setText(StrUtil.getStr(bean.getRefund_desc()));
		return document.getRootElement().asXML();
	}
	
	public static String getUnifiedOrderXmlFile(UnifiedOrder bean) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xml");
		root.addElement("appid").setText(StrUtil.getStr(bean.getAppid()));
//		root.addElement("attach").setText(StrUtil.getStr(bean.getAttach()));
		root.addElement("body").setText(StrUtil.getStr(bean.getBody()));
		root.addElement("mch_id").setText(StrUtil.getStr(bean.getMch_id()));
		root.addElement("nonce_str").setText(StrUtil.getStr(bean.getNonce_str()));
		root.addElement("notify_url").setText(StrUtil.getStr(bean.getNotify_url()));
		root.addElement("out_trade_no").setText(StrUtil.getStr(bean.getOut_trade_no()));
		root.addElement("spbill_create_ip").setText(StrUtil.getStr(bean.getSpbill_create_ip()));
		root.addElement("total_fee").setText(StrUtil.getStr(bean.getTotal_fee()));
		root.addElement("trade_type").setText(StrUtil.getStr(bean.getTrade_type()));
		root.addElement("sign").setText(StrUtil.getStr(bean.getSign()));
		return document.getRootElement().asXML();
	}
	/**
	 * 查询订单 xml文件
	 * @param bean
	 * @return
	 */
	public static String getOrderQueryXmlFile(OrderQuery bean) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xml");
		root.addElement("appid").setText(StrUtil.getStr(bean.getAppid()));
		root.addElement("mch_id").setText(StrUtil.getStr(bean.getMch_id()));
		root.addElement("out_trade_no").setText(StrUtil.getStr(bean.getOut_trade_no()));
		root.addElement("nonce_str").setText(StrUtil.getStr(bean.getNonce_str()));
		root.addElement("sign").setText(StrUtil.getStr(bean.getSign()));
		return document.getRootElement().asXML();
	}

	/**
	 * 拼接键值对
	 * 
	 * @param key
	 * @param value
	 * @param isEncode
	 * @return
	 */
	private static String buildKeyValue(String key, String value, boolean isEncode) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append("=");
		if (isEncode) {
			try {
				sb.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				sb.append(value);
			}
		} else {
			sb.append(value);
		}
		return sb.toString();
	}

	/**
	 * 构造支付订单参数信息
	 * 
	 * @param map
	 *            支付订单参数
	 * @return
	 */
	public static String buildParam(Map<String, String> map) {
		List<String> keys = new ArrayList<String>(map.keySet());

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = map.get(key);
			sb.append(buildKeyValue(key, value, false));
			sb.append("&");
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = map.get(tailKey);
		sb.append(buildKeyValue(tailKey, tailValue, false));

		return sb.toString();
	}

	protected static CloseableHttpClient buildHttpClient(SSLContext ctx) {
		HttpClientBuilder hcb = HttpClientBuilder.create().disableAuthCaching().disableCookieManagement().setSslcontext(ctx);

		return (hcb.build());
	}

	/**
	 * 执行Post请求
	 * @param fullURL
	 * @param bodyXML
	 * @return
	 * @throws IOException
	 */
	public static InputStream executePostXML(String fullURL, String bodyXML) throws IOException {

		CloseableHttpClient hc = HttpClientBuilder.create().disableAuthCaching().disableCookieManagement().build();

		HttpPost req = new HttpPost(fullURL);
		req.setEntity(new StringEntity(bodyXML, ContentType.create("text/xml", "utf-8")));

		CloseableHttpResponse resp = hc.execute(req);
		 System.out.println(resp.getEntity().getContent());
		resp.close();
		return resp.getEntity().getContent();
	}
	
	public static String executePost(String fullURL) throws IOException {
		CloseableHttpClient hc = HttpClientBuilder.create().disableAuthCaching().disableCookieManagement().build();
		HttpPost req = new HttpPost(fullURL);
		CloseableHttpResponse resp = hc.execute(req);
		resp.close();

		return  convertStreamToString(resp.getEntity().getContent());
	}
	
	public static String executeGet(String fullURL) throws IOException {
		CloseableHttpClient hc = HttpClientBuilder.create().disableAuthCaching().disableCookieManagement().build();
		HttpGet req = new HttpGet(fullURL);
		CloseableHttpResponse resp = hc.execute(req);
		resp.close();

		return convertStreamToString(resp.getEntity().getContent());
	}
	
	/**
	 * 返回 Document
	 * @param in
	 * @return
	 */
	public static Document getXmlDocumentText(InputStream in) {
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding("UTF-8");
		Document document = null;
		try {
			document = saxReader.read(in);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	public static String getNodeText(Document document,String txtNode){
		Node n = document.selectSingleNode(txtNode);
		if (!CommUtils.isObjEmpty(n) && !StrUtil.isEmpty(n.getText())) {
			return n.getText().trim();
		}
		return "";
	}
	
	/**
	 * 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {      
         BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
         StringBuilder sb = new StringBuilder();      
     
         String line = null;      
        try {      
            while ((line = reader.readLine()) != null) {      
                 sb.append(line + "\n");      
             }      
         } catch (IOException e) {      
             e.printStackTrace();      
         } finally {      
            try {      
                 is.close();      
             } catch (IOException e) {      
                 e.printStackTrace();      
             }      
         }      
     
        return sb.toString();      
     }
	
	public static void main(String[] args) throws Exception {
		UnifiedOrder bean = new UnifiedOrder();
		bean.setAppid("wx0f87a5a0faa4936d");
//		bean.setAttach("微信支付");
		bean.setBody("区信");
		bean.setMch_id("1482467662");
//		bean.setNonce_str(IdGenerateUtils.getUUID());
		bean.setNonce_str("9e94542db5b3404a8e528acd36202211");
		bean.setNotify_url("http://pay.hxinside.com:9991/hx_admin/weixin/receiveNotify");
		bean.setOut_trade_no(System.currentTimeMillis()+"");
		bean.setSpbill_create_ip("123.12.12.123");
		bean.setTotal_fee("888");
		bean.setTrade_type("APP");
		// bean.setSign("xxxx");

		System.out.println(GenXmlFile.getUnifiedOrderXmlFile(bean));

		TreeMap map = (TreeMap) BeanToMapUtil.convertSortMap(bean);
		String stringA = buildParam(map);
		String stringSignTemp = stringA + "&key=520eC450459A4CEA87162400Db6DF273";
		System.out.println("stringSignTemp="+stringSignTemp);
		String sign = MD5.MD5Encode(stringSignTemp).toUpperCase();
//		String sign = MD5.MD5Encode("appid=wx0f87a5a0faa4936d&attach=微信支付&body=区信微信支付&mch_id=1482467662&nonce_str=9e94542db5b3404a8e528acd36202211&notify_url=http://119.23.17.175:9991/hx_admin/alipay/receiveNotify&out_trade_no=20150806125346&spbill_create_ip=123.12.12.123&total_fee=10&trade_type=APP&key=520ec450459a4cea87162400db6df273").toUpperCase();
		System.out.println(buildParam(map));
		System.out.println("sign="+sign);

		bean.setSign(sign);
		System.out.println(GenXmlFile.getUnifiedOrderXmlFile(bean));

		InputStream is = executePostXML("https://api.mch.weixin.qq.com/pay/unifiedorder", GenXmlFile.getUnifiedOrderXmlFile(bean));
		FileOutputStream fos = new FileOutputStream("D:/tmp.txt");
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = is.read(buffer)) != -1) {
			fos.write(buffer, 0, len);
		}
		fos.close();
		is.close();
	}
}
