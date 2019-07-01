package com.mediahx.alipay;

import java.util.List;

/**
 * 统一收单交易退款 响应
 * 
 * @author ZHE
 *
 */
@SuppressWarnings("serial")
public class TradeRefundResp extends PublicRespParam {
	
	private String trade_no;// String 必填 64 2013112011001004330000121536 支付宝交易号
	private String out_trade_no;// String 必填 64 商户订单号 6823789339978248
	private String buyer_logon_id;// String 必填 100 用户的登录id 159****5620
	private String fund_change;// String 必填 1 本次退款是否发生了资金变化 Y
	private String refund_fee;// Price 必填 11 退款总金额 88.88
	private String gmt_refund_pay;// Date 必填 32 退款支付时间 2014-11-27 15:45:57
	private List<TradeFundBill> refund_detail_item_list;// TradeFundBill 选填
														// 退款使用的资金渠道
	private String store_name;// String 选填 512 交易在支付时候的门店名称 望湘园联洋店
	private String buyer_user_id;// String 必填 28 买家在支付宝的用户id 2088101117955611

	public String getTrade_no() {
		return trade_no;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public String getBuyer_logon_id() {
		return buyer_logon_id;
	}

	public String getFund_change() {
		return fund_change;
	}

	public String getRefund_fee() {
		return refund_fee;
	}

	public String getGmt_refund_pay() {
		return gmt_refund_pay;
	}

	public List<TradeFundBill> getRefund_detail_item_list() {
		return refund_detail_item_list;
	}

	public String getStore_name() {
		return store_name;
	}

	public String getBuyer_user_id() {
		return buyer_user_id;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public void setBuyer_logon_id(String buyer_logon_id) {
		this.buyer_logon_id = buyer_logon_id;
	}

	public void setFund_change(String fund_change) {
		this.fund_change = fund_change;
	}

	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}

	public void setGmt_refund_pay(String gmt_refund_pay) {
		this.gmt_refund_pay = gmt_refund_pay;
	}

	public void setRefund_detail_item_list(List<TradeFundBill> refund_detail_item_list) {
		this.refund_detail_item_list = refund_detail_item_list;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public void setBuyer_user_id(String buyer_user_id) {
		this.buyer_user_id = buyer_user_id;
	}

}
