package com.mediahx.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;

/**
 * 
 * @author ZHE
 *
 */
public class PropertiesUtil {
		
	private static String CONFIG_ENCODING = "UTF-8";
	
		public static String getPropertyForString(String property){
			
			Properties prop= new Properties();
	        InputStreamReader is = null;
	        try {
	        	is = new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream("properties/config.properties"), CONFIG_ENCODING);
				prop.load(is);
		        return prop.getProperty(property);
			} catch (UnsupportedEncodingException e) {
				
			} catch (IOException e) {
				//e.printStackTrace();
			}
			return "";
		}
		
		public static int getPropertyForInt(String properties){
			Properties prop= new Properties();
			InputStreamReader is = null;
	        try {
	        	is = new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream("properties/config.properties"), CONFIG_ENCODING);
				prop.load(is);
		        return Integer.parseInt(prop.getProperty(properties));
			} catch (UnsupportedEncodingException e) {
				
			} catch (IOException e) {
				//e.printStackTrace();
			}
			return 0;
			
		}
		
		public static long getPropertyForLong(String properties){
			Properties prop= new Properties();
			InputStreamReader is = null;
	        try {
	        	is = new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream("properties/config.properties"), CONFIG_ENCODING);
				prop.load(is);
		        return Long.parseLong(prop.getProperty(properties));
			} catch (UnsupportedEncodingException e) {
				
			} catch (IOException e) {
				//e.printStackTrace();
			}
			return 0;		
		}
		
		private PropertiesUtil() {
		}

		/**
		 * 返回properties对象。
		 *
		 * @param relativePath
		 * @return
		 */
		public static Properties getProperties(String relativePath) {
			Properties properties = new Properties();
			try {

				URL url = PropertiesUtil.getResource(relativePath);
				File file = new File(url.getPath());
				InputStream in = new BufferedInputStream(new FileInputStream(file));
				properties.load(in);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return properties;
		}

		/**
		 * 获取资源URL，获取不到返回Null。
		 *
		 * @param relativePath
		 *            相对路径，如config/client.properties
		 * @return 资源URL
		 */
		public static URL getResource(String relativePath) {
			return Thread.currentThread().getContextClassLoader().getResource(relativePath);
		}


}
