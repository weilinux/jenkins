package com.mediahx.dao.mysql;

import java.util.List;

import com.mediahx.model.AppMessage;

public interface AppMessageMapper {
    int deleteByPrimaryKey(String id);

    int insert(AppMessage record);

    int insertSelective(AppMessage record);

    AppMessage selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AppMessage record);

    int updateByPrimaryKey(AppMessage record);
    
    /**
     * 查询最近的5条数据
     * @return
     */
    List<AppMessage> queryMsgs();
}