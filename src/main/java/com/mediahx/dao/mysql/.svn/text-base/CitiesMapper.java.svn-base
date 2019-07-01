package com.mediahx.dao.mysql;

import java.util.List;

import com.mediahx.model.Cities;

public interface CitiesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cities record);

    int insertSelective(Cities record);

    Cities selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cities record);

    int updateByPrimaryKey(Cities record);
    
    List<Cities> findAll();
    
    Cities findByCityId(String cityId);
    
    List<Cities> findByProvinceId(String provinceId);
}