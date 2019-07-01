package com.mediahx.dao.mysql;

import com.mediahx.bean.user.UserInfoBean;
import com.mediahx.model.UserInfo;

public interface UserInfoMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    /**
     * 根据用户微信唯一标识查询用户
     * @param unionid
     * @return
     */
	UserInfo selectByUnionid(String weixinId);
	
	UserInfo selectById(String id);

	/**
	 * 根据用户id获取用户详情
	 * @param userId
	 * @return
	 */
	UserInfoBean selectUserInfoById(String userId);
}