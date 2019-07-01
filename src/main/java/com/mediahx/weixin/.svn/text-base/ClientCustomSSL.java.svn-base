package com.mediahx.weixin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */
public class ClientCustomSSL {

	public final static void main(String[] args) throws Exception {
		System.out.println(ClientCustomSSL.class.getResource("/com/mediahx/weixin").getPath());
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		// FileInputStream instream = new FileInputStream(new
		// File(ClientCustomSSL.class.getResource("").getPath().substring(0)+"apiclient_cert.p12"));
		FileInputStream instream = new FileInputStream(new File("E:/workspace2/hx_admin/src/main/java/com/mediahx/weixin/apiclient_cert.p12"));
		try {
			keyStore.load(instream, "1482467662".toCharArray());
		} finally {
			instream.close();
		}

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1482467662".toCharArray()).build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
			// HttpGet httpget = new
			// HttpGet("https://api.mch.weixin.qq.com/secapi/pay/refund");

			System.out.println("executing request" + httpPost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();

				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println("Response content length: " + entity.getContentLength());
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String text;
					while ((text = bufferedReader.readLine()) != null) {
						System.out.println(text);
					}

				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

}
