package com.mediahx.bo.pay;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mediahx.constant.Constants;
import com.mediahx.constant.RespCode;
import com.mediahx.dao.mysql.OrderDetailMapper;
import com.mediahx.dao.mysql.OrderMapper;
import com.mediahx.dao.mysql.StadiumDetailMapper;
import com.mediahx.dao.mysql.StadiumMapper;
import com.mediahx.dao.mysql.UserInfoMapper;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.model.Order;
import com.mediahx.model.OrderDetail;
import com.mediahx.model.Stadium;
import com.mediahx.model.StadiumDetail;
import com.mediahx.model.UserInfo;
import com.mediahx.util.CommUtils;
import com.mediahx.util.DateUtils;
import com.mediahx.util.LogicUtils;

/**
 * 检查价格和赠豆数量是否发生改变,店铺是否正常
 * 
 * @author guomf 2018年11月2日 project： 梦幻城堡
 */
@Component
public class CheckDataBo {

	@Autowired
	StadiumMapper stadiumMapper;

	@Autowired
	StadiumDetailMapper stadiumDetailMapper;

	@Autowired
	UserInfoMapper userInfoMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	OrderDetailMapper orderDetailMapper;

	@Autowired
	LogicUtils logicUtils;

	/**
	 * 检查店铺是否关闭
	 * 
	 * @param map
	 * @param resp
	 * @return
	 */
	private CommResp checkShopInfoData(Map<String, Object> map, CommResp resp) {

		Stadium stadium = stadiumMapper.queryOpenStadium(map.get("stadiumId") + "");
		if (CommUtils.isObjEmpty(stadium)) {
			resp = SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(), "该球场服务可能已关闭，请联系店铺工作人员",
					RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
			return resp;
		}
		map.put("stadium", stadium);
		return resp;
	}

