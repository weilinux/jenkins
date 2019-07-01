package com.mediahx.dao.mysql;

import java.util.List;
import java.util.Map;

import com.mediahx.bean.stadium.StadiumDetailBean;
import com.mediahx.model.StadiumDetail;

public interface StadiumDetailMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(StadiumDetail record);

    int insertSelective(StadiumDetail record);

    StadiumDetail selectByPrimaryKey(String id);
    
    /**
     * 查询上架的场次信息
     * @param map
     * @return
     */
    StadiumDetail selectStadiumDetailOn(Map<String, Object> map);

    int updateByPrimaryKeySelective(StadiumDetail record);

    int updateByPrimaryKey(StadiumDetail record);
    
    /**
     * 查询场地费
     * @param hmap
     * @return
     */
	Map<String, Object> findStadiumPice(Map<String, Object> hmap);

	/**
	 * 根据球场id和当天时间获取 时间段信息
	 * @param map
	 * @return
	 */
	List<StadiumDetailBean> selectByIdAndDay(Map<String, Object> map);

	/**
	 * 根据球场场次id查询该时段的可加入球场
	 * @param stadiumDelId
	 * @return
	 */
	List<Map<String, Object>> selectInfoById(String stadiumDelId);

	/**
	 * 获取组队打球列表 分页
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBallTeamList(Map<String, Object> map);
}