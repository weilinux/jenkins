package com.mediahx.controller.pay;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.mediahx.bean.RequestVo;
import com.mediahx.constant.RespCode;
import com.mediahx.controller.BaseController;
import com.mediahx.dao.mysql.StadiumMapper;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.util.Arith;
import com.mediahx.util.BeanToMapUtil;
import com.mediahx.util.CallServiceUtil;
import com.mediahx.util.CommUtils;
import com.mediahx.util.ExecuteResult;
import com.mediahx.util.IdGenerateUtils;
import com.mediahx.util.PropertiesUtil;
import com.mediahx.util.RedisClient;
import com.mediahx.weixin.ClientCustomSSL;
import com.mediahx.weixin.GenXmlFile;
import com.mediahx.weixin.MD5;
import com.mediahx.weixin.Refund;
import com.mediahx.weixin.UnifiedOrderResp;

/**
 * 小程支付
 * @author guomf
 * 2019年4月30日
 * project： 梦幻城堡
 */
@Controller
@RequestMapping("Pay/MiniProgramPay")
public class MiniProgramController extends BaseController {
	private static String appid;
	private static String notifyurl;
	private static String apikey;
	private static String secret;
	private static String mchid;
	private static String url;
	private static String refundurl;
	private static String order_query_url;
	@SuppressWarnings("unused")
	private static String timeout_express;
	static Properties pro = PropertiesUtil.getProperties("properties/miniprogram.properties");

	static {
		appid = pro.getProperty("weixin.appid");
		mchid = pro.getProperty("weixin.mchid");
		notifyurl = pro.getProperty("weixin.notifyurl");
		apikey = pro.getProperty("weixin.apikey");
		secret = pro.getProperty("weixin.secret");
		url = pro.getProperty("weixin.url");
		refundurl = pro.getProperty("weixin.refund.url");
		timeout_express = pro.getProperty("weixin.timeout_express");
		order_query_url=pro.getProperty("weixin.orderquery.url");

	}
	@Autowired
	StadiumMapper stadiumMapper;

	@Autowired
	RedisClient redisClient;

