package com.mediahx.dao.mysql;

import java.util.List;

import com.mediahx.model.Areas;

public interface AreasMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Areas record);

    int insertSelective(Areas record);

    Areas selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Areas record);

    int updateByPrimaryKey(Areas record);
    
    List<Areas> findAll();
    
    List<Areas> findByCityId(String cityId);
    
    Areas findByAreaId(String areaId);
}