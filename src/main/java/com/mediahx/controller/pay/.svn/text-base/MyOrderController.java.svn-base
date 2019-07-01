package com.mediahx.controller.pay;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mediahx.bean.RequestVo;
import com.mediahx.bo.pay.CheckDataBo;
import com.mediahx.bo.pay.OrderBo;
import com.mediahx.constant.RespCode;
import com.mediahx.controller.BaseController;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.util.CommUtils;

/**
 * 我的订单管理
 * @author guomf
 * 2019年5月8日
 * project： 梦幻城堡
 */
@Controller
@RequestMapping("Pay/MyOrder")
public class MyOrderController extends BaseController {
	
	@Autowired
	CheckDataBo checkDataBo;
	
	@Autowired
	OrderBo orderBo;
	
	@RequestMapping(value = "/queryMyOrders", produces = "application/json;charset=UTF-8")
	public void queryMyOrders(Model model, HttpServletRequest request, PrintWriter out) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		RequestVo reqVo = getPagerParams(request, model);
	    Map<String, Object> map = reqVo.getParams();
	    if (!CommUtils.isObjEmpty(map.get("userId"))){
	    	//检查用户信息
	    	resp = checkDataBo.checkUser(map, resp);
	    	 if (resp.getRetCode().equals("111")){
		    	 CommResp.printStrJson(resp, out);
				 return;
		     }
	    	//查询订单信息
	    	resp = orderBo.queryMyOrders(map, resp);
	    	 
	    }else {
	    	resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "参数有误请确认", RespCode.FAILURE_CODE.getRetCode());
			CommResp.printStrJson(resp, out);
			return ;
	    }
		CommResp.printStrJson(resp, out);
	}
	
	/**
	 * 订单详情
	 * @param model
	 * @param request
	 * @param out
	 */
	@RequestMapping(value = "/queryMyOrderDetail", produces = "application/json;charset=UTF-8")
	public void queryMyOrderDetail(Model model, HttpServletRequest request, PrintWriter out) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		RequestVo reqVo = getPagerParams(request, model);
	    Map<String, Object> map = reqVo.getParams();
	    if (!CommUtils.isObjEmpty(map.get("orderId"))){
	    	//查询订单信息
	    	resp = orderBo.queryMyOrderDetail(map, resp);
	    } else {
	    	resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "参数有误请确认", RespCode.FAILURE_CODE.getRetCode());
			CommResp.printStrJson(resp, out);
			return ;
	    }
		CommResp.printStrJson(resp, out);
	}
}
