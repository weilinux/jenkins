package com.mediahx.bean;

/**
 *	退款的out_trade_no
 * @author guomf
 * 2019年5月9日
 * project： 梦幻城堡
 */
public class RefundBean {
	
	private String orderId;
	private String orderDetailId;
	
	private String detailStatus;
	/**
	 * 发起人场地预付金额
	 */
	private Double siteAmt;
	/**
	 * 拼场人员预付金额
	 */
	private Double prepayAmt;
	/**
	 * 拼场实际付款金额(计算后的)
	 */
	private Double actualAmt;
	
	/**
	 * 实际加入人数
	 */
	private Double actualSize;
	
	/**
	 * 订单总金额
	 */
	private Double orderAmt;
	public String getDetailStatus() {
		return detailStatus;
	}
	public void setDetailStatus(String detailStatus) {
		this.detailStatus = detailStatus;
	}
	public Double getSiteAmt() {
		return siteAmt;
	}
	public void setSiteAmt(Double siteAmt) {
		this.siteAmt = siteAmt;
	}
	public Double getPrepayAmt() {
		return prepayAmt;
	}
	public void setPrepayAmt(Double prepayAmt) {
		this.prepayAmt = prepayAmt;
	}
	public Double getActualAmt() {
		return actualAmt;
	}
	public void setActualAmt(Double actualAmt) {
		this.actualAmt = actualAmt;
	}
	public Double getActualSize() {
		return actualSize;
	}
	public void setActualSize(Double actualSize) {
		this.actualSize = actualSize;
	}
	public Double getOrderAmt() {
		return orderAmt;
	}
	public void setOrderAmt(Double orderAmt) {
		this.orderAmt = orderAmt;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

}
