package com.mediahx.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 基类
 * @author ZHE
 *
 */
public class BaseEntity implements Serializable, Cloneable {
	private String id;
	/**
	 * ZHE
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 获取数据库序列名.
	 * @return			序列名
	 */
	/*public String getSequenceName() {
		return null;
	}*/

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}