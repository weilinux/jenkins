package com.mediahx.bean;

import java.util.HashMap;
import java.util.Map;

import com.mediahx.model.BaseVO;

/**
 * 请求对象
 * 
 * @author ZHE
 *
 */
public class RequestVo extends BaseVO {
	private static final long serialVersionUID = 1L;

	private PageInfo pager;

	private Map<String, Object> params;

	public RequestVo() {

	}

	public RequestVo(PageInfo pageInfo, Map<String, Object> params) {
		this.pager = pageInfo;
		this.params = params;
	}

	public PageInfo getPager() {
		return pager;
	}

	public void setPager(PageInfo pager) {
		this.pager = pager;
	}

	public Map<String, Object> getParams() {
		if (null == params) {
			params = new HashMap<String, Object>();
		}
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
