package com.mediahx.dao.mysql;

import java.util.List;
import java.util.Map;

import com.mediahx.model.Stadium;

public interface StadiumMapper {
    int deleteByPrimaryKey(String id);

    int insert(Stadium record);

    int insertSelective(Stadium record);

    Stadium selectByPrimaryKey(String id);
    
    /**
     * 查询上架的球场
     * @param id
     * @return
     */
    Stadium queryOpenStadium(String id);

    int updateByPrimaryKeySelective(Stadium record);

    int updateByPrimaryKey(Stadium record);

    /**
     * 查询所有的球场
     * @return
     */
	List<Stadium> selectAllStadium(Map<String,Object> map);
}