package com.mediahx.bean;

import com.mediahx.model.BaseEntity;
import com.mediahx.util.ListUtils;

/**
 * 分页
 * 
 * @author ZHE
 *
 */
public class PageInfo extends BaseEntity {

	private static final long serialVersionUID = -900159989081498222L;
	//数据库当前页
	private int currentPageNum=0;
	// 当前页数
	private int pageNum = 1;

	// 每页显示数量
	private int numPerPage = 20;

	// 总页数
	private int totalPage;

	// 总数量
	private int totalCount;

	// 将pageNum、numPerPage进行初始化
	public PageInfo(int pageNum, int numPerPage) {
		this.pageNum = pageNum;
		this.numPerPage = numPerPage;
	}

	public PageInfo() {

	}

	public PageInfo(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

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

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		this.totalPage = ListUtils.getTotalPage(this.numPerPage, this.totalCount);
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getCurrentPageNum() {
		return currentPageNum;
	}

	public void setCurrentPageNum(int currentPageNum) {
		this.currentPageNum = currentPageNum;
	}

}