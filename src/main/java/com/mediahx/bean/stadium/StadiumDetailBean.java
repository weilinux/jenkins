package com.mediahx.bean.stadium;

import com.mediahx.model.StadiumDetail;

public class StadiumDetailBean extends StadiumDetail{
	
	/**
	 * 可预订场次
	 */
	private Integer canReserve;
	/**
	 * 可拼场加入场次
	 */
	private Integer canJoin;
	
	public Integer getCanReserve() {
		return canReserve;
	}
	public void setCanReserve(Integer canReserve) {
		this.canReserve = canReserve;
	}
	public Integer getCanJoin() {
		return canJoin;
	}
	public void setCanJoin(Integer canJoin) {
		this.canJoin = canJoin;
	}
	
}
