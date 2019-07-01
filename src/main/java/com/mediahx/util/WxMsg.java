package com.mediahx.util;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.mediahx.constant.Constants;
import com.mediahx.constant.RespCode;
import com.mediahx.controller.BaseController;
import com.mediahx.dao.mysql.OrderMapper;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.model.Order;
import com.mediahx.model.OrderDetail;
import com.mediahx.model.Stadium;
import com.mediahx.model.StadiumDetail;
import com.mediahx.weixin.TemplateData;
import com.mediahx.weixin.WxMssVo;

/**
 *	微信消息推送
 * @author guomf
 * 2019年5月22日
 * project： 梦幻城堡
 */
@Component
public  class WxMsg extends BaseController {
	
	private static String appid;
	private static String secret;
	@SuppressWarnings("unused")
	private static String timeout_express;
	static Properties pro = PropertiesUtil.getProperties("properties/miniprogram.properties");

	static {
		appid = pro.getProperty("weixin.appid");
		secret = pro.getProperty("weixin.secret");
		timeout_express = pro.getProperty("weixin.timeout_express");
	}
	
	private static String MatchedMsg_template_id = "oQ4--ZpPanlP404GLsgqPcnpI25obhn_8LvHf5PytRA";
	private static String MatchedMsgMore_template_id = "MbRJTkysSYaoBJwLVOiKnOEtpvDo_s7xr6yWCgIqDPk";
	private static String MatchingFailMsg_template_id = "_9RISxnWxaBzWZjt5EWATqNZ_T0uuOJsloeNGj2rL_c";
	private static String RefundMsg_template_id = "mCahu5Z4OYWl84ooeDjD2kcZUwtRRSRd5lUwaL5BMDg";
	private static String GroupMsg_template_id = "oQ4--ZpPanlP404GLsgqPcnpI25obhn_8LvHf5PytRA";
	@Autowired
	 RedisClient redisClient;
	@Autowired
	private OrderMapper orderMapper;

	/**
	 * 微信模板消息推送
	 * @param request
	 * @param out
	 * @param httpServletResponse
	 */
	public CommResp sendTemplateMessage(String orderId,String code,String mobilephone,String name) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(),
				RespCode.SUCCESS_CODE.getRetCode());
		//准备相关参数
		//接收者（用户）的 openid
		String touser = redisClient.get("openId"+orderId);
		//表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
		String formId = redisClient.get("formId"+orderId);
		if (CommUtils.isEmpty(touser) || CommUtils.isEmpty(formId)){
			resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "formId或openId不存在",
					RespCode.FAILURE_CODE.getRetCode());
			return resp;
		}
		//接口调用凭证
		String access_token = redisClient.get("mini_access_token");
		//token失效应再次获取一个新的
		if (CommUtils.isEmpty(access_token)){
			getAccessToken();
			access_token = redisClient.get("mini_access_token");
		}
		//所需下发的模板消息的id
		String template_id = "76IgA9VALEuPV6DcQHivqUMtKKTUw6lslYpT2o9Z0nM";
		
		Order order = orderMapper.selectByPrimaryKey(orderId);
		
		 //拼接推送的模版 
		WxMssVo wxMssVo = new WxMssVo(); 
		wxMssVo.setTouser(touser);//用户openid 
		wxMssVo.setTemplate_id(template_id);//模版id 
		wxMssVo.setForm_id(formId);//formid 
		//page点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
			wxMssVo.setPage("page/component/index?id=" + order.getId());
		 Map<String, TemplateData> m = new HashMap<>(7);
		//keyword1：取货码，keyword2：订单号，keyword3：商品详情，keyword4：取货地点，keyword5：温馨提示，keyword6：联系电话，keyword7：商家名
	        TemplateData keyword1 = new TemplateData();
	        TemplateData keyword2 = new TemplateData();
	        TemplateData keyword3 = new TemplateData();
	        TemplateData keyword4 = new TemplateData();
	        TemplateData keyword5 = new TemplateData();
	        TemplateData keyword6 = new TemplateData();
	        TemplateData keyword7 = new TemplateData();
	        //取货码
	        keyword1.setValue(code);
	        m.put("keyword1", keyword1);
	        //订单号
	        keyword2.setValue(order.getOrderNo());
	        m.put("keyword2", keyword2);
	        //商品详情
	        keyword3.setValue(name);
	        m.put("keyword3", keyword3);
	        //取货地点
