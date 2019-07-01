package com.mediahx.weixin;

import com.mediahx.util.CallServiceUtil;

/**
 * 
 * @author ZHE
 *
 */
public class ReFundRequestUtil {
	public static void main(String[] args) {
		String str = CallServiceUtil.sendPost("http://www.hxinside.com:9991/hx_admin/weixin/refund?terminal=ios&refund_fee=1.90&total_fee=25.2&refund_desc=ok&out_trade_no=012017112211095841e1c11bf", null);
		System.out.println(str);
	}
}