	/**
	 * 发起小程序支付
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getSignMiniP", produces = "application/json;charset=UTF-8")
	public void getSignMiniP(HttpServletRequest request, PrintWriter out,
			HttpServletResponse httpServletResponse) {
		logger.info("小程序统一下单 接口调用");
		String orderId = request.getParameter("orderId");
		String spbill_create_ip = request.getParameter("spbill_create_ip");
		double price = Double.valueOf(request.getParameter("price")).doubleValue();
		if (!CommUtils.isEmpty(orderId) && !CommUtils.isEmpty(spbill_create_ip)) {

			// 接受参数(金额)
			String amount = ((int) (Arith.mul(price, 100.0))) + "";
			// 接受参数(openid)
			String openid = request.getParameter("openid");
			//将openid存储便于发送微信服务消息时使用
//			redisClient.set("openid"+order.getOrderNo(), openid, Constants.MINI_VALID_FORMID_TIME );
			// 创建hashmap(用户获得签名)
			TreeMap<String, String> paraMap = new TreeMap<String, String>();
			// 设置body变量 (支付成功显示在微信支付 商品详情中)
			String body = "篮球场相关";
			// 设置随机字符串
			String nonceStr = IdGenerateUtils.getUUID().toUpperCase();
			// 设置请求参数(小程序ID)
			paraMap.put("appid", appid);
			// 设置请求参数(商品描述)
			paraMap.put("body", body);
			// 设置请求参数(商户号)
			paraMap.put("mch_id", mchid);
			// 设置请求参数(随机字符串)
			paraMap.put("nonce_str", nonceStr);
			// 设置请求参数(通知地址)
			paraMap.put("notify_url", notifyurl);
			// 设置请求参数(openid)(在接口文档中 该参数 是否必填项 但是一定要注意
			// 如果交易类型设置成'JSAPI'则必须传入openid)
			paraMap.put("openid", openid);
			// 设置请求参数(商户订单号)
			paraMap.put("out_trade_no", orderId);
			// 设置请求参数(终端IP)
			paraMap.put("spbill_create_ip", spbill_create_ip);
			// 设置请求参数(总金额)
			paraMap.put("total_fee", amount);
			// 设置请求参数(交易类型)
			paraMap.put("trade_type", "JSAPI");
			//签名类型
			paraMap.put("sign_type", "MD5");

//			 String stringA = GenXmlFile.buildParam(paraMap);
			String stringA = formatUrlMap(paraMap, false, false);
			String stringSignTemp = stringA + "&key=" + apikey;
			logger.info("小程序统一下单加签字符串：" + stringSignTemp);
			String sign = MD5.MD5Encode(stringSignTemp).toUpperCase();
			// 将参数 编写XML格式
			StringBuffer paramBuffer = new StringBuffer();
			paramBuffer.append("<xml>");
			paramBuffer.append("<appid>" + appid + "</appid>");
			paramBuffer.append("<body>" + body + "</body>");
			paramBuffer.append("<mch_id>" + mchid + "</mch_id>");
			paramBuffer.append("<nonce_str>" + nonceStr + "</nonce_str>");
			paramBuffer.append("<notify_url>" + notifyurl + "</notify_url>");
			paramBuffer.append("<openid>" + openid + "</openid>");
			paramBuffer.append("<out_trade_no>" + orderId + "</out_trade_no>");
			paramBuffer.append("<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>");
			paramBuffer.append("<total_fee>" + amount + "</total_fee>");
			paramBuffer.append("<trade_type>" + "JSAPI" + "</trade_type>");
			paramBuffer.append("<sign_type>" + "MD5" + "</sign_type>");
			paramBuffer.append("<sign>" + sign + "</sign>");
			paramBuffer.append("</xml>");

			try {
				String iss = getRemotePortData(url, new String(paramBuffer.toString().getBytes(), "ISO8859-1"));
				logger.info("post请求结果：" + iss);
				Document document = doXMLParse(iss);

//				 InputStream is = GenXmlFile.executePostXML(url,paramBuffer.toString());
//				 Document document = GenXmlFile.getXmlDocumentText(is);

				String prepay_id = GenXmlFile.getNodeText(document, "/xml/prepay_id");
//				if (!CommUtils.isEmpty(prepay_id)){
//					//订单作废的 时候也进行了删除
//					redisClient.set("prepay_id"+ orderId, prepay_id, Constants.MINI_VALID_FORMID_TIME );
//				}
				String nonce_str = IdGenerateUtils.getUUID().toUpperCase();
				String timeStamp = (System.currentTimeMillis() / 1000) + "";
				UnifiedOrderResp resp = new UnifiedOrderResp();
				resp.setAppid(appid);
				resp.setNonce_str(nonce_str);
				resp.setMch_id(mchid);
				resp.setPrepay_id(prepay_id);
				resp.setTotal_fee(amount);
				resp.setTimestamp(timeStamp);

				TreeMap<String, String> miniMap = new TreeMap<String, String>();
				// 设置(小程序ID)(这块一定要是大写)
				miniMap.put("appId", appid);
				// 设置(时间戳)
				miniMap.put("timeStamp", timeStamp);
				// 设置(随机串)
				miniMap.put("nonceStr", nonce_str);
				// 设置(数据包)
				miniMap.put("package", "prepay_id=" + prepay_id);
				// 设置(签名方式)
				miniMap.put("signType", "MD5");

				stringA = GenXmlFile.buildParam(miniMap);
				stringSignTemp = stringA + "&key=" + apikey;
				logger.info("小程序加签字符串：" + stringSignTemp);
				sign = MD5.MD5Encode(stringSignTemp).toUpperCase();
				if (StringUtils.isNotBlank(sign)) {
					// 返回签名信息
					resp.setSign(sign);
				}
				resp.setRetCode(RespCode.SUCCESS_CODE.getRetCode());
				resp.setOut_trade_no(orderId);
				logger.info("微信小程序 支付接口生成签名 方法结束");
				out.print(JSON.toJSONString(resp));
			} catch (UnsupportedEncodingException e) {
				logger.info("微信 统一下单 异常：" + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				logger.info("微信 统一下单 异常：" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取openId
	 * 
	 * @param request
	 * @param out
	 * @param httpServletResponse
	 */
	@RequestMapping(value = "/getOpenId", produces = "application/json;charset=UTF-8")
	public void getOpenId(HttpServletRequest request, PrintWriter out, HttpServletResponse httpServletResponse) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(),
				RespCode.SUCCESS_CODE.getRetCode());
		String js_code = request.getParameter("code");
		// 构建get请求参数
		// appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
		String url = "https://api.weixin.qq.com/sns/jscode2session";
		String param = "appid=" + appid + "&secret=" + secret + "&js_code=" + js_code
				+ "&grant_type=authorization_code";
		String str2 = CallServiceUtil.sendGet(url, param);
		// String转换Map，json转List
		logger.info(str2);
		@SuppressWarnings("rawtypes")
		Map reqMap = JSON.parseObject(str2, Map.class);
		if (CommUtils.isObjEmpty(reqMap.get("openid"))) {
			resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "获取openid失败",
					RespCode.FAILURE_CODE.getRetCode());
		}
		resp.setObject(str2);
		CommResp.printStrJson(resp, out);
	}

	private String getRemotePortData(String urls, String param) {
		logger.info("查询抓取数据----开始抓取外网数据");
		try {
			URL url = new URL(urls);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置连接超时时间
			conn.setConnectTimeout(30000);
			// 设置读取超时时间
			conn.setReadTimeout(30000);
			conn.setRequestMethod("POST");
			if (StringUtils.isNotBlank(param)) {
				conn.setRequestProperty("Origin", "https://sirius.searates.com");// 主要参数
				conn.setRequestProperty("Referer",
						"https://sirius.searates.com/cn/port?A=ChIJP1j2OhRahjURNsllbOuKc3Y&D=567&G=16959&shipment=1&container=20st&weight=1&product=0&requst=&weightcargo=1&");
				conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");// 主要参数
			}
			// 需要输出
			conn.setDoInput(true);
			// 需要输入
			conn.setDoOutput(true);
			// 设置是否使用缓存
			conn.setUseCaches(false);
			// 设置请求属性
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			conn.setRequestProperty("Charset", "UTF-8");

			if (StringUtils.isNotBlank(param)) {
				// 建立输入流，向指向的URL传入参数
				DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(param);
				dos.flush();
				dos.close();
			}
			// 输出返回结果
			InputStream input = conn.getInputStream();
			int resLen = 0;
			byte[] res = new byte[1024];
			StringBuilder sb = new StringBuilder();
			while ((resLen = input.read(res)) != -1) {
				sb.append(new String(res, 0, resLen));
			}
			return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.info("查询抓取数据----抓取外网数据发生异常：" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("查询抓取数据----抓取外网数据发生异常：" + e.getMessage());
		}
		logger.info("查询抓取数据----抓取外网数据失败, 返回空字符串");
		return "";
	}

	private static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
		String buff = "";
		Map<String, String> tmpMap = paraMap;
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				@Override
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});
			// 构造URL 键值对的格式
			StringBuilder buf = new StringBuilder();
			for (Map.Entry<String, String> item : infoIds) {
				if (StringUtils.isNotBlank(item.getKey())) {
					String key = item.getKey();
					String val = item.getValue();
					if (urlEncode) {
						val = URLEncoder.encode(val, "utf-8");
					}
					if (keyToLower) {
						buf.append(key.toLowerCase() + "=" + val);
					} else {
						buf.append(key + "=" + val);
					}
					buf.append("&");
				}

			}
			buff = buf.toString();
			if (buff.isEmpty() == false) {
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e) {
			return null;
		}
		return buff;
	}

	@SuppressWarnings("unused")
	private Document doXMLParse(String strxml) throws Exception {
		if (null == strxml || "".equals(strxml)) {
			return null;
		}

		Map<String, String> m = new HashMap<String, String>();
		InputStream in = String2Inputstream(strxml);
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

	private InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}
	/**
	 * 退款
	 * 
	 * @param model
	 * @param request
	 * @param out
	 * @param httpServletResponse
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/refund", produces = "application/json;charset=UTF-8")
	public void refund(Model model, HttpServletRequest request, PrintWriter out,
			HttpServletResponse httpServletResponse) throws Exception {
		logger.info("========================开始退款========================");

		RequestVo reqVo = getPagerParams(request, model);
		Map<String, Object> map = reqVo.getParams();
		if (CommUtils.isObjEmpty(map.get("out_trade_no")) || CommUtils.isObjEmpty(map.get("refund_desc"))
				|| CommUtils.isObjEmpty(map.get("total_fee")) || CommUtils.isObjEmpty(map.get("refund_fee"))) {
			// 退款失败
			ExecuteResult.printJson(ExecuteResult.jsonReturn(ExecuteResult.FAILURE_CODE, "退款失败！"), out);
			return;
		}

		double total_fee = Double.parseDouble(map.get("total_fee") + "");
		double refund_fee = Double.parseDouble(map.get("refund_fee") + "");

		Refund refund = new Refund();
		refund.setAppid(appid);
		refund.setMch_id(mchid);
		refund.setNonce_str(IdGenerateUtils.getUUID().toUpperCase());
		refund.setOut_trade_no(map.get("out_trade_no") + "");
		refund.setOut_refund_no(System.currentTimeMillis() + ""); // 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@
																	// ，同一退款单号多次请求只退一笔。
		refund.setTotal_fee(((int) (Arith.mul(total_fee, 100.0))) + "");
		refund.setRefund_fee(((int) (Arith.mul(refund_fee, 100.0))) + "");
		refund.setRefund_desc(map.get("refund_desc") + "");

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
				ExecuteResult.printJson(ExecuteResult.jsonReturn(ExecuteResult.FAILURE_CODE, "退款失败!!"), out);
				return;
			} else if (!CommUtils.isEmpty(result_code) && result_code.equals("SUCCESS")) {
				// 退款成功
				ExecuteResult.printJson(ExecuteResult.jsonReturn(ExecuteResult.SUCCESS_CODE, "退款成功!"), out);
				return;
			}
		}

		ExecuteResult.printJson(ExecuteResult.jsonReturn(ExecuteResult.FAILURE_CODE, "退款失败！"), out);

		logger.info("========================结束退款========================");
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
			// HttpGet httpget = new
			// HttpGet("https://api.mch.weixin.qq.com/secapi/pay/refund");

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
	
	/**
	 * 根据支付订单id查询订单支付状态
	 * 
	 * @author : guomf
	 * @param outTradeNo
	 * @return
	 */
	public String orderQuery(String outTradeNo) {
		String orderPayResult = null;

		try {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("appid", appid);
			map.put("mch_id", mchid);
			map.put("out_trade_no", outTradeNo);
			map.put("nonce_str", IdGenerateUtils.getUUID().toUpperCase());

			// 获取订单查询签名
			String sign = getSign(map);
			map.put("sign", sign);
			// 拼装请求参数
			String xml = getXml(map);
			System.out.println("订单查询请求参数XML:" + xml);

			String signResult = http(order_query_url, xml);

			// 尝试进行支付操作处理
			InputStream xmlIn = null;
			try {
				xmlIn = new ByteArrayInputStream(signResult.getBytes("UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Document doc = getXmlDocumentText(xmlIn);
			Element root = doc.getRootElement();
			Element el = root.element("return_code");

			System.out.println("订单查询结果结果XML:" + doc.asXML());
			String returnCode = el.getText();
			if ("SUCCESS".equals(returnCode)) {
				String resultCode = root.elementText("result_code");
				if ("SUCCESS".equals(resultCode)) {
					String tradeState = root.elementText("trade_state");
					System.out.println("订单支付状态查询结果:" + tradeState);
					orderPayResult = tradeState;
				} else {
					orderPayResult = "PAYERROR";
				}
			} else {
				orderPayResult = "PAYERROR";
			}
		} catch (Exception e) {
			e.printStackTrace();
			orderPayResult = "PAYERROR";
		}

		return orderPayResult;
	}
	
	/**
	 * 获取支付签名
	 * 
	 * @param signMap
	 * @return
	 */
	private String getSign(Map<String, Object> map) {

		// ASCII排序
		Map<String, Object> finalMap = new LinkedHashMap<String, Object>();
		Comparator<Entry<String, Object>> compator = (p1, p2) -> {
			return p2.getKey().compareTo(p1.getKey());
		};
		map.entrySet().stream().sorted(compator.reversed()).forEach(p -> {
			finalMap.put(p.getKey(), p.getValue());
		});
		// 字段拼接
		StringBuffer buffer = new StringBuffer("");
		finalMap.forEach((p1, p2) -> {
			if (buffer.length() < 1) {
				buffer.append(p1 + "=" + p2);
			} else {
				buffer.append("&" + p1 + "=" + p2);
			}
		});
		// 添加key
		buffer.append("&key=" + apikey);

		// 进行MD5加密
		String md5Str = CommUtils.getMD5(buffer.toString()).toUpperCase();

		return md5Str;
	}
	
	/**
	 * 根据请求参数和加密签名生成请求XML报文
	 * 
	 * @param map
	 * @return
	 */
	private String getXml(Map<String, Object> map) {

		StringBuffer buffer = new StringBuffer("<xml>");
		map.forEach((key, value) -> {
			buffer.append("<" + key + ">" + value + "</" + key + ">");
		});
		buffer.append("</xml>");

		return buffer.toString();
	}
	/**
	 * 发送http请求
	 */
	private String http(String urlStr, String paramStr) {

		try {
			InputStream ins = executePostXML(urlStr, paramStr);
			BufferedReader bur = new BufferedReader(new InputStreamReader(ins, "UTF-8"), 1024);

			StringBuffer buffer = new StringBuffer();
			String tmp = null;
			while ((tmp = bur.readLine()) != null) {
				buffer.append(tmp);
			}
			bur.close();
			ins.close();

			System.out.println("下单结果:" + buffer.toString());
			return buffer.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 执行Post请求
	 * 
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
		resp.close();

		return resp.getEntity().getContent();
	}
	/**
	 * 返回 Document
	 * 
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
	/**
	 * 获取小程序全局唯一后台接口调用凭据（access_token）
	 */
//	private void getAccessToken() {
//		String url = "https://api.weixin.qq.com/cgi-bin/token";
//		String param = "appid=" + appid + "&secret=" + secret + "&grant_type=client_credential";
//		String str2 = CallServiceUtil.sendGet(url, param);
//		// String转换Map，json转List
//		logger.info(str2);
//		@SuppressWarnings("rawtypes")
//		Map reqMap = JSON.parseObject(str2, Map.class);
//		if (!CommUtils.isObjEmpty(reqMap.get("access_token"))) {
//			redisClient.set("mini_access_token", reqMap.get("access_token")+"",Constants.MINI_VALID_TOKEN_TIME);
//		}
//	}
}