//	        keyword4.setValue(shopInfo.getShopAddr());
	        m.put("keyword4", keyword4);
	        //温馨提示
	        keyword5.setValue("取货时核对商品明细数量");
	        m.put("keyword5", keyword5);
	        //联系电话
	        keyword6.setValue(mobilephone);
	        m.put("keyword6", keyword6);
	        //商家名
//	        keyword7.setValue(shopInfo.getShopName());
	        m.put("keyword7", keyword7);
	        
	        wxMssVo.setData(m);
	        String urll = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token;
	        RestTemplate template = new RestTemplate();
	        ResponseEntity<String> responseEntity = template.postForEntity(urll, wxMssVo, String.class);
	        logger.info("微信模板消息请求结果1："+responseEntity);
	        logger.info("微信模板消息请求结果2："+responseEntity.getBody());
	        resp.setObject(responseEntity.getBody());
			return resp;
	        
	}
	/**
	 * 满6人拼场成功、给6人发送通知
	 * @param orderId
	 * @param code
	 * @param mobilephone
	 * @param name
	 * @return
	 */
	public CommResp sendMatchedMsg(Order order, StadiumDetail stadiumDetail, Stadium stadium,String orderId) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(),
				RespCode.SUCCESS_CODE.getRetCode());
		// 接收者（用户）的 openid
		String touser = redisClient.get("openId" + orderId);
		// 表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
		String formId = redisClient.get("formId" + orderId);
		if (CommUtils.isEmpty(touser) || CommUtils.isEmpty(formId)) {
			resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "formId或openId不存在",
					RespCode.FAILURE_CODE.getRetCode());
			return resp;
		}
		// 接口调用凭证
		String access_token = redisClient.get("mini_access_token");
		// token失效应再次获取一个新的
		if (CommUtils.isEmpty(access_token)) {
			getAccessToken();
			access_token = redisClient.get("mini_access_token");
		}
		// 所需下发的模板消息的id
		String template_id = MatchedMsg_template_id;
		// 拼接推送的模版
		WxMssVo wxMssVo = new WxMssVo();
		wxMssVo.setTouser(touser);// 用户openid
		wxMssVo.setTemplate_id(template_id);// 模版id
		wxMssVo.setForm_id(formId);// formid
		//page点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
		wxMssVo.setPage("pages/index/index?type=order&id=" + order.getId());
		
		Map<String, TemplateData> m = new HashMap<>(5);
		// keyword1： 预定单号，keyword2：地址 ，keyword3： 时间，keyword4：预定类型，keyword5：温馨提示
		TemplateData keyword1 = new TemplateData();
		TemplateData keyword2 = new TemplateData();
		TemplateData keyword3 = new TemplateData();
		TemplateData keyword4 = new TemplateData();
		TemplateData keyword5 = new TemplateData();
		keyword1.setValue(order.getOrderNo());
		m.put("keyword1", keyword1);
		keyword2.setValue(stadium.getAddress());
		m.put("keyword2", keyword2);
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 ");
		String day = df.format(stadiumDetail.getDay());
		keyword3.setValue(day + order.getTime());
		m.put("keyword3", keyword3);
		if (Constants.ORDER.CLASS_GROUP.equals(order.getOrderClass())) {
			keyword4.setValue("包场");

		} else {
			keyword4.setValue("拼场");
		}
		m.put("keyword4", keyword4);
		keyword5.setValue("您的场次暗号为:" + order.getTeamCode() + "，请于" + day + order.getTime() + "到场。");
		m.put("keyword5", keyword5);

		wxMssVo.setData(m);
		String urll = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + access_token;
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> responseEntity = template.postForEntity(urll, wxMssVo, String.class);
		logger.info("微信模板消息请求结果1：" + responseEntity);
		logger.info("微信模板消息请求结果2：" + responseEntity.getBody());
		resp.setObject(responseEntity.getBody());
		return resp;
	}
	/**
	 * 大于6人加入成功：加入者通知：加入成功
	 * 包场时，加入者：加入成功通知：加入成功
	 * @param orderId
	 * @param code
	 * @param mobilephone
	 * @param name
	 * @return
	 */
	public CommResp sendMatchedMsgMore(Order order, StadiumDetail stadiumDetail,OrderDetail orderDetail,String userName) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(),
				RespCode.SUCCESS_CODE.getRetCode());
		//接收者（用户）的 openid
				String touser = redisClient.get("openId"+order.getId());
				//表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
				String formId = redisClient.get("formId"+order.getId());
				if (CommUtils.isEmpty(touser) || CommUtils.isEmpty(formId)){
					resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "formId或openId不存在",
							RespCode.FAILURE_CODE.getRetCode());
					return resp;
				}
				//接口调用凭证
				String access_token = redisClient.get("mini_access_token");
				//token失效应再次获取一个新的
				if (CommUtils.isEmpty(access_token)){
					getAccessToken();
					access_token = redisClient.get("mini_access_token");
				}
				//所需下发的模板消息的id
				String template_id = MatchedMsgMore_template_id;
				 //拼接推送的模版 
				WxMssVo wxMssVo = new WxMssVo(); 
				wxMssVo.setTouser(touser);//用户openid 
				wxMssVo.setTemplate_id(template_id);//模版id 
				wxMssVo.setForm_id(formId);//formid 
				//page点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
				wxMssVo.setPage("pages/index/index?type=order&id=" + order.getId());
				
				 Map<String, TemplateData> m = new HashMap<>(4);
					//keyword1：活动名称，keyword2：用户名，keyword3：加入时间，keyword4：温馨提示
				        TemplateData keyword1 = new TemplateData();
				        TemplateData keyword2 = new TemplateData();
				        TemplateData keyword3 = new TemplateData();
				        TemplateData keyword4 = new TemplateData();
				        
				        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
				        String day = df.format(stadiumDetail.getDay());
				        keyword1.setValue(day+order.getTime()+"的拼场");
						m.put("keyword1", keyword1);
						
						keyword2.setValue(userName);
						m.put("keyword2", keyword2);
						SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						keyword3.setValue(dff.format(orderDetail.getOrderTime()));
						m.put("keyword3", keyword3);
						keyword4.setValue("拼场暗号为:" + order.getTeamCode() + "请于" + day + order.getTime() + "到场。");
						m.put("keyword4", keyword4);
				        
						wxMssVo.setData(m);
				        String urll = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token;
				        RestTemplate template = new RestTemplate();
				        ResponseEntity<String> responseEntity = template.postForEntity(urll, wxMssVo, String.class);
				        logger.info("微信模板消息请求结果1："+responseEntity);
				        logger.info("微信模板消息请求结果2："+responseEntity.getBody());
				        resp.setObject(responseEntity.getBody());
		
		return resp;
	}
	/**
	 * 拼场失败给发起者发，改变状态发送服务通知
	 * @param orderId
	 * @param code
	 * @param mobilephone
	 * @param name
	 * @return
	 */
	public CommResp sendMatchingFailMsg(Order order, StadiumDetail stadiumDetail, Stadium stadium) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(),
				RespCode.SUCCESS_CODE.getRetCode());
		//接收者（用户）的 openid
				String touser = redisClient.get("openId"+order.getId());
				//表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
				String formId = redisClient.get("formId"+order.getId());
				if (CommUtils.isEmpty(touser) || CommUtils.isEmpty(formId)){
					resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "formId或openId不存在",
							RespCode.FAILURE_CODE.getRetCode());
					return resp;
				}
				//接口调用凭证
				String access_token = redisClient.get("mini_access_token");
				//token失效应再次获取一个新的
				if (CommUtils.isEmpty(access_token)){
					getAccessToken();
					access_token = redisClient.get("mini_access_token");
				}
				//所需下发的模板消息的id
				String template_id = MatchingFailMsg_template_id;
				 //拼接推送的模版 
				WxMssVo wxMssVo = new WxMssVo(); 
				wxMssVo.setTouser(touser);//用户openid 
				wxMssVo.setTemplate_id(template_id);//模版id 
				wxMssVo.setForm_id(formId);//formid 
				//page点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
				wxMssVo.setPage("pages/index/index?type=order&id=" + order.getId());
				
				 Map<String, TemplateData> m = new HashMap<>(6);
					//keyword1：订单号，keyword2：订单金额，keyword3：预订地点 ，keyword4：预订时间  ，keyword5：失败原因，keyword6：退款提醒
				TemplateData keyword1 = new TemplateData();
		        TemplateData keyword2 = new TemplateData();
		        TemplateData keyword3 = new TemplateData();
		        TemplateData keyword4 = new TemplateData();
		        TemplateData keyword5 = new TemplateData();
		        TemplateData keyword6 = new TemplateData();
		        
		        keyword1.setValue(order.getOrderNo());
		        m.put("keyword1", keyword1);
		        keyword2.setValue(order.getSiteAmt()+"");
		        m.put("keyword2", keyword2);
		        keyword3.setValue(stadium.getAddress());
		        m.put("keyword3", keyword3);
		        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 ");
				String day = df.format(stadiumDetail.getDay());
		        keyword4.setValue(day + order.getTime());
		        m.put("keyword4", keyword4);
		        keyword5.setValue("您预定的场次不足六人");
		        m.put("keyword5", keyword5);
		        keyword6.setValue("已自动为您提交退款申请，请注意查收！");
		        m.put("keyword6", keyword6);
				
				 wxMssVo.setData(m);
			        String urll = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token;
			        RestTemplate template = new RestTemplate();
			        ResponseEntity<String> responseEntity = template.postForEntity(urll, wxMssVo, String.class);
			        logger.info("微信模板消息请求结果1："+responseEntity);
			        logger.info("微信模板消息请求结果2："+responseEntity.getBody());
			        resp.setObject(responseEntity.getBody());
		return resp;
	}
	/**
	 * 拼场失败给加入者发，改变状态发送服务通知
	 * @param orderId
	 * @param code
	 * @param mobilephone
	 * @param name
	 * @return
	 */
	public CommResp sendMatchingFailMsg2(Order order,OrderDetail orderDetail,StadiumDetail stadiumDetail, Stadium stadium) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(),
				RespCode.SUCCESS_CODE.getRetCode());
		//接收者（用户）的 openid
				String touser = redisClient.get("openId"+orderDetail.getId());
				//表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
				String formId = redisClient.get("formId"+orderDetail.getId());
				if (CommUtils.isEmpty(touser) || CommUtils.isEmpty(formId)){
					resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "formId或openId不存在",
							RespCode.FAILURE_CODE.getRetCode());
					return resp;
				}
				//接口调用凭证
				String access_token = redisClient.get("mini_access_token");
				//token失效应再次获取一个新的
				if (CommUtils.isEmpty(access_token)){
					getAccessToken();
					access_token = redisClient.get("mini_access_token");
				}
				//所需下发的模板消息的id
				String template_id = MatchingFailMsg_template_id;
				 //拼接推送的模版 
				WxMssVo wxMssVo = new WxMssVo(); 
				wxMssVo.setTouser(touser);//用户openid 
				wxMssVo.setTemplate_id(template_id);//模版id 
				wxMssVo.setForm_id(formId);//formid 
				//page点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
				wxMssVo.setPage("pages/index/index?type=order&id=" + order.getId());
				
				 Map<String, TemplateData> m = new HashMap<>(6);
					//keyword1：订单号，keyword2：订单金额，keyword3：预订地点 ，keyword4：预订时间  ，keyword5：失败原因，keyword6：退款提醒
				TemplateData keyword1 = new TemplateData();
		        TemplateData keyword2 = new TemplateData();
		        TemplateData keyword3 = new TemplateData();
		        TemplateData keyword4 = new TemplateData();
		        TemplateData keyword5 = new TemplateData();
		        TemplateData keyword6 = new TemplateData();
		        
		        keyword1.setValue(order.getOrderNo());
		        m.put("keyword1", keyword1);
		        keyword2.setValue(order.getPrepayAmt()+"");
		        m.put("keyword2", keyword2);
		        keyword3.setValue(stadium.getAddress());
		        m.put("keyword3", keyword3);
		        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 ");
				String day = df.format(stadiumDetail.getDay());
		        keyword4.setValue(day + order.getTime());
		        m.put("keyword4", keyword4);
		        keyword5.setValue("您预定的场次不足六人");
		        m.put("keyword5", keyword5);
		        keyword6.setValue("已自动为您提交退款申请，请注意查收！");
		        m.put("keyword6", keyword6);
				
				 wxMssVo.setData(m);
			        String urll = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token;
			        RestTemplate template = new RestTemplate();
			        ResponseEntity<String> responseEntity = template.postForEntity(urll, wxMssVo, String.class);
			        logger.info("微信模板消息请求结果1："+responseEntity);
			        logger.info("微信模板消息请求结果2："+responseEntity.getBody());
			        resp.setObject(responseEntity.getBody());
		return resp;
	}
	/**
	 * 结算退款，退款成功发送服务通知,发起者
	 * @param orderId
	 * @param code
	 * @param mobilephone
	 * @param name
	 * @return
	 */
	public CommResp sendRefundMsg(Order order,StadiumDetail stadiumDetail, Stadium stadium) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(),
				RespCode.SUCCESS_CODE.getRetCode());
		//接收者（用户）的 openid
				String touser = redisClient.get("openId"+order.getId());
				//表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
				String formId = redisClient.get("formId"+order.getId());
				if (CommUtils.isEmpty(touser) || CommUtils.isEmpty(formId)){
					resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "formId或openId不存在",
							RespCode.FAILURE_CODE.getRetCode());
					return resp;
				}
				//接口调用凭证
				String access_token = redisClient.get("mini_access_token");
				//token失效应再次获取一个新的
				if (CommUtils.isEmpty(access_token)){
					getAccessToken();
					access_token = redisClient.get("mini_access_token");
				}
				//所需下发的模板消息的id
				String template_id = RefundMsg_template_id;
				 //拼接推送的模版 
				WxMssVo wxMssVo = new WxMssVo(); 
				wxMssVo.setTouser(touser);//用户openid 
				wxMssVo.setTemplate_id(template_id);//模版id 
				wxMssVo.setForm_id(formId);//formid 
				//page点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
				wxMssVo.setPage("pages/index/index?type=order&id=" + order.getId());
				
				 Map<String, TemplateData> m = new HashMap<>(7);
					//keyword1：项目地点，keyword2：预约时间，keyword3：结算时间 ，keyword4：结算金额  ，keyword5：收款金额，keyword6：退款金额  keyword7：结算说明
				TemplateData keyword1 = new TemplateData();
		        TemplateData keyword2 = new TemplateData();
		        TemplateData keyword3 = new TemplateData();
		        TemplateData keyword4 = new TemplateData();
		        TemplateData keyword5 = new TemplateData();
		        TemplateData keyword6 = new TemplateData();
		        TemplateData keyword7 = new TemplateData();
		        keyword1.setValue(stadium.getAddress());
		        m.put("keyword1", keyword1);
		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
		        String day = df.format(stadiumDetail.getDay());
		        keyword2.setValue(day+order.getTime());
		        m.put("keyword2", keyword2);
		        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
		        String ss = dff.format(order.getSettlementTime());
		        keyword3.setValue(ss);
		        m.put("keyword3", keyword3);
		        keyword4.setValue(order.getOrderAmt()+"");
		        m.put("keyword4", keyword4);
		        keyword5.setValue(order.getSiteAmt()+"");
		        m.put("keyword5", keyword5);
		        keyword6.setValue((order.getSiteAmt()-order.getActualAmt())+"");
		        m.put("keyword6", keyword6);
		        keyword7.setValue("费用已结清");
		        m.put("keyword7", keyword7);
				wxMssVo.setData(m);
			        String urll = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token;
			        RestTemplate template = new RestTemplate();
			        ResponseEntity<String> responseEntity = template.postForEntity(urll, wxMssVo, String.class);
			        logger.info("微信模板消息请求结果1："+responseEntity);
			        logger.info("微信模板消息请求结果2："+responseEntity.getBody());
			        resp.setObject(responseEntity.getBody());
		return resp;
	}
	/**
	 * 结算退款，退款成功发送服务通知,加入者
	 * @param orderId
	 * @param code
	 * @param mobilephone
	 * @param name
	 * @return
	 */
	public CommResp sendRefundMsg2(Order order,OrderDetail orderDetail,StadiumDetail stadiumDetail, Stadium stadium) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(),
				RespCode.SUCCESS_CODE.getRetCode());
		//接收者（用户）的 openid
				String touser = redisClient.get("openId"+orderDetail.getId());
				//表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
				String formId = redisClient.get("formId"+orderDetail.getId());
				if (CommUtils.isEmpty(touser) || CommUtils.isEmpty(formId)){
					resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "formId或openId不存在",
							RespCode.FAILURE_CODE.getRetCode());
					return resp;
				}
				//接口调用凭证
				String access_token = redisClient.get("mini_access_token");
				//token失效应再次获取一个新的
				if (CommUtils.isEmpty(access_token)){
					getAccessToken();
					access_token = redisClient.get("mini_access_token");
				}
				//所需下发的模板消息的id
				String template_id = RefundMsg_template_id;
				 //拼接推送的模版 
				WxMssVo wxMssVo = new WxMssVo(); 
				wxMssVo.setTouser(touser);//用户openid 
				wxMssVo.setTemplate_id(template_id);//模版id 
				wxMssVo.setForm_id(formId);//formid 
				//page点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
				wxMssVo.setPage("pages/index/index?type=order&id=" + order.getId());
				
				 Map<String, TemplateData> m = new HashMap<>(7);
					//keyword1：项目地点，keyword2：预约时间，keyword3：结算时间 ，keyword4：结算金额  ，keyword5：收款金额，keyword6：退款金额  keyword7：结算说明
				TemplateData keyword1 = new TemplateData();
		        TemplateData keyword2 = new TemplateData();
		        TemplateData keyword3 = new TemplateData();
		        TemplateData keyword4 = new TemplateData();
		        TemplateData keyword5 = new TemplateData();
		        TemplateData keyword6 = new TemplateData();
		        TemplateData keyword7 = new TemplateData();
		        keyword1.setValue(stadium.getAddress());
		        m.put("keyword1", keyword1);
		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
		        String day = df.format(stadiumDetail.getDay());
		        keyword2.setValue(day+order.getTime());
		        m.put("keyword2", keyword2);
		        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
		        String ss = dff.format(order.getSettlementTime());
		        keyword3.setValue(ss);
		        m.put("keyword3", keyword3);
		        keyword4.setValue(order.getOrderAmt()+"");
		        m.put("keyword4", keyword4);
		        keyword5.setValue(order.getPrepayAmt()+"");
		        m.put("keyword5", keyword5);
		        keyword6.setValue((order.getPrepayAmt()-order.getActualAmt())+"");
		        m.put("keyword6", keyword6);
		        keyword7.setValue("费用已结清");
		        m.put("keyword7", keyword7);
				wxMssVo.setData(m);
			        String urll = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token;
			        RestTemplate template = new RestTemplate();
			        ResponseEntity<String> responseEntity = template.postForEntity(urll, wxMssVo, String.class);
			        logger.info("微信模板消息请求结果1："+responseEntity);
			        logger.info("微信模板消息请求结果2："+responseEntity.getBody());
			        resp.setObject(responseEntity.getBody());
		return resp;
	}
	/**
	 * 发起者：预定成功通知：预定成功
	 * @param orderId
	 * @param code
	 * @param mobilephone
	 * @param name
	 * @return
	 */
	public CommResp sendGroupSuccendMsg(Order order,StadiumDetail stadiumDetail, Stadium stadium) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(),
				RespCode.SUCCESS_CODE.getRetCode());
		//接收者（用户）的 openid
				String touser = redisClient.get("openId"+order.getId());
				//表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
				String formId = redisClient.get("formId"+order.getId());
				if (CommUtils.isEmpty(touser) || CommUtils.isEmpty(formId)){
					resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "formId或openId不存在",
							RespCode.FAILURE_CODE.getRetCode());
					return resp;
				}
				//接口调用凭证
				String access_token = redisClient.get("mini_access_token");
				//token失效应再次获取一个新的
				if (CommUtils.isEmpty(access_token)){
					getAccessToken();
					access_token = redisClient.get("mini_access_token");
				}
				//所需下发的模板消息的id
				String template_id = GroupMsg_template_id;
				 //拼接推送的模版 
				WxMssVo wxMssVo = new WxMssVo(); 
				wxMssVo.setTouser(touser);//用户openid 
				wxMssVo.setTemplate_id(template_id);//模版id 
				wxMssVo.setForm_id(formId);//formid 
				//page点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
				wxMssVo.setPage("pages/index/index?type=order&id=" + order.getId());
				
				 Map<String, TemplateData> m = new HashMap<>(5);
				//keyword1：预定单号，keyword2：地址，keyword3：时间 ，keyword4：预定类型   ，keyword5：温馨提示
				TemplateData keyword1 = new TemplateData();
		        TemplateData keyword2 = new TemplateData();
		        TemplateData keyword3 = new TemplateData();
		        TemplateData keyword4 = new TemplateData();
		        TemplateData keyword5 = new TemplateData();
		        keyword1.setValue(order.getOrderNo());
		        m.put("keyword1", keyword1);
		        keyword2.setValue(stadium.getAddress());
		        m.put("keyword2", keyword2);
		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
		        String day = df.format(stadiumDetail.getDay());
		        keyword3.setValue(day+order.getTime());
		        m.put("keyword3", keyword3);
		        keyword4.setValue("包场");
		        m.put("keyword4", keyword4);
		        keyword5.setValue("您的场次暗号为:" + order.getTeamCode() + "，请于" + day + order.getTime() + "到场。");
		        m.put("keyword5", keyword5);
				
				 wxMssVo.setData(m);
			        String urll = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+access_token;
			        RestTemplate template = new RestTemplate();
			        ResponseEntity<String> responseEntity = template.postForEntity(urll, wxMssVo, String.class);
			        logger.info("微信模板消息请求结果1："+responseEntity);
			        logger.info("微信模板消息请求结果2："+responseEntity.getBody());
			        resp.setObject(responseEntity.getBody());
		return resp;
	}
	/**
	 * 获取小程序全局唯一后台接口调用凭据（access_token）
	 */
	private void getAccessToken() {
		String url = "https://api.weixin.qq.com/cgi-bin/token";
		String param = "appid=" + appid + "&secret=" + secret + "&grant_type=client_credential";
		String str2 = CallServiceUtil.sendGet(url, param);
		// String转换Map，json转List
		logger.info("获取小程序token结果："+str2);
		@SuppressWarnings("rawtypes")
		Map reqMap = JSON.parseObject(str2, Map.class);
		if (!CommUtils.isObjEmpty(reqMap.get("access_token"))) {
			redisClient.set("mini_access_token", reqMap.get("access_token")+"",Constants.MINI_VALID_TOKEN_TIME);
		}
	}
	
}