	/**
	 * 检查用户是否存在
	 * 
	 * @param map
	 * @param resp
	 * @return
	 */
	public CommResp checkUser(Map<String, Object> map, CommResp resp) {

		UserInfo userInfo = userInfoMapper.selectById(map.get("userId") + "");
		if (CommUtils.isObjEmpty(userInfo)) {
			resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "用户已不存在",
					RespCode.FAILURE_CODE.getRetCode());
		}
		resp.setObject(userInfo);
		return resp;

	}

	/**
	 * 检查球场是否改变
	 * 
	 * @param map
	 * @param resp
	 * @return
	 */
	public CommResp checkBallData(Map<String, Object> map, CommResp resp) {

		checkShopInfoData(map, resp);

		Map<String, Object> map2 = new HashMap<String, Object>();
		// 查询该场次的价格信息
		StadiumDetail stadiumDetail = stadiumDetailMapper.selectStadiumDetailOn(map);
		if (!CommUtils.isObjEmpty(stadiumDetail)) {
			//判断该场次是否售罄
			
			int s = stadiumDetail.getSoldQuantity() == null ? 0:stadiumDetail.getSoldQuantity();//已售数量
			int ss = stadiumDetail.getQuantity() == null ? 0:stadiumDetail.getQuantity();//库存数量
			if (ss<=s){
				map2.put("stadiumDetail", stadiumDetail);
				resp = SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(), "该场次已售罄",
						RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
				resp.setExt(map2);
				return resp;
			}

			// 应支付金额
			double p = stadiumDetail.getPrice();
			// 判断球场的单价是否改变
			if (p != Double.parseDouble(map.get("price").toString())) {
				map2.put("stadiumDetail", stadiumDetail);
				resp = SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(), "球场价格发生了改变",
						RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
				resp.setExt(map2);
				return resp;
			}
			// 判断预约的时间段是否一致
			String t = stadiumDetail.getTime();
			if (!t.equals(map.get("time"))) {
				resp = SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(), "该场次时间设置发生了改变，请返回重试",
						RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
				return resp;
			}
			map.put("stadiumDetail", stadiumDetail);
		} else {
			resp = SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(), "该场次已下架，请联系客服",
					RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
			return resp;
		}

		return resp;
	}

	/**
	 * 判断加入的场次是否已满
	 * 
	 * @param map
	 * @param resp
	 * @return
	 * @throws ParseException 
	 */
	public CommResp checkBallDetail(Map<String, Object> map, CommResp resp) throws ParseException {
		// 查询订单信息
		Order order = orderMapper.selectByPrimaryKey(map.get("orderId") + "");
		if (!CommUtils.isObjEmpty(order)) {
			//对比当下时间和预约场次时间
			map.put("stadiumDetailId", order.getStadiumDetailId());
			StadiumDetail stadiumDetail = stadiumDetailMapper.selectStadiumDetailOn(map);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
	        String day = df.format(stadiumDetail.getDay());
			day = day+order.getTime().substring(6);
			 DateFormat bf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date end = bf.parse(day);
			long l = DateUtils.dateDiff(new Date(),end);
			if (l<= 0){
				resp = SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(), "该场次拼活动已经结束,无法加入！",
						RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
				return resp;
			}
			if (Constants.ORDER.STATUS_FAILED.equals(order.getStatus())) {
				resp = SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(), "该场次拼场已失败，请选择别的战队吧",
						RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
				return resp;
			}
			// 检查是否已经加入,status为空时说明支付未成功，并未真实加入战队，将此条数据删除
			OrderDetail orderDetail = orderDetailMapper.queryOrderDetail(map);

			if (!CommUtils.isObjEmpty(orderDetail) && !CommUtils.isEmpty(orderDetail.getStatus())) {
				resp = SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(), "您已加入该场次，不能重复加入",
						RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
				return resp;
			}
			if (!CommUtils.isObjEmpty(orderDetail) && CommUtils.isEmpty(orderDetail.getStatus())) {
				orderDetailMapper.deleteByPrimaryKey(orderDetail.getId());
			}

			// 判断应预付金额是否相同
			double d = Double.valueOf(map.get("price") + "").doubleValue();
			if (order.getPrepayAmt() != d) {
				resp = SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(), "预支付价格有异常，无法加入",
						RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
				return resp;
			}
			// 判断球员数是否已满
			if (!CommUtils.isObjEmpty(order.getLockNum())) {
				// 通过锁位连接邀请的好友在这里做一个标识
				if (CommUtils.isObjEmpty(map.get("lockShare"))) {
					//锁单加入加入人员数
					int ln = order.getLockNum()-order.getLockSize();
					if ((order.getMemberSize() - order.getLockNum()) <= (order.getActualSize()-ln)) {
						resp = SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(), "该场次球员已加满",
								RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
						return resp;
					}
				}
			}
			if (order.getMemberSize() <= order.getActualSize()) {
				resp = SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(), "该场次球员已加满!",
						RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
				return resp;
			}

			// 加入包场的 时候
			if (d <= 0) {
				int as = order.getActualSize() + 1;
				order.setActualSize(as);
				// 通过锁位连接邀请的好友减少一个锁位数量
				if (!CommUtils.isObjEmpty(map.get("lockShare"))) {
					if (!CommUtils.isObjEmpty(order.getLockSize()) && order.getLockSize() > 0) {
						order.setLockSize(order.getLockSize() - 1);
					}
				}
				// 组队人数满6人时，订单改为成功，生成暗号信息
				// if (as == 6) {
				// order.setStatus(Constants.ORDER.STATUS_SUCCEED);// 在此修改
				// // 组队成功生成暗号
				// map.put("stadiumDetailId", order.getStadiumDetailId());
				// StadiumDetail stadiumDetail =
				// stadiumDetailMapper.selectStadiumDetailOn(map);
				// if (CommUtils.isObjEmpty(stadiumDetail)) {
				// resp =
				// SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(),
				// "该场次信息已下架，请重新选择",
				// RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
				// return resp;
				// }
				// String num = logicUtils.secretSignal(stadiumDetail.getDay(),
				// order.getStadiumId());
				// order.setTeamCode(num);
				// order.setUpdateTime(new Date());
				//
				// }
				// 更新订单信息
				orderMapper.updateByPrimaryKeySelective(order);
				map.put("stadiumDetailId", order.getStadiumDetailId());

				map.put("orderStatus", order.getStatus());
			}
		} else {
			resp = SetCommRespMsg.defaultSet(RespCode.REGION_LIMIT_ERR_CODE.getRtn(), "未找到该场次信息!",
					RespCode.REGION_LIMIT_ERR_CODE.getRetCode());
			return resp;
		}
		return resp;

	}
}
