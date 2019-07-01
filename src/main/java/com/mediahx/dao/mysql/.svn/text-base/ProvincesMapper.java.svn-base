package com.mediahx.dao.mysql;

import java.util.List;

import com.mediahx.model.Provinces;

public interface ProvincesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Provinces record);

    int insertSelective(Provinces record);

    Provinces selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Provinces record);

    int updateByPrimaryKey(Provinces record);
    
    List<Provinces> findAll();
   
    Provinces findByProvinceId(String provinceId);
}