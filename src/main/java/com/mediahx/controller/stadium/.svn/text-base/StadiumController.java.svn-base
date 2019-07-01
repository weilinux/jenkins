package com.mediahx.controller.stadium;

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mediahx.bean.RequestVo;
import com.mediahx.bo.stadium.StadiumBo;
import com.mediahx.constant.RespCode;
import com.mediahx.controller.BaseController;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.util.CommUtils;

/**
 * 球场信息
 * @author PW
 *
 */
@Controller
@RequestMapping("/stadium")
public class StadiumController extends BaseController{

	@Autowired
	private StadiumBo stadiumBo;
	
	/**
	 * 球场主页信息
	 * @param model
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/stadiumHomePage", produces = "application/json;charset=UTF-8")
	public void stadiumHomePage(Model model, HttpServletRequest request, PrintWriter out){
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.PARAMTER_ERR_CODE.getRtn(),RespCode.PARAMTER_ERR_CODE.getRtnMsg(), RespCode.PARAMTER_ERR_CODE.getRetCode());
		Map<String, Object> map = getAllParams(request, model);
		String longitude = map.get("longitude") + "";//经度
		String latitude = map.get("latitude")+"";//维度
		
		resp = stadiumBo.getAllStadiumInfo(longitude, latitude, resp);
		CommResp.printStrJson(resp, out);
	}
	
	/**
	 * 获取球场详情信息
	 * @param model
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/stadiumInfo", produces = "application/json;charset=UTF-8")
	public void stadiumInfo(Model model, HttpServletRequest request, PrintWriter out){
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.PARAMTER_ERR_CODE.getRtn(),RespCode.PARAMTER_ERR_CODE.getRtnMsg(), RespCode.PARAMTER_ERR_CODE.getRetCode());
		Map<String, Object> map = getAllParams(request, model);
		String stadiumId = map.get("stadiumId") + "";//球场主键id
		String time = map.get("time")+"";//时间
		
		if(!CommUtils.isEmpty(stadiumId) && !CommUtils.isEmpty(time)){
			
			resp = stadiumBo.getStadiumInfo(stadiumId, time, resp);
		}
		
		CommResp.printStrJson(resp, out);
	}
	
	/**
	 * 球场信息页面 加入拼场列表
	 * @param model
	 * @param request
	 * @param out
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/stadiumJoins", produces = "application/json;charset=UTF-8")
	public void stadiumJoins(Model model, HttpServletRequest request, PrintWriter out) throws ParseException{
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.PARAMTER_ERR_CODE.getRtn(),RespCode.PARAMTER_ERR_CODE.getRtnMsg(), RespCode.PARAMTER_ERR_CODE.getRetCode());
		Map<String, Object> map = getAllParams(request, model);
		String longitude = map.get("longitude") + "";//经度
		String latitude = map.get("latitude")+"";//维度
		String stadiumDelId = map.get("stadiumDelId")+"";
		
		if(!CommUtils.isEmpty(stadiumDelId)){
			
			resp = stadiumBo.getJoinStadiumList(stadiumDelId, longitude, latitude, resp);
		}
		CommResp.printStrJson(resp, out);
	}
	
	/**
	 * 组队打球列表
	 * @param model
	 * @param request
	 * @param out
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/getballTeamList", produces = "application/json;charset=UTF-8")
	public void getballTeamList(Model model, HttpServletRequest request, PrintWriter out) throws ParseException{
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.PARAMTER_ERR_CODE.getRtn(),RespCode.PARAMTER_ERR_CODE.getRtnMsg(), RespCode.PARAMTER_ERR_CODE.getRetCode());
		RequestVo reqVo = getPagerParams(request, model);
		Map<String, Object> map = reqVo.getParams();
			
		resp = stadiumBo.getballTeamList(map, resp);
		
		CommResp.printStrJson(resp, out);
	}
	
	/**
	 * 组队详情信息
	 * @param model
	 * @param request
	 * @param out
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/ballTeamInfo", produces = "application/json;charset=UTF-8")
	public void ballTeamInfo(Model model, HttpServletRequest request, PrintWriter out) throws ParseException{
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.PARAMTER_ERR_CODE.getRtn(),RespCode.PARAMTER_ERR_CODE.getRtnMsg(), RespCode.PARAMTER_ERR_CODE.getRetCode());
		Map<String, Object> map = getAllParams(request, model);
		String orderId = map.get("orderId")+"";
		String userId = map.get("userId")+"";
		String lock  = map.get("lock")+"";
		if(!CommUtils.isEmpty(orderId)){
			
			resp = stadiumBo.ballTeamInfo(orderId, userId,lock, resp);
		}
		CommResp.printStrJson(resp, out);
	}
	
	
}
