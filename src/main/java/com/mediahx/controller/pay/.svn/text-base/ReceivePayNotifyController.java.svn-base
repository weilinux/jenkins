package com.mediahx.controller.pay;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mediahx.bean.RefundBean;
import com.mediahx.bean.RequestVo;
import com.mediahx.bo.pay.OrderBo;
import com.mediahx.bo.pay.RefundBo;
import com.mediahx.constant.Constants;
import com.mediahx.constant.RespCode;
import com.mediahx.controller.BaseController;
import com.mediahx.dao.mysql.AppMessageMapper;
import com.mediahx.dao.mysql.OrderDetailMapper;
import com.mediahx.dao.mysql.OrderMapper;
import com.mediahx.dao.mysql.StadiumDetailMapper;
import com.mediahx.dao.mysql.StadiumMapper;
import com.mediahx.dao.mysql.UserInfoMapper;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.model.AppMessage;
import com.mediahx.model.Order;
import com.mediahx.model.OrderDetail;
import com.mediahx.model.Stadium;
import com.mediahx.model.StadiumDetail;
import com.mediahx.util.Arith;
import com.mediahx.util.CommUtils;
import com.mediahx.util.LogicUtils;
import com.mediahx.util.PropertiesUtil;
import com.mediahx.util.RedisClient;
import com.mediahx.util.WxMsg;
import com.mediahx.weixin.GenXmlFile;

/**
 * 微信和支付的服务器之间的回调（前端不调用这两个接口）
 * 
 * @author guomf 2018年8月31日
 */
@Controller
@RequestMapping("/notifypay")
public class ReceivePayNotifyController extends BaseController {

	private static String weixin_notifyvalid;

	@Autowired
	RedisClient redisClient;

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	UserInfoMapper appUserInfoMapper;

	@Autowired
	OrderBo orderBo;

	@Autowired
	StadiumDetailMapper stadiumDetailMapper;

	@Autowired
	StadiumMapper stadiumMapper;

	@Autowired
	OrderDetailMapper orderDetailMapper;

	@Autowired
	LogicUtils logicUtils;

	@Autowired
	RefundBo refundBo;

	@Autowired
	AppMessageMapper appMessageMapper;
	
	@Autowired
	WxMsg wxMsg;

	private static final String ORDER_PERSIONS_OUT = "预约失败，该场次预约人员已达上限、系统已自动退款！";

	private static final String WRONG_TIME_REFUND = "预定失败、场次时间发生改变、系统已自动退款！";
	
	private static final String WRONG_PRICE_REFUND = "预定失败、场次价格发生改变、系统已自动退款！";
	
	private static final String NO_STADIUM_REFUND = "预定失败，该场次已下架，系统已自动退款！";
	
	private static final String NOT_HAVE_STADIUM_REFUND = "预定失败、该场次已售罄、系统已自动退款！";
	
	private static final String FAILED_ORDER_REFUND = "预定失败、该次拼场已经失败、系统已自动退款！";
	

	static Properties pro = PropertiesUtil.getProperties("properties/miniprogram.properties");

	static {
		weixin_notifyvalid = pro.getProperty("weixin.notifyvalid");
	}

