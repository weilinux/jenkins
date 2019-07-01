package com.mediahx.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.icu.math.BigDecimal;

/**
 * @author : jiangjinpeng
 * @date : 2018-11-02
 * @comment : 梦幻城堡-业务规则生成辅助处理类
 */
@Component
public final class LogicUtils {
	
	@Autowired
	 RedisClient redisClient;
	
	private static final String DATE_FORMAT = "yyyyMMdd";
	private static final String DATE_FORMAT_T = "yyyyMMddHHmmss";
	private static ThreadLocal<SimpleDateFormat> DATE_FORMAT_THREAD = new ThreadLocal<SimpleDateFormat>();

	// 配置读取
	static Properties config = PropertiesUtil.getProperties("properties/config.properties");

	/**
	 * 用于生成订单编号的日期部分的格式化
	 * 
	 * @author : jjp
	 * @return
	 */
	public static SimpleDateFormat getDateFormat() {
		SimpleDateFormat df = DATE_FORMAT_THREAD.get();
		if (df == null) {
			df = new SimpleDateFormat(DATE_FORMAT_T);
			DATE_FORMAT_THREAD.set(df);
		}
		return df;
	}

	/**
	 * 获取APP端底部显示标语
	 * 
	 * @author : jjp
	 * @return
	 */
	public static String getAppTips1(String... param) {

		String msgTemp = config.getProperty("app_tips_msg_1");
		for (int i = 0; i < param.length; i++) {
			msgTemp = msgTemp.replace("@" + (i + 1) + "@", param[i]);
		}

		return msgTemp;
	}

	/**
	 * 获取点赞数字符串
	 * 
	 * @author : jjp
	 * @param likesNum
	 * @return
	 */
	public static String getLikesStr(Integer likesNum) {

		String likesStr = "";
		if (likesNum != null && String.valueOf(likesNum).length() > 4) {
			BigDecimal big = new BigDecimal(likesNum);
			big = big.movePointLeft(4);
			big = big.setScale(1, BigDecimal.ROUND_DOWN);

			String bigStr = big.toString();
			// 处理小数点后保留0的现象
			while (bigStr.indexOf(".") != -1 && (bigStr.endsWith("0") || bigStr.endsWith("."))) {
				if (bigStr.endsWith(".0")) {
					bigStr = bigStr.substring(0, bigStr.length() - 2);
				} else if (bigStr.endsWith(".")) {
					bigStr = bigStr.substring(0, bigStr.length() - 1);
				} else if (bigStr.endsWith("0")) {
					bigStr = bigStr.substring(0, bigStr.length() - 1);
				}
			}

			likesStr = bigStr + "w";
			return likesStr;
		} else {
			return String.valueOf(likesNum);
		}

	}

	/**
	 * 根据店铺ID(shop_id)生成订单编号(order_no)
	 * 
	 * @author : jjp
	 * @param shopCode
	 *            店铺ID
	 * @return
	 */
	
	
	public String createOrderCode() {
		// 订单编号生成规则：店铺编号+年月日+4位随机数
		String ranStr = genNo(4);
		String orderCode = getDateFormat().format(new Date()) + ranStr;
		return orderCode;
	}
	
	/**
	 * 制作某一天的暗号
	 * 暗号规则：每个场馆每天从01开始递增
	 * @return
	 */
	public String  secretSignal(Date d,String stadiumId){
		String num = null;
		int time = secondsTime(d).intValue();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);//Date指定格式：yyyyMMdd
		String dateStr = simpleDateFormat.format(d);

		num = redisClient.get(dateStr+"_"+stadiumId);
		
		if (num == null){
			num ="01";
			redisClient.set(dateStr+"_"+stadiumId, "1",time);
		} else {
			int number = Integer.valueOf(num).intValue();
			if ((number+1)<10){
				num = "0"+(number+1);
				String n = (number+1)+"";
				redisClient.set(dateStr+"_"+stadiumId, n,time);

			} else{
				num = (number+1)+"";
				redisClient.set(dateStr+"_"+stadiumId, num,time);

			} 
		}
		
		return num;
		
	}
	
	/**
	 * 当下时间距离某天结束时间的秒数
	 * @return
	 */
	private  Integer secondsTime(Date d) {
		//获取结束时间
		Date end = DateUtils.getDayEndTime(d);
		//当下时间
		Date now = new Date();
		
		//计算当下时间距离结束时间的秒数
		long milli = DateUtils.dateDiff(now,end);
		int second = (int) (milli/1000);
		return second;
		
	}

	/**
	 * 保留指定长度的小数位
	 * 
	 * @param num
	 *            待保留小数位数字的字符串格式
	 * @param scale
	 *            待保留小数位数
	 * @return
	 */
	public static String reservedDecimal(String num, int scale) {

		String result = num;

		String tmpStr = String.valueOf(num);
		if (tmpStr.indexOf(".") != -1) {
			// 存在小数部分,检查小数位数是否满足长度要求
			int checkLength = tmpStr.substring(tmpStr.lastIndexOf("."), tmpStr.length()).length();
			if (checkLength > scale) {
				String savePart = tmpStr.substring(0, tmpStr.lastIndexOf("."));
				String modifyPart = tmpStr.substring(tmpStr.lastIndexOf("."), tmpStr.length());
				result = savePart + modifyPart.substring(0, checkLength - (checkLength - scale) + 1);
			} else {
				// 现有精度小于保留精度,不做任何处理
			}
		}
		return result;
	}

	/**
	 * 清除数字字符串后面的无效"0"或者"."
	 * 
	 * @param num
	 *            待格式化的数字的字符串格式
	 * @return
	 */
	public static String clearBoth(String num) {

		if (num.indexOf(".") > 0) {
			num = num.replaceAll("0+?$", "");
			num = num.replaceAll("[.]$", "");
		}

		return num;
	}

	/**
	 * 根据当前数字左补0生成一个编号
	 * 
	 * @param length
	 *            待生成随机数长度
	 * @return
	 */
	private static String genNo(int length) {

		int rootNum = -1;
		Random random = new Random();
		if (length < 2) {
			return String.valueOf(random.nextInt(10));
		} else {
			rootNum = (int) Math.pow(10, length);
		}

		rootNum = random.nextInt(rootNum);
		int missLen = length - String.valueOf(rootNum).length();
		Stream<String> numStream = Stream.iterate("0", p -> {
			return "0";
		});
		StringBuffer numStr = numStream.limit(missLen).collect(() -> new StringBuffer(), (oldVal, newVal) -> {
			oldVal.append(newVal);
		}, (val1, val2) -> {
			val1.append(val2);
		});

		return numStr.append(rootNum).toString();
	}

}
