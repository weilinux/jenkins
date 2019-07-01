package com.mediahx.dao.mysql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.mediahx.model.OrderDetail;
import com.mediahx.model.UserInfo;

public interface OrderDetailMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(OrderDetail record);

    int insertSelective(OrderDetail record);

    OrderDetail selectByPrimaryKey(String id);
    /**
     * 根据orderid和userid查询订单
     * @param map
     * @return
     */
    OrderDetail queryOrderDetail(Map<String, Object> map);
    
    int updateByPrimaryKeySelective(OrderDetail record);
    
    int updateStatusByOrderId(String orderId);

    int updateByPrimaryKey(OrderDetail record);

    /**
     * 根据订单id查询所有的组队人的头像
     * @param orderId
     * @return
     */
	List<UserInfo> selectPhotoByOrderId(String orderId);
	
	 int updateOrdersDetailStatus(@Param("reqParam")Map<String ,Object> reqParam);
}