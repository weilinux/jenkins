package com.mediahx.util;

import java.math.BigDecimal;

/**
 * 
 * @author ZHE
 *
 */
public class Arith {
	
	//精确至两位
	public static final int ROUND_RANGE = 2;
	
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	// 这个类不能实例化
	private Arith() {

	}

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */

	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	
	public static String sub2(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1+"");
		BigDecimal b2 = new BigDecimal(v2+"");
		return b1.subtract(b2)+"";
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 截取至小数点后几位
	 * 
	 * @param v
	 *            需要截取数字
	 * @param scale
	 *           
	 * @return  小数点后保留几位 （其余舍弃，不四舍五入）
	 */
	public static double abnegateRound(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_DOWN).doubleValue();
	}
	
	/** 
     * 使用java正则表达式去掉多余的.与0 
     * @param s 
     * @return  
     */  
    public static String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }
	
    /**
     * 小数位小于0.5按照0.5计算，大于0.5按照1.0计算 ,等于0.5则不变
     * 	(返回一位小数)
     * @param s
     * @return
     */
    public static double roundfive(double s){
    	
    	s = Double.valueOf((s+"").substring(0,(s+"").indexOf(".")+2));//截取到小数第一位
    	if(s % 1.0 == 0){
    		//整数则返回
			return s;
		}else{
			String str = s+"";
			//取前面整数
			double dotUp = Double.valueOf(str.substring(0,str.indexOf(".")));
			//获取小数点后一位
			double dotNext = Double.valueOf(str.substring(str.indexOf(".")+1,str.indexOf(".")+2));
			if(dotNext == 5){
				s = add(dotUp, 0.5);
			}else if(dotNext > 5){
				s = add(dotUp, 1);
			}else if(dotNext < 5){
				s = add(dotUp, 0.5);
			}
		}
    	return s;
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
   	public static Double reservedDecimal(String num, int scale) {

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
   		result = clearBoth(result);
   		return Double.valueOf(result);
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
    
}