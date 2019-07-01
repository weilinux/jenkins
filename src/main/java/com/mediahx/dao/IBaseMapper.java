package com.mediahx.dao;

import java.util.List;

import com.mediahx.model.BaseEntity;
/**
 * 
 * @author ZHE
 *
 * mybatis 映射基类
 * @param <T>
 */
public interface IBaseMapper<T extends BaseEntity> {
	/**
	 * 插入记录
	 * @param entity
	 */
	int create(T entity);
	int insert(T entity);

	/**
	 * 插入记录 并返回ID
	 * <p>
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象ID
	 */
	long createReturnId(T entity);

	/**
	 * 批量插入记录
	 * <p>
	 * 
	 * @param entitys
	 *            实体对象
	 */
	void batchCreate(List<T> entitys);

	/**
	 * 更新记录
	 * <p>
	 * 
	 * @param entity
	 *            实体对象
	 * @return 更新记录数
	 */
	int update(T entity);

	/**
	 * 批量更新记录
	 * <p>
	 * 
	 * @return 更新记录数
	 * @param entitys
	 *            实体对象
	 */
	int batchUpdate(List<T> entitys);

	/**
	 * 保存记录,如果存在记录,则更新;否则插入
	 * <p>
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象ID
	 */
	long save(T entity);

	/**
	 * 批量保存记录,如果存在记录,则更新;否则插入
	 * <p>
	 * 
	 * @param entitys
	 *            实体对象
	 * @return 更新和插入记录数
	 */
	int batchSave(List<T> entitys);

	/**
	 * 按ID删除记录
	 * <p>
	 * 
	 * @param id
	 *            对象ID
	 * @return 删除记录数
	 */
	int deleteById(String id);

	/**
	 * 按ID查询单条记录
	 * <p>
	 * 
	 * @param id
	 *            实体对象ID
	 * @return 实体对象
	 */
	T queryById(String id);

}