	/**
	 * 小程序支付回调
	 * 
	 * @param model
	 * @param request
	 * @param out
	 * @param httpServletResponse
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	@RequestMapping(value = "/miniProgramNotify", produces = "application/json")
	public void miniProgramNotify(Model model, HttpServletRequest request, PrintWriter out,
			HttpServletResponse httpServletResponse) throws Exception {
		logger.info("========================开始接收小程序回调数据========================");
		if (!CommUtils.isEmpty(weixin_notifyvalid) && weixin_notifyvalid.equals("1")) {
			httpServletResponse.setContentType("text/xml;charset=utf-8");
			RequestVo reqVo = getPagerParams(request, model);
			Map<String, Object> map = reqVo.getParams();
			System.out.println("map=" + map);
			ServletInputStream is = request.getInputStream();
			if (!CommUtils.isObjEmpty(is)) {

				Document document = GenXmlFile.getXmlDocumentText(is);
				final String out_trade_no = GenXmlFile.getNodeText(document, "/xml/out_trade_no");
				String return_code = GenXmlFile.getNodeText(document, "/xml/return_code");
				String appid = GenXmlFile.getNodeText(document, "/xml/appid");
				String mch_id = GenXmlFile.getNodeText(document, "/xml/mch_id");
				System.out.println("out_trade_no=" + out_trade_no);
				logger.info("**********************out_trade_no=" + out_trade_no + ",return_code=" + return_code
						+ ",appid=" + appid + ",mch_id=" + mch_id);
				if (!CommUtils.isEmpty(out_trade_no) && !CommUtils.isEmpty(return_code) && return_code.equals("SUCCESS")
						&& !CommUtils.isEmpty(appid) && !CommUtils.isEmpty(mch_id)) {
					new Thread(new Runnable() {
						public void run() {
							Order order = null;
							OrderDetail orderDetail = null;
							boolean flag = false;
							order = orderMapper.selectByPrimaryKey(out_trade_no);
							// order存在说明是发起人，不存在说明是加入人
							if (!CommUtils.isObjEmpty(order)) {
								// 发起人的 支付
								flag = true;
							} else {
								// 加入人的支付
								orderDetail = orderDetailMapper.selectByPrimaryKey(out_trade_no);
								order = orderMapper.selectByPrimaryKey(orderDetail.getOrderId());

							}
							//订单已失败的直接退款
							if (Constants.ORDER.STATUS_FAILED.equals(order.getStatus())){
								logger.info("+++++++++++++++++订单已失败,再加入直接退款++++++++++++++++++++++++++");
								faildeOrderRefund(order,orderDetail);
								return;
							}
							// 判断球场 的场次数是否已经订完，场次时间是否一致
							StadiumDetail stadiumDetail = stadiumDetailMapper.selectByPrimaryKey(order.getStadiumDetailId());
							Stadium stadium = stadiumMapper.selectByPrimaryKey(stadiumDetail.getStadiumId());
							//场次下架，退款
							if (Constants.CONSTANTS_N.equals(stadiumDetail.getStatus())){
								logger.info("+++++++++++++++++场次下架，退款++++++++++++++++++++++++++");
								noStadiumRefund(order);
								return;
							}
							// 场次时间段不一致,退款
							if (!order.getTime().equals(stadiumDetail.getTime())) {
								logger.info("+++++++++++++++++场次时间段不一致,退款++++++++++++++++++++++++++");
								wrongTimeRefun(order);
								return;
							}
							// 支付金额和场次费用不一致，退款
							if (!order.getSiteAmt().equals(stadiumDetail.getPrice())){
								logger.info("+++++++++++++++++支付金额和场次费用不一致，退款++++++++++++++++++++++++++");
								wrongPriceRefun(order);
								return;
							}
							// 没有球场时退款
							if (stadiumDetail.getQuantity() <= stadiumDetail.getSoldQuantity()) {
								// 发送微信模板消息通知：场次未预约成功，支付资金会原路返回

								// 第六个人加入没有库存的时候给所有人退钱结算，如果是发起者则给发起者退款
								noTimeRefund(order, flag, orderDetail);

								// 查询该场次所有正在拼场中的订单
								Map<String, Object> parmMap = new HashMap<String, Object>();
								parmMap.put("stadiumDetailId", stadiumDetail.getId());
								List<RefundBean> rbList = orderMapper.queryMachingOrderIds(parmMap);
								if (!CommUtils.isObjEmpty(rbList) && rbList.size() > 0) {
									// 准备退款
									refund(rbList);
								}
								return;
							} else {
								logger.info("+++++++++++++++++++++flag：" + flag + "+++++++++++++++");
								if (flag) {
									// 发起打球的 支付成功时修改订单
									order.setStatus(order.getRemark());
									order.setRemark(null);
									order.setUpdateTime(new Date());
									// 更新场地库存,包场的直接更新
									if (Constants.ORDER.CLASS_GROUP.equals(order.getOrderClass())) {
										int sq = stadiumDetail.getSoldQuantity() == null ? 0
												: stadiumDetail.getSoldQuantity();
										stadiumDetail.setSoldQuantity(sq + 1);
										stadiumDetail.setUpdateTime(new Date());
										stadiumDetailMapper.updateByPrimaryKeySelective(stadiumDetail);
										//包场成功消息
										logger.info("++++++++++++++++++++包场成功   微信模板消息发送 开始+++++++++++++++++++++++");
										wxMsg.sendGroupSuccendMsg(order, stadiumDetail, stadium);
										logger.info("==========包场成功   微信模板消息 发送结束===========");
									}
								} else {
									// 加入拼场时
									int as = order.getActualSize()  + 1;
									// 判断该订单加入的人数是否已满，实际加入数量大于最大允许加入数量则退钱
									if (as > order.getMemberSize()) {
										Map<String, Object> reqParam = new HashMap<String, Object>();
										reqParam.put("orderId", orderDetail.getId());
										reqParam.put("refund_fee", order.getPrepayAmt());
										reqParam.put("total_fee", order.getPrepayAmt());
										reqParam.put("refund_desc", ORDER_PERSIONS_OUT);
										CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(),
												RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
										try {
											logger.info(
													"++++++++++++++++++++实际加入人数大于了最大要求人数 退款,并删除了该加入人订单+++++++++++++++++++++++");

											resp = refundBo.refund(reqParam, resp);
											logger.info("++++++++++++++++++++" + resp.getRtnMsg()
													+ "+++++++++++++++++++++++");
											return;
										} catch (Exception e) {
											logger.info("++++++++++++++++++++退款报错了+++++++++++++++++++++++");
											e.printStackTrace();
										}
									} else {
										order.setActualSize(as);
										//立刻更新一下数据库，防止超员判断
										orderMapper.updateByPrimaryKeySelective(order);
									}
									// 通过锁位连接邀请的好友减少一个锁位数量
									if (!CommUtils.isObjEmpty(orderDetail.getRemark())) {
										if (order.getLockSize() > 0) {
											order.setLockSize(order.getLockSize() - 1);
										}
									}
									// 更新预支付总金额
									order.setOrderAmt(Arith.add(order.getOrderAmt(), order.getPrepayAmt()));
									if (as < 6) {
										orderDetail.setStatus(order.getStatus());
									}
									if (as == 6) {
										// 组队成功生成暗号
										String num = logicUtils.secretSignal(stadiumDetail.getDay(),
												order.getStadiumId());
										order.setStatus(Constants.ORDER.STATUS_SUCCEED);// 在此修改
										orderDetail.setStatus(Constants.ORDER.STATUS_SUCCEED);
										orderDetailMapper.updateByPrimaryKey(orderDetail);
										order.setTeamCode(num);
										order.setUpdateTime(new Date());
										// 修改orderDetail表的所有支付订单的状态
										orderDetailMapper.updateStatusByOrderId(order.getId());
										logger.info("++++++++++++++++++++拼场满6人 给6人发送  微信模板消息发送 开始+++++++++++++++++++++++");
										Map<String, Object> parmMap = new HashMap<String, Object>();
										parmMap.put("orderId", order.getId());
										List<RefundBean> rbList = orderMapper.queryMachingOrderIds(parmMap);
										Map<String, Object> mapIds = new HashMap<String, Object>();
										for (RefundBean rb : rbList) {
											mapIds.put(rb.getOrderId(), rb.getSiteAmt());
											if (!CommUtils.isEmpty(rb.getDetailStatus())) {
												mapIds.put(rb.getOrderDetailId(), rb.getPrepayAmt());
											}
										}
										
										// 遍历map，执行退款操作
										for (Map.Entry<String, Object> entry : mapIds.entrySet()) {
											wxMsg.sendMatchedMsg(order, stadiumDetail, stadium,entry.getKey());
										}
										logger.info("==========拼场满6人  给6人发送                 微信模板消息                        发送结束===========");
										// 更新库存
										int sq = stadiumDetail.getSoldQuantity() == null ? 0
												: stadiumDetail.getSoldQuantity();
										stadiumDetail.setSoldQuantity(sq + 1);
										stadiumDetail.setUpdateTime(new Date());
										stadiumDetailMapper.updateByPrimaryKeySelective(stadiumDetail);
									}
									if (as > 6) {
										// 修改orderDetail表的状态
										orderDetail.setStatus(Constants.ORDER.STATUS_SUCCEED);
										
//										logger.info("++++++++++++++++++++大于6人加入成功   微信模板消息发送 开始+++++++++++++++++++++++");
//										UserInfo userInfo = appUserInfoMapper.selectById(order.getUserId());
//										暂时没有成功，待调
//										wxMsg.sendMatchedMsgMore(order, stadiumDetail, orderDetail,userInfo.getNickName());
//										logger.info("==========大于6人加入成功   微信模板消息 发送结束==========="); 
										
									}
									orderDetail.setOrderTime(new Date());
									orderDetailMapper.updateByPrimaryKey(orderDetail);
								}
								orderMapper.updateByPrimaryKeySelective(order);
							}
							
							// 在消息表中加入一条消息数据
							if (Constants.ORDER.CLASS_GROUP.equals(order.getOrderClass())) {
								// 包场公开（免费状态不为朋友）时加一条
								if (!Constants.ORDER.FREE_TYPE_FRIEND.equals(order.getFreeType())) {
									logger.info("+++++包场+++++在消息表中加入一条消息数据++++++++++++++");
									creatAppMsg(order,stadium,stadiumDetail);
								}
							} else {
								// 拼场时加一条
								logger.info("+++++拼场+++++在消息表中加入一条消息数据++++++++++++++");
								creatAppMsg(order,stadium,stadiumDetail);
							}
						}
					}).start();

					httpServletResponse.getWriter().print(
							"<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
					logger.info("========================结束接收微信回调数据========================");

				}

			}
		} else {
			logger.info("========================接收微信回调数据异常========================");

		}
	}

	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 加入已失败的订单直接退款
	 * @param order
	 */
	private void faildeOrderRefund(Order order,OrderDetail orderDetail){

		Map<String, Object> reqParam = new HashMap<String, Object>();
		reqParam.put("orderId", orderDetail.getId());
		reqParam.put("refund_fee", order.getPrepayAmt());
		reqParam.put("total_fee", order.getPrepayAmt());
		reqParam.put("refund_desc", FAILED_ORDER_REFUND);
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(),
				RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		try {
			resp = refundBo.refund(reqParam, resp);
			logger.info("++++++++++++++++++++" + resp.getRtnMsg() + "+++++++++++++++++++++++");
			// 修改订单状态为已失败
			orderDetail.setStatus(Constants.ORDER.STATUS_FAILED);
			orderDetail.setOrderTime(new Date());
			orderDetailMapper.updateByPrimaryKeySelective(orderDetail);
		} catch (Exception e) {
			logger.info("++++++++++++++++++++加入已失败的订单直接退款,退款报错了+++++++++++++++++++++++");
			e.printStackTrace();
		}
	
	}
	/**
	 * 该场次已下架
	 * @param order
	 */
	private void noStadiumRefund(Order order){
		Map<String, Object> reqParam = new HashMap<String, Object>();
		reqParam.put("orderId", order.getId());
		reqParam.put("refund_fee", order.getSiteAmt());
		reqParam.put("total_fee", order.getSiteAmt());
		reqParam.put("refund_desc", NO_STADIUM_REFUND);
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(),
				RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		try {
			resp = refundBo.refund(reqParam, resp);
			logger.info("++++++++++++++++++++" + resp.getRtnMsg() + "+++++++++++++++++++++++");
			// 修改订单状态为已失败
			order.setStatus(Constants.ORDER.STATUS_FAILED);
			order.setUpdateTime(new Date());
			orderMapper.updateByPrimaryKeySelective(order);
		} catch (Exception e) {
			logger.info("++++++++++++++++++++场次已下架,退款报错了+++++++++++++++++++++++");
			e.printStackTrace();
		}
	}
	/**
	 * 支付金额和场次价格不一致 ，退钱
	 * @param order
	 */
	private void wrongPriceRefun(Order order){
			Map<String, Object> reqParam = new HashMap<String, Object>();
			reqParam.put("orderId", order.getId());
			reqParam.put("refund_fee", order.getSiteAmt());
			reqParam.put("total_fee", order.getSiteAmt());
			reqParam.put("refund_desc", WRONG_PRICE_REFUND);
			CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(),
					RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
			try {
				resp = refundBo.refund(reqParam, resp);
				logger.info("++++++++++++++++++++" + resp.getRtnMsg() + "+++++++++++++++++++++++");
				// 修改订单状态为已失败
				order.setStatus(Constants.ORDER.STATUS_FAILED);
				order.setUpdateTime(new Date());
				orderMapper.updateByPrimaryKeySelective(order);
			} catch (Exception e) {
				logger.info("++++++++++++++++++++支付金额和场次价格不一致,退款报错了+++++++++++++++++++++++");
				e.printStackTrace();
			}
	}
	/**
	 * 场次时间不匹配退款
	 * @param order
	 * @param stadiumDetail
	 */
	private void wrongTimeRefun(Order order){
			Map<String, Object> reqParam = new HashMap<String, Object>();
			reqParam.put("orderId", order.getId());
			reqParam.put("refund_fee", order.getSiteAmt());
			reqParam.put("total_fee", order.getSiteAmt());
			reqParam.put("refund_desc", WRONG_TIME_REFUND);
			CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(),
					RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
			try {
				resp = refundBo.refund(reqParam, resp);
				logger.info("++++++++++++++++++++" + resp.getRtnMsg() + "+++++++++++++++++++++++");
				// 修改订单状态为已失败
				order.setStatus(Constants.ORDER.STATUS_FAILED);
				order.setUpdateTime(new Date());
				orderMapper.updateByPrimaryKeySelective(order);
			} catch (Exception e) {
				logger.info("++++++++++++++++++++场次时间不匹配,退款报错了+++++++++++++++++++++++");
				e.printStackTrace();
			}
	}
	/**
	 * 第六个人加入没有库存的时候给所有人退钱结算，查询本订单的所有参与者
	 */
	private void noTimeRefund(Order order, Boolean flag, OrderDetail orderDetail) {
		// 修改订单为已失败
		if (flag) {
			logger.info("++++++++++++++++++++发起者 没有球场，退款+++++++++++++++++++++++");
			order.setStatus(Constants.ORDER.STATUS_FAILED);
			order.setRemark(null);
			order.setUpdateTime(new Date());
			orderMapper.updateByPrimaryKeySelective(order);
			// 给发起者退款
			Map<String, Object> reqParam = new HashMap<String, Object>();
			reqParam.put("orderId", order.getId());
			reqParam.put("refund_fee", order.getSiteAmt());
			reqParam.put("total_fee", order.getSiteAmt());
			reqParam.put("refund_desc", NOT_HAVE_STADIUM_REFUND);
			CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(),
					RespCode.SUCCESS_CODE.getRetCode());
			try {
				resp = refundBo.refund(reqParam, resp);
				logger.info("++++++++++++++++++++" + resp.getRtnMsg() + "+++++++++++++++++++++++");
			} catch (Exception e) {
				logger.info("++++++++++++++++++++第六个人加入没有库存的时候给所有人退钱结算,退款报错了+++++++++++++++++++++++");
				e.printStackTrace();
			}

		} else {
			logger.info("++++++++++++++++++++加入者 没有球场，退款+++++++++++++++++++++++");
			// 修改orderDetail的状态为拼场中，方便统一退款的时候查询
			orderDetail.setStatus(order.getStatus());
			orderDetail.setOrderTime(new Date());
			orderDetailMapper.updateByPrimaryKey(orderDetail);
			// 第六个人加入没有库存的时候给所有人退钱结算，查询本订单的所有参与者
			Map<String, Object> parmMap = new HashMap<String, Object>();
			parmMap.put("orderId", order.getId());
			List<RefundBean> rbList = orderMapper.queryMachingOrderIds(parmMap);
			if (!CommUtils.isObjEmpty(rbList) && rbList.size() > 0) {
				// 准备退款
				refund(rbList);
			}
		}
	}

	/**
	 * 没有场次了将该场次正在拼单中的订单改为已失败，并退款
	 * 
	 * @param rbList
	 */
	private void refund(List<RefundBean> rbList) {
		// 准备退款
		// 对rbList进行分类,以 id:应退还的金额 的方式存在
		Map<String, Object> mapIds = new HashMap<String, Object>();
		for (RefundBean rb : rbList) {
			mapIds.put(rb.getOrderId(), rb.getSiteAmt());
			if (!CommUtils.isEmpty(rb.getDetailStatus())) {
				mapIds.put(rb.getOrderDetailId(), rb.getPrepayAmt());
			}
		}
		Map<String, Object> reqParam = new HashMap<String, Object>();
		// 遍历map，执行退款操作
		for (Map.Entry<String, Object> entry : mapIds.entrySet()) {
			reqParam.put("orderId", entry.getKey());
			reqParam.put("refund_fee", entry.getValue() + "");
			reqParam.put("total_fee", entry.getValue() + "");
			reqParam.put("refund_desc", NOT_HAVE_STADIUM_REFUND);
			CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(),
					RespCode.SUCCESS_CODE.getRetCode());
			try {
				resp = refundBo.refund(reqParam, resp);
				logger.info("++++++++++++++++++++" + resp.getRtnMsg() + "+++++++++++++++++++++++");
			} catch (Exception e) {
				logger.info("++++++++++++++++++++没有场次了将该场次正在拼单中的订单改为已失败,退款报错了+++++++++++++++++++++++");
				e.printStackTrace();
			}
		}
		// 批量修改订单状态为已失败
		orderMapper.updateOrdersStatus(mapIds);
		orderDetailMapper.updateOrdersDetailStatus(mapIds);

	}

	private void creatAppMsg(Order order,Stadium stadium,StadiumDetail stadiumDetail) {
		AppMessage appMessage = new AppMessage();
		appMessage.setAppUserId(order.getUserId());
		appMessage.setId(CommUtils.getUUID());
		appMessage.setOrderId(order.getId());
		appMessage.setCreateTime(new Date());
		// 拼接title
		Date day = stadiumDetail.getDay();
		SimpleDateFormat df = new SimpleDateFormat("MM-dd");
		String d = df.format(day);
		String messTitle = "";
		if (Constants.ORDER.CLASS_GROUP.equals(order.getOrderClass())) {
			messTitle = d + stadium.getName() + stadiumDetail.getTime() + "有人发起包场";
		} else {
			messTitle = d + stadium.getName() + stadiumDetail.getTime() + "有人发起拼场";
		}
		appMessage.setMessTitle(messTitle);
		logger.info("========================开始插入消息========================" + "getAppUserId:"
				+ appMessage.getAppUserId() + "getId:" + appMessage.getId() + "getOrderId:" + appMessage.getOrderId());
		appMessageMapper.insertSelective(appMessage);
		logger.info("========================消息插入结束========================");

	}
}
