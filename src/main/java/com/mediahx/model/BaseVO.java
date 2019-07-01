package com.mediahx.model;

import java.util.List;
/**
 * 
 * @author ZHE
 *
 */
public class BaseVO extends BaseEntity {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** 分页页码 */
	private int pageNum = 0;

	/** 每页记录数 */
	private int numPerPage = 0;

	/** 排序字段 */
	private String orderField;

	/** 排序方向 asc或desc */
	private String orderDirection;

	/** ID,分号分隔,用于批量处理 */
	private String idStrs;

	/** ID列表 */
	private List<String> idList;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public String getIdStrs() {
		return idStrs;
	}

	public void setIdStrs(String idStrs) {
		this.idStrs = idStrs;
	}

	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList = idList;
	}

}