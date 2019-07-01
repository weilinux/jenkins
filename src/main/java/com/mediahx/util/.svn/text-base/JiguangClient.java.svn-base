package com.mediahx.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.utils.StringUtils;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

/***
 * 
 * @author feihang
 *
 */
public class JiguangClient {
    private static final Logger log = LoggerFactory.getLogger(JiguangClient.class);
	private static final String MASTER_SECRET = "jiguang.masterSecret";
	private static final String APP_KEY = "jiguang.appKey";
	private static final String TIME_TO_LIVE = "jiguang.timeToLive";
	private static final String ENV = "jiguang.env";
	
	private static String masterSecret = "";
	private static String appKey = "";
	private static int timeToLive = -1;
	private static Boolean env = null;

	// 测试main方法
	public static void main(String[] args) {
		String alias = "a6fc06fea3304ac4a2b4c2a573dea202";// 声明别名
		log.info("对别名" + alias + "的用户推送信息");
		// 注意 推送环境，失效时间，密钥，appkey得从配置文件配置
//		PushResult result = pushByAlias(alias, "huang发消息到APP");
		PushResult result = pushNotificationByAlias(alias, "这是通知的内容!", "通知的TITLE","", "");
		if (result != null && result.isResultOK()) {
			log.info("针对别名" + alias + "的信息推送成功！");
		} else {
			log.info("针对别名" + alias + "的信息推送失败！");
		}
	}

	/**
     * @param env  环境（true-推送生产环境 false-推送开发环境）
     * @param timeToLive 消息在JPush服务器的失效时间（秒）
     * @param masterSecret 极光上注册的应用密钥
     * @param appKey 极光上注册的应用appKey
	 */
	public static void initJiguangInfo() {
		masterSecret = PropertiesUtil.getPropertyForString(MASTER_SECRET);
		appKey = PropertiesUtil.getPropertyForString(APP_KEY);
		timeToLive = PropertiesUtil.getPropertyForInt(TIME_TO_LIVE);
		String envStr = PropertiesUtil.getPropertyForString(ENV);
		if (!StringUtils.isEmpty(envStr)) {
			env = Boolean.parseBoolean(envStr);
		}
	}

    /***
     * 以别名的方式发送自定义消息
     * @param alias 别名值
     * @param content 消息内容
     * @return
     */
	public static PushResult pushByAlias(String alias, String content) {
		if (StringUtils.isEmpty(masterSecret)) {
			initJiguangInfo();
		}
		if (!StringUtils.isEmpty(masterSecret) && !StringUtils.isEmpty(appKey) && timeToLive >= 0 && env != null) {
			ClientConfig clientConfig = ClientConfig.getInstance();
			JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
			PushPayload payload = buildMessagePushByAlias(alias, content, env, timeToLive);
			try {
				PushResult result = jpushClient.sendPush(payload);
				return result;
			} catch (APIConnectionException e) {
				log.error("Connection error. Should retry later. ", e);
				return null;
			} catch (APIRequestException e) {
				log.error("Error response from JPush server. Should review and fix it. ", e);
				log.info("HTTP Status: " + e.getStatus());
				log.info("Error Code: " + e.getErrorCode());
				log.info("Error Message: " + e.getErrorMessage());
				log.info("Msg ID: " + e.getMsgId());
				return null;
			} finally {
				jpushClient.close();
			}
		} else {
			log.error("**************************Can't got jiguang config info from properties file, masterSecret : "
					+ masterSecret + " appKey: " + appKey + " timeToLive: " + timeToLive + " env: " + env);
			return null;
		}
		
    }
	
	
    /***
     * 以别名的方式发送通知消息
     * @param alias 别名值
     * @param content 消息内容
     * @param extraKey 附加字段key
     * @param extraValue 附加字段值
     * @return
     */
	public static PushResult pushNotificationByAlias(String alias, String content, String title,String extraKey, String extraValue) {
		if (StringUtils.isEmpty(masterSecret)) {
			initJiguangInfo();
		}
		if (!StringUtils.isEmpty(masterSecret) && !StringUtils.isEmpty(appKey) && timeToLive >= 0 && env != null) {
			ClientConfig clientConfig = ClientConfig.getInstance();
			JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
			PushPayload payload = buildNotificationPushByAlias(alias, content, title, env, timeToLive, extraKey, extraValue);
			try {
				PushResult result = jpushClient.sendPush(payload);
				return result;
			} catch (APIConnectionException e) {
				log.error("Connection error. Should retry later. ", e);
				return null;
			} catch (APIRequestException e) {
				log.error("Error response from JPush server. Should review and fix it. ", e);
				log.info("HTTP Status: " + e.getStatus());
				log.info("Error Code: " + e.getErrorCode());
				log.info("Error Message: " + e.getErrorMessage());
				log.info("Msg ID: " + e.getMsgId());
				return null;
			} finally {
				jpushClient.close();
			}
		} else {
			log.error("**************************Can't got jiguang config info from properties file, masterSecret : "
					+ masterSecret + " appKey: " + appKey + " timeToLive: " + timeToLive + " env: " + env);
			return null;
		}
		
    }


