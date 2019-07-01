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
import com.mediahx.dao.mysql.UserInfoMapper;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.util.CommUtils;

/**
 * 生成订单
 * @author guomf
 * 2019年4月30日
 * project： 梦幻城堡
 */
@Controller
@RequestMapping("Pay/Order")
public class OrderController extends BaseController {
	@Autowired
	UserInfoMapper userInfoMapper;
	
	@Autowired
	CheckDataBo checkDataBo;
	
	@Autowired
	OrderBo orderBo;
	
	/**
	 * 发起订单
	 * @param model
	 * @param request
	 * @param out
	 */
	@RequestMapping(value = "/createOrder", produces = "application/json;charset=UTF-8")
	public void createOrder(Model model, HttpServletRequest request, PrintWriter out) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		RequestVo reqVo = getPagerParams(request, model);
	    Map<String, Object> map = reqVo.getParams();
	    if (!CommUtils.isObjEmpty(map.get("stadiumId")) && !CommUtils.isObjEmpty(map.get("userId")) && !CommUtils.isObjEmpty(map.get("stadiumDetailId"))
	    		&& !CommUtils.isObjEmpty(map.get("price"))){
	    	
	    	//检查用户信息
	    	resp = checkDataBo.checkUser(map, resp);
	    	 if (resp.getRetCode().equals("111")){
		    	 CommResp.printStrJson(resp, out);
				 return;
		     }
	    	
	    	 //检查价格是否发生改变
		     resp = checkDataBo.checkBallData(map, resp);
		     if (resp.getRetCode().equals("003")){
		    	 CommResp.printStrJson(resp, out);
				 return;
		     }
		     
		     //生成发起订单
		     resp = orderBo.generateOrder(map,resp);
	    	
	    } else {
	    	resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "参数有误请确认", RespCode.FAILURE_CODE.getRetCode());
			CommResp.printStrJson(resp, out);
			return ;
	    }
		CommResp.printStrJson(resp, out);

	}
	
	/**
	 * 加入 拼场订单
	 * @param model
	 * @param request
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value = "/createOrderDetail", produces = "application/json;charset=UTF-8")
	public void createOrderDetail(Model model, HttpServletRequest request, PrintWriter out) throws Exception {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		RequestVo reqVo = getPagerParams(request, model);
	    Map<String, Object> map = reqVo.getParams();
	    if (!CommUtils.isObjEmpty(map.get("orderId")) && !CommUtils.isObjEmpty(map.get("userId"))
	    		&& !CommUtils.isObjEmpty(map.get("price"))){
	    	
	    	resp = checkDataBo.checkUser(map, resp);
	    	 if (resp.getRetCode().equals("111")){
		    	 CommResp.printStrJson(resp, out);
				 return;
		     }
	    	 //检查加入的场次是否已满，价格是否发生改变
		     resp = checkDataBo.checkBallDetail(map, resp);
		     if (resp.getRetCode().equals("003")){
		    	 CommResp.printStrJson(resp, out);
				 return;
		     }
		     //生成加入订单
		     resp = orderBo.generateOrderDetail(map,resp);
	    } else {
	    	resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "参数有误请确认", RespCode.FAILURE_CODE.getRetCode());
			CommResp.printStrJson(resp, out);
			return ;
	    }
		CommResp.printStrJson(resp, out);
	}
	/**
	 * 支付完成后确认订单
	 * @param model
	 * @param request
	 * @param out
	 */
	@RequestMapping(value = "/confirmOrder", produces = "application/json;charset=UTF-8")
	public void confirmOrder(Model model, HttpServletRequest request, PrintWriter out) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		RequestVo reqVo = getPagerParams(request, model);
	    Map<String, Object> map = reqVo.getParams();
	    if (!CommUtils.isObjEmpty(map.get("orderId"))){
	    	 resp = orderBo.confirmOrder(map,resp);
	    }
	    
	    CommResp.printStrJson(resp, out);
	}
}
