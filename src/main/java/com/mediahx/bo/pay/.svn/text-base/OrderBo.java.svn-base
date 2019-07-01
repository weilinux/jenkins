package com.mediahx.bo.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mediahx.bean.OrderBean;
import com.mediahx.bean.OrderDetailBean;
import com.mediahx.bo.AbstractBaseBO;
import com.mediahx.constant.Constants;
import com.mediahx.controller.pay.MiniProgramController;
import com.mediahx.dao.IBaseMapper;
import com.mediahx.dao.mysql.AppMessageMapper;
import com.mediahx.dao.mysql.OrderDetailMapper;
import com.mediahx.dao.mysql.OrderMapper;
import com.mediahx.dao.mysql.StadiumDetailMapper;
import com.mediahx.dao.mysql.StadiumMapper;
import com.mediahx.message.CommResp;
import com.mediahx.model.Order;
import com.mediahx.model.OrderDetail;
import com.mediahx.model.StadiumDetail;
import com.mediahx.model.UserInfo;
import com.mediahx.util.Arith;
import com.mediahx.util.CommUtils;
import com.mediahx.util.IdGenerateUtils;
import com.mediahx.util.LogicUtils;
import com.mediahx.util.RedisClient;

/**
 * @author guomf 2018年10月31日 project： 梦幻城堡
 */
@Component
public class OrderBo  extends AbstractBaseBO<Order> {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	private static String weixin_orderquery_post;
//	static Properties pro =PropertiesUtil.getProperties("properties/miniprogram.properties");
//	static{
//		weixin_orderquery_post=pro.getProperty("weixin.orderquery.post");
//	}

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private RedisClient redisClient;

	@Autowired
	  OrderDetailMapper orderDetailMapper;
	
	@Autowired
	LogicUtils logicUtils;
	
	@Autowired
	MiniProgramController miniProgramController;
	
	@Autowired
	AppMessageMapper appMessageMapper;
	
	@Autowired
	StadiumDetailMapper stadiumDetailMapper;
	
	@Autowired
	StadiumMapper stadiumMapper;

	/**
	 * 生成发起订单
	 * 
	 * @param map
	 * @return
	 */
	public CommResp generateOrder(Map<String, Object> map, CommResp resp) {
		// 生成发起订单
		Order order = order(map,resp);
		// 微信发送模板消息的必要参数，由小程序调用接口时传过来存储，便于发送模板消息时使用
		if (!CommUtils.isObjEmpty(map.get("formId"))){
			String formId = map.get("formId")+"";
			String openId = map.get("openId")+"";
			redisClient.set("formId"+order.getId(), formId, Constants.MINI_VALID_FORMID_TIME);
			redisClient.set("openId"+ order.getId(), openId, Constants.MINI_VALID_FORMID_TIME);
		}
		
		Map<String, Object> orderMap = new HashMap<String, Object>();

		orderMap.put("order", order);
		resp.setExt(orderMap);
		return resp;
	}
	
	/**
	 * 拼场加入订单
	 * @param map
	 * @param resp
	 * @return
	 */
	public CommResp generateOrderDetail(Map<String, Object> map, CommResp resp) {
		// 生成加入订单
		OrderDetail orderDetail = orderDetail(map,resp);
		
		// 微信发送模板消息的必要参数，由小程序调用接口时传过来存储，便于发送模板消息时使用
		if (!CommUtils.isObjEmpty(map.get("formId"))){
			String formId = map.get("formId")+"";
			String openId = map.get("openId")+"";
			redisClient.set("formId"+orderDetail.getId(), formId, Constants.MINI_VALID_FORMID_TIME);
			redisClient.set("openId"+ orderDetail.getId(), openId, Constants.MINI_VALID_FORMID_TIME);
		}
		
		Map<String, Object> orderMap = new HashMap<String, Object>();

		orderMap.put("orderDetail", orderDetail);
		resp.setExt(orderMap);
		return resp;
	}
	
