package com.mediahx.dao.mysql;

import java.util.List;

import com.mediahx.dao.IBaseMapper;
import com.mediahx.model.Advert;

public interface AdvertMapper extends IBaseMapper<Advert>{
	
    int deleteByPrimaryKey(String id);

    int insert(Advert record);

    int insertSelective(Advert record);

    Advert selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Advert record);

    int updateByPrimaryKeyWithBLOBs(Advert record);

    int updateByPrimaryKey(Advert record);

    /**
     * 获取到所有生效的广告
     * @return
     */
	List<Advert> selectAllAdvert();
}