    /***
     * 以注册ID的方式发送自定义消息
     * @param registrationId 注册ID值
     * @param content 消息内容
     * @param env  环境（true-推送生产环境 false-推送开发环境）
     * @param timeToLive 消息在JPush服务器的失效时间（秒）
     * @param masterSecret 极光上注册的应用密钥
     * @param appKey 极光上注册的应用appKey
     * @return
     */
    public static PushResult pushByRegistrationId(String registrationId, String content){
		if (StringUtils.isEmpty(masterSecret)) {
			initJiguangInfo();
		}
		if (!StringUtils.isEmpty(masterSecret) && !StringUtils.isEmpty(appKey) && timeToLive >= 0 && env != null) {
			ClientConfig clientConfig = ClientConfig.getInstance();
			JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
			PushPayload payload = buildMessagePushByRegistrationId(registrationId, content, env, timeToLive);
			try {
				PushResult result = jpushClient.sendPush(payload);
				jpushClient.close();
				return result;
			} catch (APIConnectionException e) {
				log.error("Connection error. Should retry later. ", e);
				return null;
			} catch (APIRequestException e) {
				log.error("Error response from JPush server. Should review and fix it. ", e);
				log.info("HTTP Status: " + e.getStatus());
				log.info("Error Code: " + e.getErrorCode());
				log.info("Error Message: " + e.getErrorMessage());
				log.info("Msg ID: " + e.getMsgId());
				return null;
			}
		} else {
			log.error("**************************Can't got jiguang config info from properties file, masterSecret : "
					+ masterSecret + " appKey: " + appKey + " timeToLive: " + timeToLive + " env: " + env);
			return null;
		}
    }
    
    
    
    
    
    
    /***
     * 以别名的方式生成自定义消息极光推送对象PushPayload
     * @param alias 别名
     * @param message 自定义消息内容
     * @param env  环境（true-推送生产环境 false-推送开发环境）
     * @param timeToLive 消息在JPush服务器的失效时间（秒）
     * @return
     */
    public static PushPayload buildMessagePushByAlias(String alias,String message, boolean env, int timeToLive){
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setMessage(Message.newBuilder().setMsgContent(message).addExtra("from", "JPush").build())
                .setOptions(Options.newBuilder().setApnsProduction(env).setTimeToLive(timeToLive).build())
                .build();
    }
    
    
    /**
     * 以注册ID的方式生成自定义消息极光推送对象PushPayload
     * @param registrationId 注册ID
     * @param message 自定义消息内容
     * @param env  环境（true-推送生产环境 false-推送开发环境）
     * @param timeToLive 消息在JPush服务器的失效时间（秒）
     * @return
     */
    public static PushPayload buildMessagePushByRegistrationId(String registrationId,String message, boolean env, int timeToLive){
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(registrationId))
                .setMessage(Message.newBuilder().setMsgContent(message).addExtra("from", "JPush").build())
                .setOptions(Options.newBuilder().setApnsProduction(env).setTimeToLive(timeToLive).build())
                .build();
    }
    
    
    /***
     *  以别名的方式生成通知极光推送对象PushPayload
     * @param alias 别名
     * @param alert 通知的内容
     * @param env 环境（true-推送生产环境 false-推送开发环境）
     * @param timeToLive 消息在JPush服务器的失效时间（秒）
     * @param extraKey 附加字段key
     * @param extraValue 附加字段值
     * @return
     */
    public static PushPayload buildNotificationPushByAlias(String alias,String alert, String title, boolean env, int timeToLive, String extraKey, String extraValue){
    	JsonObject obj = new JsonObject();
    	obj.addProperty("body", alert);
    	obj.addProperty("title", title);
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().addExtra(extraKey, extraValue).setAlert(alert).setTitle(title).build())
                        .addPlatformNotification(IosNotification.newBuilder().addExtra(extraKey, extraValue).setAlert(obj).build()).build())
                .setOptions(Options.newBuilder().setApnsProduction(env).setTimeToLive(timeToLive).build())
                .build();
    }
    
    
    /***
     *  以注册ID的方式生成通知极光推送对象PushPayload
     * @param registrationId 注册ID
     * @param alert 通知的内容
     * @param env 环境（true-推送生产环境 false-推送开发环境）
     * @param timeToLive 消息在JPush服务器的失效时间（秒）
     * @return
     */
    public static PushPayload buildNotificationPushByRegistrationId(String registrationId,String alert, boolean env, int timeToLive){
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.registrationId(registrationId))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().addExtra("type", "infomation").setAlert(alert).build())
                        .addPlatformNotification(IosNotification.newBuilder().addExtra("type", "infomation").setAlert(alert).build()).build())
                .setOptions(Options.newBuilder().setApnsProduction(env).setTimeToLive(timeToLive).build())
                .build();
    }
}