	public CommResp queryMyOrders(Map<String, Object> map, CommResp resp) {
		List<OrderBean> orderList = orderMapper.queryMyOrders(map);
		if (!CommUtils.isObjEmpty(orderList) && orderList.size() > 0){
			for (OrderBean orderBean : orderList){
				if (CommUtils.isObjEmpty(orderBean.getLockSize())){
					orderBean.setLockSize(0);
				}
				//查询该订单所有参与者信息
				map.put("orderId", orderBean.getId());
				List <UserInfo> photoList = orderMapper.queryPhotoList(map);
				orderBean.setPhotoList(photoList);
			}
		}
		resp.setData(orderList);
		//查询数量
		int c = orderMapper.queryMyOrdersCount(map);
		resp.setTotalCount(c);
		return resp;
	}
	
	public CommResp queryMyOrderDetail(Map<String, Object> map, CommResp resp) {
		//查询订单详情信息
		OrderDetailBean orderDetailBean = orderMapper.queryMyOrderDetail(map);
		
		if (!CommUtils.isObjEmpty(map.get("userId")) && map.get("userId").equals(orderDetailBean.getUserId())){
			orderDetailBean.setFlag(Constants.CONSTANTS_Y);
		} else {
			orderDetailBean.setFlag(Constants.CONSTANTS_N);
		}
		if (CommUtils.isObjEmpty(orderDetailBean.getLockNum())){
			orderDetailBean.setIsLock(Constants.CONSTANTS_N);
		} else {
			orderDetailBean.setIsLock(Constants.CONSTANTS_Y);
		}
		//拼场订单为已失败，已完成时 计算退款金额
			if (Constants.ORDER.STATUS_FAILED.equals(orderDetailBean.getStatus())){
				if (!CommUtils.isObjEmpty(map.get("userId")) && map.get("userId").equals(orderDetailBean.getUserId())){
					orderDetailBean.setRefundAmt(orderDetailBean.getSiteAmt());
				} else {
					orderDetailBean.setRefundAmt(orderDetailBean.getPrepayAmt());
				}
				
			}
			if (Constants.ORDER.STATUS_FINISHED.equals(orderDetailBean.getStatus())){
				if (!CommUtils.isObjEmpty(map.get("userId")) && map.get("userId").equals(orderDetailBean.getUserId())){
					orderDetailBean.setRefundAmt(0D);
				} else {
					orderDetailBean.setRefundAmt(Arith.sub(orderDetailBean.getPrepayAmt(), orderDetailBean.getActualAmt()));
				}
				
			}
		//查询该订单所有参与者信息
		List <UserInfo> photos = orderMapper.queryPhotos(map);
		
		if (!CommUtils.isObjEmpty(orderDetailBean)){
			orderDetailBean.setPhoto(photos);
		}
		resp.setObject(orderDetailBean);
		return resp;
	}
	/**
	 * 生成发起订单
	 * @param map
	 * @param resp
	 * @return
	 */
	@SuppressWarnings("static-access")
	private Order order(Map<String, Object> map, CommResp resp) {
			Order order = new Order();
			// 获取金额
			double d = Double.valueOf(map.get("price")+"").doubleValue();
			order.setOrderAmt(d);
			order.setSiteAmt(d);
			
		// freeType 存在说明是包场，不存在说明是拼场
		if (!CommUtils.isObjEmpty(map.get("freeType"))) {
			order.setFreeType(map.get("freeType")+"");
			order.setOrderClass(Constants.ORDER.CLASS_GROUP);
			//包场分公开和非公开，非公开时设置最多球员数12，公开时按照用户设置设定 
			if (Constants.ORDER.FREE_TYPE_FRIEND.equals(map.get("freeType"))){
				order.setMemberSize(12);
			} else {
				order.setMemberSize(Integer.valueOf(map.get("memberSize")+""));
			}
			order.setActualSize(1);
			order.setRemark(Constants.ORDER.STATUS_SUCCEED);
			//获取暗号
			StadiumDetail stadiumDetail = (StadiumDetail) map.get("stadiumDetail");//在校验场地价格时进行了put操作
			String num = logicUtils.secretSignal(stadiumDetail.getDay(), map.get("stadiumId")+"");
			order.setTeamCode(num);
			order.setPrepayAmt(0D);
			order.setMinimumAmt(0D);
			if (!CommUtils.isObjEmpty(map.get("lockSize")) || !"0".equals(map.get("lockSize"))){
				order.setLockNum(Integer.valueOf(map.get("lockSize")+""));
				order.setLockSize(Integer.valueOf(map.get("lockSize")+""));
			}
			//包场的参与者实付金额直接设置为0
			order.setActualAmt(0D);
		} else {
			order.setOrderClass(Constants.ORDER.CLASS_MATCH);
			//发起人预付费金额除以6，计算出拼场人员最少预付金额
			//资金格式处理,截取小数点后一位
			String m = "";
			m = logicUtils.reservedDecimal(Arith.div(d, Double.valueOf(map.get("memberSize")+"").doubleValue())+"", 1);
			order.setMinimumAmt(Double.valueOf(m).doubleValue());
			//计算出最多预付金额
			m = logicUtils.reservedDecimal(Arith.div(d, 6D)+"", 1);
			order.setPrepayAmt(Double.valueOf(m).doubleValue());
			order.setMemberSize(Integer.valueOf(map.get("memberSize")+""));
			order.setActualSize(1);
			if (!CommUtils.isObjEmpty(map.get("lockSize")) || !"0".equals(map.get("lockSize"))){
				order.setLockNum(Integer.valueOf(map.get("lockSize")+""));
				order.setLockSize(Integer.valueOf(map.get("lockSize")+""));
			}
			order.setRemark(Constants.ORDER.STATUS_MATCHING);
			
		}
		order.setStadiumId(map.get("stadiumId")+"");
		order.setStadiumDetailId(map.get("stadiumDetailId")+"");
		order.setUserId(map.get("userId")+"");
		//订单号:年月日+4位随机数
		String orderNo = null;
		orderNo = logicUtils.createOrderCode();
		order.setOrderNo(orderNo);
		
		order.setId(IdGenerateUtils.getUUID());
		// 用户表ID
		order.setUserId(map.get("userId") + "");
		order.setTime(map.get("time") + "");
		order.setOrderTime(new Date());
		orderMapper.insertSelective(order);
		
		return order;
}
	/**
	 * 拼场加入订单
	 * @param map
	 * @param resp
	 * @return
	 */
	private OrderDetail orderDetail(Map<String, Object> map, CommResp resp){
		OrderDetail orderDetail = new OrderDetail();
		
		orderDetail.setId(IdGenerateUtils.getUUID());
		orderDetail.setOrderId(map.get("orderId")+"");
		orderDetail.setUserId(map.get("userId")+"");
		if (!CommUtils.isObjEmpty(map.get("orderStatus"))){
			orderDetail.setStatus(map.get("orderStatus")+"");
		}
		//通过锁位连接邀请的好友在这里做一个标识
		if (!CommUtils.isObjEmpty(map.get("lockShare"))){
			orderDetail.setRemark(map.get("lockShare")+"");
		}
		orderDetail.setOrderTime(new Date());
		orderDetailMapper.insertSelective(orderDetail);
		return orderDetail;
		
	}
	/**
	 * 确认订单
	 * @param map
	 * @param resp
	 * @return
	 */
	public CommResp confirmOrder(Map<String, Object> map, CommResp resp) {
		Order order = orderMapper.selectByPrimaryKey(map.get("orderId")+"");
		//order存在说明是发起人，不存在说明是加入人
		if (!CommUtils.isObjEmpty(order)){
			//确认是否支付成功
			logger.info("**********************进入支付是否成功确认:"+order.getId());
			String  result = miniProgramController.orderQuery(order.getId());
			if ("SUCCESS".equals(result)){
				//支付成功，判断订单状态是否修改，status为空，说明是发起人，不为空说明是加入人
				if (CommUtils.isEmpty(order.getStatus())){
					if (!CommUtils.isEmpty(order.getRemark())){
							order.setStatus(order.getRemark());
							order.setRemark(null);
							order.setUpdateTime(new Date());
							orderMapper.updateByPrimaryKeySelective(order);
						}
				} else {
					//加入人的支付
//					orderDetail = orderDetailMapper.selectByPrimaryKey(out_trade_no);
//					order = orderMapper.selectByPrimaryKey(orderDetail.getOrderId());
				}
				
			}
		}
		return resp;
		
	}
	@Override
	protected IBaseMapper<Order> getMapper() {
		return null;
	}
	

}
