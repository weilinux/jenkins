package com.mediahx.alipay;


/**
 * 交易创建
 * 
 * @author ZHE
 *
 */
public class TradeCreate {
	private String out_trade_no; // 订单支付时传入的商户订单号,不能和 trade_no同时为空。
	private String seller_id;
	private String total_amount;
	private String discountable_amount;
	private String subject;
	private String body;
	private String buyer_id;
	private String goods_detail;
	private String operator_id;
	private String store_id;
	private String terminal_id;
	private String extend_params;
	private String timeout_express;
	private String settle_info;
	private String business_params;
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getDiscountable_amount() {
		return discountable_amount;
	}
	public void setDiscountable_amount(String discountable_amount) {
		this.discountable_amount = discountable_amount;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getBuyer_id() {
		return buyer_id;
	}
	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}
	public String getGoods_detail() {
		return goods_detail;
	}
	public void setGoods_detail(String goods_detail) {
		this.goods_detail = goods_detail;
	}
	public String getOperator_id() {
		return operator_id;
	}
	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}
	public String getExtend_params() {
		return extend_params;
	}
	public void setExtend_params(String extend_params) {
		this.extend_params = extend_params;
	}
	public String getTimeout_express() {
		return timeout_express;
	}
	public void setTimeout_express(String timeout_express) {
		this.timeout_express = timeout_express;
	}
	public String getSettle_info() {
		return settle_info;
	}
	public void setSettle_info(String settle_info) {
		this.settle_info = settle_info;
	}
	public String getBusiness_params() {
		return business_params;
	}
	public void setBusiness_params(String business_params) {
		this.business_params = business_params;
	}
	
}
