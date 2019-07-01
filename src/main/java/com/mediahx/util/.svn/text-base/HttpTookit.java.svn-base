package com.mediahx.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
/**
 * HTTP工具箱
 *
 *
 * @author czm
 */
public  class HttpTookit {
	private static Logger logger = LoggerFactory.getLogger(HttpTookit.class);

	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param queryString
	 *            请求的查询参数,可以为null
	 * @param charset
	 *            字符集
	 * @param pretty
	 *            是否美化
	 * @return 返回请求响应的HTML
	 */
	public static String doGet(String url, String queryString, String charset,
			boolean pretty) {
		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		try {
			if (StringUtils.isNotBlank(queryString))
				// 对get请求参数做了http请求默认编码，好像没有任何问题，汉字编码后，就成为%式样的字符串
				method.setQueryString(URIUtil.encodeQuery(queryString));
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(),
								charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty)
						response.append(line).append(
								System.getProperty("line.separator"));
					else
						response.append(line);
				}
				reader.close();
			}
		} catch (URIException e) {
			logger.error("执行HTTP Get请求时，编码查询字符串“" + queryString + "”发生异常！", e);
		} catch (IOException e) {
			logger.error("执行HTTP Get请求" + url + "时，发生异常！", e);
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}

	/**
	 * 执行一个HTTP POST请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @param charset
	 *            字符集
	 * @param pretty
	 *            是否美化
	 * @return 返回请求响应的HTML
	 */
	public static String doPost(String url, Map<String, String> params,
			String charset, boolean pretty) {
		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		HttpMethod method = new PostMethod(url);
		// 设置Http Post数据
		if (params != null) {
			HttpMethodParams p = new HttpMethodParams();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				p.setParameter(entry.getKey(), entry.getValue());
			}
			method.setParams(p);
		}
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(),
								charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty)
						response.append(line).append(
								System.getProperty("line.separator"));
					else
						response.append(line);
				}
				reader.close();
			}
		} catch (IOException e) {
			logger.error("执行HTTP Post请求" + url + "时，发生异常！", e);
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}
	
	/**
	 * 执行一个HTTP POST请求，返回请求响应的Json
	 * @param url  请求的URL地址
	 * @param map  请求设置内容
	 * @return 返回请求响应的Json
	 * @throws IOException
	 */
    /*public static String doPost(String url, Map<String, String> map) throws IOException{
		String response = null;
		PostMethod postMethod = new PostMethod(url);
		String tmp = JSON.toJSON(map).toString();
		String params = "jsonparam=" + URLEncoder.encode(tmp, "utf-8") + "&sign=" + MD5Hash.encryptPwd(tmp);
		try {
			HttpClient client = new HttpClient();
			postMethod.setRequestEntity(new StringRequestEntity(params,"text/json;charset=UTF-8","UTF-8"));
			int status = client.executeMethod(postMethod);
			if(status == HttpStatus.SC_OK){
				response = postMethod.getResponseBodyAsString();
			} else {
				response = "fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			postMethod.releaseConnection();
		}
		return response;
	}*/

	/**
	 * 上传文件到指定URL
	 * 
	 * @param file
	 * @param token
	 * @param ts
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String doUploadFile(File file, String token, String ts,
			String url) throws IOException {
		String response = "";
		if (!file.exists()) {
			return "file not exists";
		}
		PostMethod postMethod = new PostMethod(url);

		StringPart sp1 = new StringPart("token", token);
		StringPart sp2 = new StringPart("ts", ts);

		try {
			// FilePart：用来上传文件的类,file即要上传的文件
			FilePart fp = new FilePart("file", file);
			Part[] parts = { fp, sp1, sp2 };

			// 对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
			MultipartRequestEntity mre = new MultipartRequestEntity(parts,
					postMethod.getParams());
			postMethod.setRequestEntity(mre);

			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(50000);// 由于要上传的文件可能比较大 ,
													// 因此在此设置最大的连接超时时间
			int status = client.executeMethod(postMethod);
			if (status == HttpStatus.SC_OK) {
				InputStream inputStream = postMethod.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						inputStream));
				StringBuffer stringBuffer = new StringBuffer();
				String str = "";
				while ((str = br.readLine()) != null) {
					stringBuffer.append(str);
				}
				response = stringBuffer.toString();
			} else {
				response = "fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			postMethod.releaseConnection();
		}
		return response;
	}
	/**
	 * 下载文件
	 * 
	 * @param file
	 * @param token
	 * @param ts
	 * @param url
	 * @param targetFile
	 *            目录地址
	 * @return
	 * @throws IOException
	 */
	public static boolean downFile(String file, String token, String ts,
			String targetFile,String url){
		if (CommUtils.isEmpty(file) || CommUtils.isEmpty(token)
				|| CommUtils.isEmpty(ts) || CommUtils.isEmpty(url)) {
			logger.error("文件fileid,ts,url,token可能为空");
			return false;
		}
		url = url + "/" + file + "?ts=" + ts + "&token=" + token;
		logger.debug("请求路径:" + url);
		GetMethod getMethod = new GetMethod(url);
		boolean issuccess = false;
		InputStream inputStream = null;
		FileOutputStream out = null;
		try {
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(50000);// 由于要上传的文件可能比较大 ,
													// 因此在此设置最大的连接超时时间
			int status = client.executeMethod(getMethod);
			logger.debug("下载文件状态:"+status);
			if (status == HttpStatus.SC_OK) {
				inputStream = getMethod.getResponseBodyAsStream();
				if (inputStream != null) {
					out = new FileOutputStream(new File(targetFile));
					byte[] b = new byte[1024];
					int len = 0;
					while ((len = inputStream.read(b)) != -1) {
						out.write(b, 0, len);
					}
					issuccess = true;
				}
			}			
		} catch (Exception e) {
			logger.error("下载文件失败", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return issuccess;
	}

	public static void main(String[] args) throws Exception {
		// String y = doGet("http://video.sina.com.cn/life/tips.html", null,
		// "GBK", true);
		// String y = doPost("http://172.17.3.155/uploadMerchantPhoto.php",
		// null, "GBK", true);
		// String y = doPost("http://localhost:8080/mpay/UploadServlet", null,
		// "GBK", true);
		// System.out.println(y);
//		String filename = "group1/M00/03/BC/rBEDm1VtdSyATOEJAAAAxV0RLHA5387097";
//
//		String token = Constants.REQ_FILE_SYSTEM_TOKEN_KEY;
//		long ts = System.currentTimeMillis() / 1000;
//		String str = filename.substring(filename.indexOf("/") + 1);
//		System.out.println(str);
//		token = MD5Hash.encryptPwd(str + token + ts);
//		System.out.println("token=" + str + token + ts);
		

	}
}