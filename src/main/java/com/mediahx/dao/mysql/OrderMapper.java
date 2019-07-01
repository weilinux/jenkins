package com.mediahx.dao.mysql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.mediahx.bean.OrderBean;
import com.mediahx.bean.OrderDetailBean;
import com.mediahx.bean.RefundBean;
import com.mediahx.model.Order;
import com.mediahx.model.UserInfo;

public interface OrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String id);
    
    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    /**
     * 查询球场 已成功订单用户图片
     * @param id
     * @return
     */
	List<String> findSucceedOrder(String id);

	/**
	 * 组队信息详情
	 * @param orderId
	 * @return
	 */
	Map<String, Object> selectBallTeam(String orderId);
	
	/**
	 * 查询我的订单
	 */
	List<OrderBean> queryMyOrders(Map<String ,Object> map);
	
	int queryMyOrdersCount(Map<String ,Object> map);
	
	OrderDetailBean queryMyOrderDetail(Map<String ,Object> map);
	List <UserInfo> queryPhotos(Map<String ,Object> map);
	List <UserInfo> queryPhotoList(Map<String ,Object> map);
	/**
	 * 根据场次id，查询某场次的在拼单中的订单id 用作退款使用
	 * @param stadiumDetailId
	 * @return
	 */
	List<RefundBean> queryMachingOrderIds(Map<String ,Object> map);
	
	int updateOrdersStatus(@Param("reqParam") Map<String, Object> reqParam);
}