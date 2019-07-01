package com.mediahx.controller.user;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mediahx.bean.user.UserInfoBean;
import com.mediahx.constant.RespCode;
import com.mediahx.controller.BaseController;
import com.mediahx.dao.mysql.UserInfoMapper;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.model.UserInfo;
import com.mediahx.util.CommUtils;

@Controller
@RequestMapping("/userInfo")
public class UserInfoController extends BaseController{
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	/**
	 * 获取用户信息
	 * @param model
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/getUserInfo", produces = "application/json;charset=UTF-8")
	public void getUserInfo(Model model, HttpServletRequest request, PrintWriter out){
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.PARAMTER_ERR_CODE.getRtn(),RespCode.PARAMTER_ERR_CODE.getRtnMsg(), RespCode.PARAMTER_ERR_CODE.getRetCode());
		Map<String, Object> map = getAllParams(request, model);
		String userId = map.get("userId") + "";
		
		if(!CommUtils.isEmpty(userId)){
			
			UserInfoBean userInfo =userInfoMapper.selectUserInfoById(userId);
			resp = SetCommRespMsg.defaultSet(resp, RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
			resp.setObject(userInfo);
		}
		CommResp.printStrJson(resp, out);
	}
	
	/**
	 * 更新用户活动范围
	 * @param model
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateUserAdress", produces = "application/json;charset=UTF-8")
	public void updateUserAdress(Model model, HttpServletRequest request, PrintWriter out){
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.PARAMTER_ERR_CODE.getRtn(),RespCode.PARAMTER_ERR_CODE.getRtnMsg(), RespCode.PARAMTER_ERR_CODE.getRetCode());
		Map<String, Object> map = getAllParams(request, model);
		String userId = map.get("userId") + "";
		String provinceId = map.get("provinceId")+"";//省
		String cityId = map.get("cityId")+"";//市
		String areaId = map.get("areaId")+"";//区
		
		if(!CommUtils.isEmpty(userId)){
			
			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
			if(!CommUtils.isObjEmpty(userInfo)){
				userInfo.setProvince(provinceId);
				userInfo.setCity(cityId);
				userInfo.setArea(areaId);
				
				userInfoMapper.updateByPrimaryKeySelective(userInfo);
				
				resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), "修改成功", RespCode.SUCCESS_CODE.getRetCode());
				resp.setObject(userInfo);
				
			}else{
				resp = SetCommRespMsg.defaultSet(RespCode.NOT_FIND_USER_CODE.getRtn(),RespCode.NOT_FIND_USER_CODE.getRtnMsg(), RespCode.NOT_FIND_USER_CODE.getRetCode());
			}
		}
		CommResp.printStrJson(resp, out);
	}

}
