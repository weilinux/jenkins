package com.mediahx.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MongoDB接口
 * @author ZHE
 *
 * @param <T>
 */
public interface IMongoDB <T>{
	/**
	 * 添加对象
	 * 
	 * @param object
	 * @param collectionName
	 */
	public void insert(T object, String collectionName);
	public T create(T object, String collectionName);
	
	/**
	 * 根据ID查询
	 * 
	 * @param odbId
	 * @param entityClass
	 * @param collectionName
	 * @return
	 */
	public T findOneById(Object odbId, Class<T> entityClass, String collectionName);
	
	
	/**
	 * 根据条件查找
	 * @param coditionParams
	 * @param entityClass
	 * @param collectionName
	 * @return
	 */
	public T findOne(Map<String, Object> coditionParams, Class<T> entityClass, String collectionName);

	/**
	 * 查询所有
	 * @param coditionParams
	 * @param entityClass
	 * @param collectionName
	 * @return
	 */
	public List<T> findAll(Map<String, Object> coditionParams,Class<T> entityClass, String collectionName);

	/**
	 * 修改
	 * 
	 * @param params
	 * @param collectionName
	 */
	public void update(Map<String, Object> params, Class<T> entityClass, String collectionName);
	
	/**
	 * 更新
	 * @param params 更新列
	 * @param conditionParams 条件
	 * @param entityClass
	 * @param collectionName
	 */
	public void updateMulti(Map<String, Object> params,Map<String, Object> conditionParams,Class<T> entityClass, String collectionName);
    
	/**
	 * 创建集合
	 * 
	 * @param collectionName
	 */
	public void createCollection(String collectionName);
    
	/**
	 * 根据ID删除
	 * 
	 * @param odbId
	 * @param collectionName
	 */
	public void removeById(Object odbId, Class<T> entityClass, String collectionName);
    
	/**
	 * 根据条件删除
	 * @param coditionParams
	 * @param entityClass
	 * @param collectionName
	 */
	public void remove(Map<String, Object> coditionParams,Class<T> entityClass, String collectionName);

	/**
	 * 
	 * 根据时间范围查询
	 * 
	 * @param sDate
	 *            开始
	 * @param eDate
	 *            结束
	 * @param entityClass
	 * @param collectionName
	 * @param fieldKey
	 * @return
	 */
	public List<T> findListByDate(Date sDate, Date eDate, Class<T> entityClass, String collectionName,String fieldKey);
    
	/**
	 * 分页查询
	 * 
	 * @param coditionParams
	 *            查询条件
	 * @param params
	 *            分页参数
	 * @param entityClass
	 * @param collectionName
	 * @return
	 */
	public List<T> findListByPage(Map<String, Object> coditionParams, Class<T> entityClass, String collectionName);
	
	/**
	 * 查询记录数
	 * @param clazz
	 * @return
	 */
	public long findCount(Map<String, Object> coditionParams,Class<T> clazz);
	
}
