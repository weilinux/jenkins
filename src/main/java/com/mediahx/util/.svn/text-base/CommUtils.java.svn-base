package com.mediahx.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

/**
 * 
 * @author ZHE
 * 
 * 通用工具类
 *
 */
public class CommUtils {

	private static final String dateFormatStr1 = "yyyy-MM-dd HH:mm:ss";
	private final static String dateFormatStr2 = "yyyy-MM-dd";
	private final static SimpleDateFormat format1 = new SimpleDateFormat(
			dateFormatStr1);
	private final static SimpleDateFormat format2 = new SimpleDateFormat(
			dateFormatStr2);
	
	

	/**
	 * 判断字符串是否为空.
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0 || str.equals("null");
	}

	/**
	 * 判断对象为空,或空字符串
	 * 
	 * @param obj
	 * @return true 为空 ，false 不为空
	 */
	public static boolean isObjEmpty(Object obj) {
		if (obj != null && !obj.equals("")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断对象为空,或空字符串 NULL
	 * 
	 * @param obj
	 * @return true 为空 ，false 不为空
	 */
	public static boolean isNullObjEmpty(Object obj) {
		if (obj != null && !obj.equals("") && !obj.equals("null")) {
			return false;
		}
		return true;
	}

	/**
	 * 分转换元,保留两位小数 0.00
	 * 
	 * @param value
	 * @return
	 */
	public static String convertFenToYuan(BigDecimal value) {
		if (CommUtils.isObjEmpty(value)) {
			return "0.00";
		}
		BigDecimal result = value.divide(new BigDecimal(100));
		return String.format("%.2f", result.doubleValue());
	}

	/**
	 * 字符串 ISO-8859-1 转成 GBK
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String tranISO2GBK(String str) throws Exception {
		if (!isEmpty(str)) {
			return new String(str.getBytes("ISO-8859-1"), "GBK");
		} else {
			return "";
		}

	}

	/**
	 * 字符串 UTF-8 转成 GBK
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String tranUTF82GBK(String str) throws Exception {
		if (!isEmpty(str)) {
			return new String(str.getBytes("UTF-8"), "GBK");
		} else {
			return "";
		}

	}

	/**
	 * 把日期转成字符串
	 * 
	 * @param date
	 *            日期
	 * @param formatStr
	 *            字符格式
	 * @return String
	 */
	public static String formatDate2Str(Date date, String formatStr) {
		if (date == null) {
			return null;
		}
		DateFormat df = new SimpleDateFormat(formatStr);
		try {
			return df.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 字符转ASC
	 * 
	 * @param value
	 * @return
	 */
	public static String stringToAscii(String value) {
		StringBuffer sbu = new StringBuffer();
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (i != chars.length - 1) {
				// sbu.append((int)chars[i]).append(",");
				sbu.append((int) chars[i]);
			} else {
				sbu.append((int) chars[i]);
			}
		}
		return sbu.toString();
	}

	/**
	 * ASC转字符
	 * 
	 * @param value
	 * @return
	 */
	public static String asciiToString(String value) {
		StringBuffer sbu = new StringBuffer();
		String[] chars = value.split(",");
		for (int i = 0; i < chars.length; i++) {
			sbu.append((char) Integer.parseInt(chars[i]));
		}
		return sbu.toString();
	}

	/**
	 * 返回后4位
	 * 
	 * @param str
	 * @return
	 */
	public static String getFourSuffix(String str) {
		if (!CommUtils.isEmpty(str)) {
			return str.substring(str.length() - 4);
		}
		return "";
	}

	/**
	 * 添加开始时间后缀
	 * 
	 * @param str
	 * @return
	 */
	public static String appendSuffixSDate(String str) {
		if (!CommUtils.isEmpty(str)) {
			StringBuilder sb = new StringBuilder(str);
			sb.append(" ");
			sb.append("00:00:00");
			return sb.toString();
		}
		return null;
	}

	/**
	 * 添加结束时间后缀
	 * 
	 * @param str
	 * @return
	 */
	public static String appendSuffixEDate(String str) {
		if (!CommUtils.isEmpty(str)) {
			StringBuilder sb = new StringBuilder(str);
			sb.append(" ");
			sb.append("23:59:59");
			return sb.toString();
		}
		return null;
	}

	/**
	 * 取当前时间 ：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            日期
	 * @return String
	 */
	public static String getCurrDateTime(Date date) {
		String str = null;
		if (!CommUtils.isObjEmpty(date)) {
			SimpleDateFormat sd = new SimpleDateFormat(dateFormatStr1);
			try {
				str = sd.format(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * 把字符日期 "yyyy-MM-dd" 转 "yyyyMMdd"
	 * 
	 * @param strDate
	 * @return String
	 */
	public static String changeStrDate(String strDate) {
		String str = null;
		if (strDate != null && !strDate.equals("")) {
			str = strDate.replace("-", "");
		}
		return str;
	}

	/**
	 * 获取固定长度的随机数字字符串，不足位数在前面补0
	 * 
	 * @param strLength
	 * @return
	 */
	public static String getFixLenthString(int strLength) {

		Random rm = new Random();
		// 返回固定的长度的随机数
		String rmResult = String.valueOf(rm.nextInt((int) Math.pow(10,
				strLength * 2)));
		int len = rmResult.length() - strLength;
		if (len > 0) {
			// 大于0，表示长度过长，从左到右截取
			rmResult = rmResult.substring(rmResult.length() - strLength,
					rmResult.length());
		} else if (len < 0) {
			// 小于0，表示长度不足，在前面补0
			String prefix = "";
			for (int i = 0; i > len; i--) {
				prefix += "0";
			}
			rmResult = prefix + rmResult;
		}

		return rmResult;
	}

	/**
	 * SHA加密
	 * 
	 * @param targetString
	 * @return
	 */
	public static String encryptSHA(String targetString) {

		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			sha.update(targetString.getBytes());
			BigInteger result = new BigInteger(sha.digest());
			return result.toString();
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 获取字符串MD5
	 * 
	 * @param source
	 * @return
	 */
	public static String getMD5(String source) {

		String s = null;
		// 用来将字节转换成 16 进制表示的字符
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] b = source.getBytes("UTF-8");
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(b);
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];

				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 加盐MD5（source.hashCode() + source+source.length() + source.hashCode()）
	 * 
	 * @param source
	 *            目标字符串
	 * @return 加盐MD5字符串
	 */
	public static String getMD5WithSalt(String source) {
		return getMD5(source.hashCode() + source + source.length()
				+ source.hashCode());
	}

	/**
	 * 前面填充 0
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String formatStringPrefix(String src, int length) {
		if (src == null || src.length() == 0) {
			return null;
		} else if (src.length() == length) {
			return src;
		} else if (src.length() > length) {
			return src;
		} else {
			if (src.length() < length) {
				for (int i = src.length(); i < length; i++) {
					src = "0" + src;
				}
			}
		}
		return src;
	}

	/**
	 * 所有数字位异或校验
	 * 
	 * @param src
	 * @return
	 */
	public static int checkNum(String src) {
		int temp = 0;
		if (src == null || src.length() == 0) {
			return temp;
		} else {
			temp = Integer.parseInt(src.substring(0, 1));
			for (int i = 1; i < src.length(); i++) {
				// System.out.println(temp|Integer.parseInt(src.charAt(i)+""));
				temp = temp | Integer.parseInt(src.charAt(i) + "");
			}
		}
		String str = temp + "";
		return Integer.parseInt(str.substring(str.length() - 1, str.length()));
	}

	/**
	 * 如果是str1为空,则返回str2;否则返回str1
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String selNull(String str1, String str2) {
		if (isEmpty(str1)) {
			return str2;
		}
		return str1;
	}

	/**
	 * 转换map里面的key为小写
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, Object> converLowerKey(Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Iterator<Map.Entry<String, Object>> iterator = map.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = iterator.next();
			resultMap.put(entry.getKey().toLowerCase(), entry.getValue());
		}
		return resultMap;

	}

	/**
	 * 首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 字符串转list,以逗号隔开
	 * 
	 * @param str
	 * @return List<String>
	 */
	public static List<String> splitStr(String str) {
		if (isEmpty(str))
			return null;
		String[] strs = str.split(",");
		List<String> list = new ArrayList<String>();
		for (String id : strs) {
			if (!isEmpty(id)) {
				list.add(id);
			}
		}
		return list;
	}

	/**
	 * 填充指定长度的数据
	 * 
	 * @param length
	 * @param isLeftAlign
	 *            是否左对齐
	 * @param value
	 *            填充数据
	 * @param fixTag
	 *            填充标签
	 * @param sourceFormat
	 * @return
	 * @throws Exception
	 */
	public static String supplyValue(int length, boolean isLeftAlign,
			String value, String fixTag, String sourceFormat) throws Exception {
		String result = "";
		String afterFormat = "utf-8";
		if (sourceFormat != null && !"".equals(sourceFormat.trim())) {
			afterFormat = sourceFormat;
		}
		String beforeFormat = "iso-8859-1";

		String format = null;
		if (isLeftAlign) {
			format = "%-" + length + "s";
		} else {
			format = "%" + length + "s";
		}
		try {
			if (isChinese(value)) {
				result = String.format(format,
						new String(value.getBytes(afterFormat), beforeFormat));
				result = new String(result.getBytes(beforeFormat), afterFormat);
			} else {
				result = String.format(format, value);
			}
			if (!" ".equals(fixTag) && !"".equals(fixTag)) {
				result = result.replaceAll(" ", fixTag);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return result;
	}

	public static List<String> process(String date1, String date2) {
		List<String> dateList = new ArrayList<String>();
		// 开始日期
		dateList.add(date1);

		if (date1.equals(date2)) {
			System.out.println("两个日期相等!");
			return dateList;
		}

		String tmp;
		if (date1.compareTo(date2) > 0) { // 确保 date1的日期不晚于date2
			tmp = date1;
			date1 = date2;
			date2 = tmp;
		}

		tmp = format2.format(str2Date(date1).getTime() + 3600 * 24 * 1000);

		int num = 0;
		while (tmp.compareTo(date2) < 0) {
			// System.out.println(tmp);
			dateList.add(tmp);
			num++;
			tmp = format2.format(str2Date(tmp).getTime() + 3600 * 24 * 1000);
		}

		// 结束日期
		dateList.add(date2);
		if (num == 0)
			System.out.println("两个日期相邻!");

		return dateList;
	}

	/**
	 * yyyy-MM-dd
	 * @param str
	 * @return
	 */
	public static Date str2Date(String str) {
		if (str == null)
			return null;

		try {
			return format2.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @param str
	 * @return
	 */
	public static Date str2DateTime(String str) {
		if (str == null)
			return null;

		try {
			return format1.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 比较两个字符串日期大小
	 * 
	 * @param date1
	 *            yyyy-MM-dd
	 * @param date2
	 *            yyyy-MM-dd
	 * 
	 * @return int 1：data1>date2 -1：date1<dat2 0:date1==date2
	 */
	public static int compareDate(String date1, String date2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				System.out.println("dt1 在dt2后");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				System.out.println("dt1在dt2前");
				return -1;
			} else {
				System.out.println("dt1在dt2 相等");
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 获取日期的当天开始时间 .<p>
	 * @param date			日期
	 * @return				当天开始时间
	 */
	public static Date getBeginDate(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
		        0, 0);
		return calendar.getTime();
	}
	
	/**
	 * 日期增加或减少天数 .<p>
	 * @param date			日期
	 * @param day			增加或减少天数
	 * @return				日期
	 */
	public static Date addDay(final Date date, final int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
		        0, 0);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}
	
	/**
	 * 获取日期的下一天开始时间.<p>
	 * @param date			日期
	 * @return				下一天开始时间
	 */
	public static Date getNextDay(final Date date) {
		return addDay(date, 1);
	}
	
	/**
	 * 获取日期的上一天开始时间.<p>
	 * @param date			日期
	 * @return				上一天开始时间
	 */
	public static Date getLastDay(final Date date) {
		return addDay(date, -1);
	}
	
	/**
	 * 日期转换为yyyyMMdd格式.
	 * @param date			日期
	 * @return				日期值
	 */
	public static int transDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return Integer.parseInt(format.format(date));
	}
	
	/**
	 * 获取服务器系统时间.
	 * @return			服务器系统时间
	 */
	public static Date getSystemDate() {
		return new Date();
	}

	/**
	 * 完整的判断中文汉字和符号
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isChinese(String str) {
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/**
	 * 获得一个UUID 32位，25c9167228cf44fc813b9a282372d72d
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}

	/**
	 * 获得指定数目的UUID
	 * 
	 * @param number
	 *            int 需要获得的UUID数量
	 * @return String[] UUID数组
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}
	
	/**
	 * 将时间戳转换为时间
	 * @param s
	 * @return
	 */
	public static Date stampToDate(String s){
		Date date =new Date();
		if(!CommUtils.isEmpty(s)){
			long lt = new Long(s);
			date = new Date(lt);
		}
        return date;
    }
	
	/////////////////////bjui/////////////////////
	// 时间戳转换为日期格式
		public String TimeStampToDate(String timestampString, String formats) {
			Long timestamp = Long.parseLong(timestampString) * 1000;
			String date = new SimpleDateFormat(formats).format(new Date(timestamp));
			return date;
		}

		/**
		 * 将某个日期以固定格式转化成字符串
		 * 
		 * @param date
		 * @return String
		 */
		public static String dateToStr(java.util.Date date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String str = sdf.format(date);
			return str;
		}

		/**
		 * 判断任意一个整数是否素数
		 * 
		 * @param n
		 * @return boolean
		 */
		public static boolean isPrimes(int n) {
			for (int i = 2; i <= Math.sqrt(n); i++) {
				if (n % i == 0) {
					return false;
				}
			}
			return true;
		}

		/**
		 * 获得任意一个整数的阶乘，递归
		 * 
		 * @param n
		 * @return n!
		 */
		public static int factorial(int n) {
			if (n == 1) {
				return 1;
			}
			return n * factorial(n - 1);
		}

		/**
		 * 将指定byte数组以16进制的形式打印到控制台
		 * 
		 * @param hint
		 *            String
		 * @param b
		 *            byte[]
		 * @return void
		 */
		public static void printHexString(String hint, byte[] b) {
			System.out.print(hint);
			for (int i = 0; i < b.length; i++) {
				String hex = Integer.toHexString(b[i] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				System.out.print(hex.toUpperCase() + " ");
			}
			System.out.println("");
		}

		/**
		 * 分割字符串
		 * 
		 * @param str
		 *            String 原始字符串
		 * @param splitsign
		 *            String 分隔符
		 * @return String[] 分割后的字符串数组
		 */
		public static String[] split(String str, String splitsign) {
			int index;
			if (str == null || splitsign == null)
				return null;
			ArrayList<String> al = new ArrayList<String>();
			while ((index = str.indexOf(splitsign)) != -1) {
				al.add(str.substring(0, index));
				str = str.substring(index + splitsign.length());
			}
			al.add(str);
			return (String[]) al.toArray(new String[0]);
		}

		/**
		 * 替换字符串
		 * 
		 * @param from
		 *            String 原始字符串
		 * @param to
		 *            String 目标字符串
		 * @param source
		 *            String 母字符串
		 * @return String 替换后的字符串
		 */
		public static String replace(String from, String to, String source) {
			if (source == null || from == null || to == null)
				return null;
			StringBuffer bf = new StringBuffer("");
			int index = -1;
			while ((index = source.indexOf(from)) != -1) {
				bf.append(source.substring(0, index) + to);
				source = source.substring(index + from.length());
				index = source.indexOf(from);
			}
			bf.append(source);
			return bf.toString();
		}

		/**
		 * 替换字符串，能能够在HTML页面上直接显示(替换双引号和小于号)
		 * 
		 * @param str
		 *            String 原始字符串
		 * @return String 替换后的字符串
		 */
		public static String htmlencode(String str) {
			if (str == null) {
				return null;
			}

			return replace("\"", "&quot;", replace("<", "&lt;", str));
		}

		/**
		 * 替换字符串，将被编码的转换成原始码（替换成双引号和小于号）
		 * 
		 * @param str
		 *            String
		 * @return String
		 */
		public static String htmldecode(String str) {
			if (str == null) {
				return null;
			}

			return replace("&quot;", "\"", replace("&lt;", "<", str));
		}

		private static final String _BR = "<br/>";

		/**
		 * 在页面上直接显示文本内容，替换小于号，空格，回车，TAB
		 * 
		 * @param str
		 *            String 原始字符串
		 * @return String 替换后的字符串
		 */
		public static String htmlshow(String str) {
			if (str == null) {
				return null;
			}

			str = replace("<", "&lt;", str);
			str = replace(" ", "&nbsp;", str);
			str = replace("\r\n", _BR, str);
			str = replace("\n", _BR, str);
			str = replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", str);
			return str;
		}

		/**
		 * 返回指定字节长度的字符串
		 * 
		 * @param str
		 *            String 字符串
		 * @param length
		 *            int 指定长度
		 * @return String 返回的字符串
		 */
		public static String toLength(String str, int length) {
			if (str == null) {
				return null;
			}
			if (length <= 0) {
				return "";
			}
			try {
				if (str.getBytes("GBK").length <= length) {
					return str;
				}
			} catch (Exception ex) {
			}
			StringBuffer buff = new StringBuffer();

			int index = 0;
			char c;
			length -= 3;
			while (length > 0) {
				c = str.charAt(index);
				if (c < 128) {
					length--;
				} else {
					length--;
					length--;
				}
				buff.append(c);
				index++;
			}
			buff.append("");
			return buff.toString();
		}

		/**
		 * 判断是否为整数
		 * 
		 * @param str
		 *            传入的字符串
		 * @return 是整数返回true,否则返回false
		 */
		public static boolean isInteger(String str) {
			Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
			return pattern.matcher(str).matches();
		}

		/**
		 * 判断是否为浮点数，包括double和float
		 * 
		 * @param str
		 *            传入的字符串
		 * @return 是浮点数返回true,否则返回false
		 */
		public static boolean isDouble(String str) {
			Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
			return pattern.matcher(str).matches();
		}

		/**
		 * 判断输入的字符串是否符合Email样式.
		 * 
		 * @param str
		 *            传入的字符串
		 * @return 是Email样式返回true,否则返回false
		 */
		public static boolean isEmail(String str) {
			Pattern pattern = Pattern
					.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
			return pattern.matcher(str).matches();
		}

		/**
		 * 判断输入的字符串是否为纯汉字
		 * 
		 * @param str
		 *            传入的字符窜
		 * @return 如果是纯汉字返回true,否则返回false
		 */
		/*public static boolean isChinese(String str) {
			Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
			return pattern.matcher(str).matches();
		}*/

		/**
		 * 是否为空白,包括null和""
		 * 
		 * @param str
		 * @return
		 */
		public static boolean isBlank(String str) {
			return str == null || str.trim().length() == 0;
		}

		/**
		 * 判断是否为质数
		 * 
		 * @param x
		 * @return
		 */
		public static boolean isPrime(int x) {
			if (x <= 7) {
				if (x == 2 || x == 3 || x == 5 || x == 7)
					return true;
			}
			int c = 7;
			if (x % 2 == 0)
				return false;
			if (x % 3 == 0)
				return false;
			if (x % 5 == 0)
				return false;
			int end = (int) Math.sqrt(x);
			while (c <= end) {
				if (x % c == 0) {
					return false;
				}
				c += 4;
				if (x % c == 0) {
					return false;
				}
				c += 2;
				if (x % c == 0) {
					return false;
				}
				c += 4;
				if (x % c == 0) {
					return false;
				}
				c += 2;
				if (x % c == 0) {
					return false;
				}
				c += 4;
				if (x % c == 0) {
					return false;
				}
				c += 6;
				if (x % c == 0) {
					return false;
				}
				c += 2;
				if (x % c == 0) {
					return false;
				}
				c += 6;
			}
			return true;
		}

		/**
		 * 全角字符转半角字符
		 * 
		 * @param QJStr
		 * @return String
		 */
		public static final String QJToBJChange(String QJStr) {
			char[] chr = QJStr.toCharArray();
			String str = "";
			for (int i = 0; i < chr.length; i++) {
				chr[i] = (char) ((int) chr[i] - 65248);
				str += chr[i];
			}
			return str;
		}

		/**
		 * 去掉字符串中重复的子字符串
		 * 
		 * @param str
		 * @return String
		 */
		public static String removeSameString(String str) {
			Set<String> mLinkedSet = new LinkedHashSet<String>();
			String[] strArray = str.split(" ");
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < strArray.length; i++) {
				if (!mLinkedSet.contains(strArray[i])) {
					mLinkedSet.add(strArray[i]);
					sb.append(strArray[i] + " ");
				}
			}
			System.out.println(mLinkedSet);
			return sb.toString().substring(0, sb.toString().length() - 1);
		}

		// 过滤特殊字符
		public static String encoding(String src) {
			if (src == null)
				return "";
			StringBuilder result = new StringBuilder();
			if (src != null) {
				src = src.trim();
				for (int pos = 0; pos < src.length(); pos++) {
					switch (src.charAt(pos)) {
					case '\"':
						result.append("&quot;");
						break;
					case '<':
						result.append("&lt;");
						break;
					case '>':
						result.append("&gt;");
						break;
					case '\'':
						result.append("&apos;");
						break;
					case '&':
						result.append("&amp;");
						break;
					case '%':
						result.append("&pc;");
						break;
					case '_':
						result.append("&ul;");
						break;
					case '#':
						result.append("&shap;");
						break;
					case '?':
						result.append("&ques;");
						break;
					default:
						result.append(src.charAt(pos));
						break;
					}
				}
			}
			return result.toString();
		}

		// 反过滤特殊字符
		public static String decoding(String src) {
			if (src == null)
				return "";
			String result = src;
			result = result.replace("&quot;", "\"").replace("&apos;", "\'");
			result = result.replace("&lt;", "<").replace("&gt;", ">");
			result = result.replace("&amp;", "&");
			result = result.replace("&pc;", "%").replace("&ul", "_");
			result = result.replace("&shap;", "#").replace("&ques", "?");
			return result;
		}

		/**
		 * 人民币转成大写
		 * 
		 * @param value
		 * @return String
		 */
		public static String hangeToBig(double value) {
			char[] hunit = { '拾', '佰', '仟' }; // 段内位置表示
			char[] vunit = { '万', '亿' }; // 段名表示
			char[] digit = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }; // 数字表示
			long midVal = (long) (value * 100); // 转化成整形
			String valStr = String.valueOf(midVal); // 转化成字符串

			String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
			String rail = valStr.substring(valStr.length() - 2); // 取小数部分

			String prefix = ""; // 整数部分转化的结果
			String suffix = ""; // 小数部分转化的结果
			// 处理小数点后面的数
			if (rail.equals("00")) { // 如果小数部分为0
				suffix = "整";
			} else {
				suffix = digit[rail.charAt(0) - '0'] + "角"
						+ digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
			}
			// 处理小数点前面的数
			char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
			char zero = '0'; // 标志'0'表示出现过0
			byte zeroSerNum = 0; // 连续出现0的次数
			for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
				int idx = (chDig.length - i - 1) % 4; // 取段内位置
				int vidx = (chDig.length - i - 1) / 4; // 取段位置
				if (chDig[i] == '0') { // 如果当前字符是0
					zeroSerNum++; // 连续0次数递增
					if (zero == '0') { // 标志
						zero = digit[0];
					} else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
						prefix += vunit[vidx - 1];
						zero = '0';
					}
					continue;
				}
				zeroSerNum = 0; // 连续0次数清零
				if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
					prefix += zero;
					zero = '0';
				}
				prefix += digit[chDig[i] - '0']; // 转化该数字表示
				if (idx > 0)
					prefix += hunit[idx - 1];
				if (idx == 0 && vidx > 0) {
					prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
				}
			}

			if (prefix.length() > 0)
				prefix += '圆'; // 如果整数部分存在,则有圆的字样
			return prefix + suffix; // 返回正确表示

		}

		/**
		 * 获得随机数
		 * 
		 * @param * num
		 * @return
		 */
		public static String getRandomStrNum(long num) {
			return formatStringPrefix(((long) (Math.random() * num)) + "",(""+num).length()-1);
		}
		/**
		 * 获得文件名
		 * @param num
		 * @param extFString
		 * @return
		 */
		public static String getNewFileName(long num,String extFString){
			StringBuilder sb = new StringBuilder();
			if(num>0 && !CommUtils.isEmpty(extFString)){
				sb.append(System.currentTimeMillis())
				  .append("_")
				  .append(getRandomStrNum(num))
				  .append(extFString);
			}
			return sb.toString();
		}
		
		public static String getNewSmallFileName(String fileName){
			StringBuilder sb = new StringBuilder();
			String fileNamePrefix = fileName.substring(0,fileName.lastIndexOf("."));
//			String extName = fileName.substring(fileName.lastIndexOf("."), fileName.length());
			sb.append(fileNamePrefix)
			  .append("_s")
//			  .append(extName);
			  .append(".jpg");
			
			return sb.toString();
		}
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object parseObject(String json, Class entityClass) {
		return JSON.parseObject(json, entityClass, Feature.InitStringFieldAsEmpty);
	}

	public String ConvertObjectToJson(Object obj) {
		String json = "";
		if (!CommUtils.isObjEmpty(obj)) {
			json = JSON.toJSONString(obj);
		}
		return json;
	}
	
	public static void copyProperties(Object dest, Object orig) throws Exception{
		ConvertUtils.register(new BeanConverter(), java.lang.Byte.class);
		ConvertUtils.register(new BeanConverter(), java.lang.Short.class);
		ConvertUtils.register(new BeanConverter(), java.lang.Integer.class);
		ConvertUtils.register(new BeanConverter(), java.lang.Long.class);
		ConvertUtils.register(new BeanConverter(), java.lang.Double.class);
		ConvertUtils.register(new BeanConverter(), java.lang.String.class);
		ConvertUtils.register(new BeanConverter(), java.util.Date.class);
		BeanUtils.copyProperties(dest, orig);
	}
	
	/**
	 * 是否含有特殊字符
	 * @param str
	 * @return
	 */
	public static boolean isSpecialChar(String str) {
		boolean flag = false;
		if (str == null || str.equals("")) {
			flag = false;
        }
		
		String regEx = "[''<>/……——‘”“’]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		flag = m.find();
		if(flag){
			return flag;
		}

		
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = emoji.matcher(str);
        flag = emojiMatcher.find();
        if(flag){
			return flag;
		}
        
        return flag;
       
	}
		
	public static void main(String[] args) {
		System.out.println("123456".hashCode());
		System.out.println(getMD5WithSalt("123456"));
		System.out.println(encryptSHA("123456"));
		System.out.println(getMD5("admin"));
	}

}
