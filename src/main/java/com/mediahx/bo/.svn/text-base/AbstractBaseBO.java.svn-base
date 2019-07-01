package com.mediahx.bo;

import java.util.List;

import com.mediahx.dao.IBaseMapper;
import com.mediahx.model.BaseEntity;
import com.mediahx.util.CommUtils;
import com.mediahx.util.IdGenerateUtils;
import com.mediahx.util.ListUtils;

/**
 * 通用数库操作BO
 * @author ZHE
 *
 * @param <T>
 */
public abstract class AbstractBaseBO<T extends BaseEntity> {

  /** 批量处理分批最大记录数 */
  public static final int BATCH_MAX_COUNT = 200;

  /** ID生成工具类 */
  protected IdGenerateUtils idGenerateUtils = new IdGenerateUtils();

  /**
   * 获取Mapper.
   * <p>
   * 
   * @return
   */
  protected abstract IBaseMapper<T> getMapper();

  /**
   * 创建记录
   * @param entity
   */
  public void create(T entity) {
    if (CommUtils.isEmpty(entity.getId())) {
        entity.setId(IdGenerateUtils.getUUID());
    }
    getMapper().create(entity);
  }
  
  /**
   * 创建记录 
   * 与create方法相同
   * @param entity
   */
  public void insert(T entity) {
    if (CommUtils.isEmpty(entity.getId())) {
        entity.setId(IdGenerateUtils.getUUID());
    }
    getMapper().insert(entity);
  }

  /**
   * 创建记录并返回ID
   * @param entity
   * @return
   */
  public String createReturnId(T entity) {
    if (CommUtils.isEmpty(entity.getId())) {
    	 entity.setId(IdGenerateUtils.getUUID());
    }
    getMapper().createReturnId(entity);
    
    return entity.getId();
  }

  /**
   * 批量插入记录
   * @param entitys
   */
  public void batchCreate(List<T> entitys) {
    if (CommUtils.isObjEmpty(entitys)) {
      return;
    }
    // for (T entity : entitys) {
    // create(entity);
    // }
    List<List<T>> splitList = ListUtils.splitList(entitys, BATCH_MAX_COUNT);
    for (List<T> list : splitList) {
      for (T entity : list) {
        if (CommUtils.isEmpty(entity.getId())) {
        	entity.setId(IdGenerateUtils.getUUID());
        }
      }
      getMapper().batchCreate(list);
    }
  }

  /**
   * 更新记录
   * @param entity
   * @return
   */
  public int update(T entity) {
    return getMapper().update(entity);
  }

  /**
   * 批量更新记录
   * @param entitys
   * @return
   */
  public int batchUpdate(List<T> entitys) {
    int result = 0;
    if (CommUtils.isObjEmpty(entitys)) {
      return result;
    }
    List<List<T>> splitList = ListUtils.splitList(entitys, BATCH_MAX_COUNT);
    for (List<T> list : splitList) {
      result = result + getMapper().batchUpdate(list);
    }
    return result;
  }

  /**
   * 保存记录，如果存在则更新
   * @param entity
   * @return
   */
  public String save(T entity) {
    if (entity.getId() == null || "".equals(entity.getId())) {
      return createReturnId(entity);
    }
    T t = queryById(entity.getId());
    if (t == null) {
      return createReturnId(entity);
    } else {
      update(entity);
      return entity.getId();
    }
  }

 /**
  * 批量保存或更新并返回影响条数
  * @param entitys
  * @return
  */
  public int batchSave(List<T> entitys) {
    for (int i = 0, size = entitys.size(); i < size; i++) {
      save(entitys.get(i));
    }
    return entitys.size();
  }

  /**
   * 删除记录数
   * @param id
   * @return
   */
  public int deleteById(String id) {
    return getMapper().deleteById(id);
  }

  /**
   * 按Id查询
   * @param id
   * @return
   */
  public T queryById(String id) {
    return getMapper().queryById(id);
  }
}
