package com.mediahx.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量
 * 
 * @author feihang
 *
 */
public class Constants {
	
	public static class ORDER {

		/** 订单状态-已成功 */
		public static final String STATUS_SUCCEED = "succeed";// 已成功
		/** 订单状态-拼场中*/
		public static final String STATUS_MATCHING = "matching";// 拼场中
		/** 订单状态-已失败*/
		public static final String STATUS_FAILED = "failed";// 已失败
		/** 订单状态-已完成 */
		public static final String STATUS_FINISHED = "finished";// 已完成
		
		/** 订单类型-包场 */
		public static final String CLASS_GROUP = "group";// 包场
		/** 订单类型-组队 */
		public static final String CLASS_MATCH = "match";// 组队
		
		/** 支付状态-未支付 */
		public static final String PAY_STATUS_UNPAID = "unpaid";// 未支付
		
		/** 免费类型 -朋友friend*/
		public static final String 	FREE_TYPE_FRIEND = "friend";
		/** 免费类型-路人 stranger*/
		public static final String 	FREE_TYPE_STRANGER = "stranger";
		
		/** 订单类型中文对照map */
		public final static Map<String, String> STATUS_NAME = new HashMap<String, String>();
		
		 static{
			 STATUS_NAME.put(STATUS_SUCCEED, "已成功");
			 STATUS_NAME.put(STATUS_MATCHING, "拼场中");
			 STATUS_NAME.put(STATUS_FAILED, "已失败");
			 STATUS_NAME.put(STATUS_FINISHED, "已完成");
		 }
		
	}

	/**
	 * 常量Y
	 */
	public static final String CONSTANTS_Y = "Y";

	/**
	 * 常量N
	 */
	public static final String CONSTANTS_N = "N";
	
	/**
	 * 常量D
	 */
	public static final String CONSTANTS_D = "D";
	
	/**
	 * 小程序解密 appId
	 */
	public static final String MIN_PROGRAM_APP_ID = "wx72a2fb68791a9bf8";
	
	/**
	 * 6位
	 */
	public static final long SIX_POS = 1000000;
	
	/**
	 * 默认用户头像
	 */
	public static final String DEFAULT_PHOTO = "/toysburg/upload/userinfo/defaultphoto101.jpeg";
	
	/**
	 * 小程序formId 的有效时间7天
	 */
	public static final Integer MINI_VALID_FORMID_TIME = 7*24*60*60;
	
	/**
	 * 小程序token 的有效时间2小时
	 */
	public static final Integer MINI_VALID_TOKEN_TIME = 7200;
	
	/**
	 * 未支付订单的有效时间
	 */
	public static final Integer UNPAID_ORDER_VALIDITY_TIME = 300;


}
