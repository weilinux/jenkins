package com.mediahx.controller.user;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mediahx.bo.user.LoginMainBo;
import com.mediahx.constant.Constants;
import com.mediahx.constant.RespCode;
import com.mediahx.controller.BaseController;
import com.mediahx.dao.mysql.AreasMapper;
import com.mediahx.dao.mysql.CitiesMapper;
import com.mediahx.dao.mysql.ProvincesMapper;
import com.mediahx.dao.mysql.UserInfoMapper;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.model.Areas;
import com.mediahx.model.Cities;
import com.mediahx.model.Provinces;
import com.mediahx.model.UserInfo;
import com.mediahx.util.CommUtils;
import com.mediahx.util.WXBizDataCrypt;

@Controller
@RequestMapping("/user")
public class LoginMainController extends BaseController{
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Autowired
	private LoginMainBo loginMainBo;
	
	@Autowired
	private CitiesMapper citiesMapper;
	
	@Autowired
	private ProvincesMapper provincesMapper;
	
	@Autowired
	private AreasMapper areasMapper;
	
	
	/**
	 * 小程序解密
	 * @param model
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/minProgramDecrypt", produces = "application/json;charset=UTF-8")
	public void minProgramDecrypt(Model model, HttpServletRequest request, PrintWriter out){
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.PARAMTER_ERR_CODE.getRtn(),RespCode.PARAMTER_ERR_CODE.getRtnMsg(), RespCode.PARAMTER_ERR_CODE.getRetCode());
		Map<String, Object> map = getAllParams(request, model);
		
		String sessionKey = map.get("sessionKey") + "";
		String encryptedData = map.get("encryptedData") + "";
		String iv = map.get("iv") + "";
		
		if (!CommUtils.isEmpty(sessionKey) && !CommUtils.isEmpty(encryptedData) && !CommUtils.isEmpty(iv) ) {
			WXBizDataCrypt biz = new WXBizDataCrypt(Constants.MIN_PROGRAM_APP_ID, sessionKey);

			String result = biz.decryptData(encryptedData, iv);
			
			resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
			resp.setObject(result);
		}
		CommResp.printStrJson(resp, out);
	}
	
	/**
	 * 小程序登录/注册
	 * @param model
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkAndLogin", produces = "application/json;charset=UTF-8")
	public void checkAndLogin(Model model, HttpServletRequest request, PrintWriter out){
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.PARAMTER_ERR_CODE.getRtn(),RespCode.PARAMTER_ERR_CODE.getRtnMsg(), RespCode.PARAMTER_ERR_CODE.getRetCode());
		Map<String, Object> map = getAllParams(request, model);
		String mobilePhone = map.get("mobilePhone") + "";
		String weixinId = map.get("weixinId")+"";//微信用户ID
		String nickName = map.get("nickName")+"";//呢称
		String gender = map.get("gender")+"";//性别
		String iconurl = map.get("iconurl")+"";//头像
		
		if(!CommUtils.isEmpty(weixinId) && !CommUtils.isEmpty(mobilePhone)){
			
			resp = loginMainBo.checkLogin(weixinId, mobilePhone, nickName, gender, iconurl, request, resp);
		}
		
		CommResp.printStrJson(resp, out);
	}
	
	/**
	 * 填写个人信息
	 * @param model
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/modifyTheUser", produces = "application/json;charset=UTF-8")
	public void modifyTheUser(Model model, HttpServletRequest request, PrintWriter out){
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.PARAMTER_ERR_CODE.getRtn(),RespCode.PARAMTER_ERR_CODE.getRtnMsg(), RespCode.PARAMTER_ERR_CODE.getRetCode());
		Map<String, Object> map = getAllParams(request, model);
		
		String userId = map.get("userId")+"";
		Integer height = Integer.valueOf(map.get("height")+"");//身高
		Integer age = Integer.valueOf(map.get("age")+"");//年龄
		Double weight = Double.valueOf(map.get("weight")+"");//体重
		String level = map.get("level")+"";//篮球水平级别
		String position = map.get("position")+"";//篮球场上的位置
		String frequency = map.get("frequency")+"";//打球频率
		String provinceId = map.get("provinceId")+"";//省
		String cityId = map.get("cityId")+"";//市
		String areaId = map.get("areaId")+"";//区
		String gender=map.get("gender")+"";//性别
		if(!CommUtils.isEmpty(userId) && !CommUtils.isObjEmpty(height) && !CommUtils.isObjEmpty(age) && !CommUtils.isObjEmpty(weight) 
				&& !CommUtils.isEmpty(level) && !CommUtils.isEmpty(position) && !CommUtils.isEmpty(frequency)){
			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
			if(!CommUtils.isObjEmpty(userInfo)){
				userInfo.setFrequency(frequency);
				userInfo.setHeight(height);
				userInfo.setLevel(level);
				userInfo.setPosition(position);
				if(weight<1000){
					userInfo.setWeight(weight);
				}
				userInfo.setAge(age);
				userInfo.setProvince(provinceId);
				userInfo.setCity(cityId);
				userInfo.setArea(areaId);
				userInfo.setGender(gender);
				userInfoMapper.updateByPrimaryKeySelective(userInfo);
				
				resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), "保存成功.", RespCode.SUCCESS_CODE.getRetCode());
				resp.setObject(userInfo);
				
			}else{
				resp = SetCommRespMsg.defaultSet(RespCode.NOT_FIND_USER_CODE.getRtn(),RespCode.NOT_FIND_USER_CODE.getRtnMsg(), RespCode.NOT_FIND_USER_CODE.getRetCode());
			}
		}
		
		CommResp.printStrJson(resp, out);
	}
	
	/**
	 * 查询全部省或单个省
	 * @param provinceid   省编号
	 * @param out
	 * @param response
	 */
	@RequestMapping(value = "/findProvinces",produces = "application/json;charset=UTF-8")
	public void findProvinces(String provinceId,HttpServletRequest request, PrintWriter out) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		
		if(!CommUtils.isEmpty(provinceId)) {
			Provinces provinces = provincesMapper.findByProvinceId(provinceId);
		    resp.setObject(provinces);
		}else{
			List<Provinces> list = provincesMapper.findAll();
			resp.setData(list);
		}
		CommResp.printStrJson(resp, out);
	} 
	
	/**
	 * 查询全部市或单个省下所有市或单个市
	 * @param cityId
	 * @param provinceid
	 * @param out
	 * @param response
	 */
	@RequestMapping(value = "/findCities",produces = "application/json;charset=UTF-8")
	public void findCities(String cityId,String provinceId,HttpServletRequest request, PrintWriter out) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		
		if(!CommUtils.isEmpty(cityId)) {
			Cities cities = citiesMapper.findByCityId(cityId);
		    resp.setObject(cities);
		}else if(CommUtils.isEmpty(cityId)&&CommUtils.isEmpty(provinceId)){
			List<Cities> list = citiesMapper.findAll();
			resp.setData(list);
		}else if(!CommUtils.isEmpty(provinceId)) {
			List<Cities> list = citiesMapper.findByProvinceId(provinceId);
			resp.setData(list);
		}
		CommResp.printStrJson(resp, out);
	} 
	
	/**
	 * 查询所有区或单个市下所有区或单个区
	 * @param areasId   区编号
	 * @param citiesId	市编号
	 * @param out
	 * @param response
	 */
	@RequestMapping(value = "/findAreas",produces = "application/json;charset=UTF-8")
	public void findAreas(String areaId,String cityId,HttpServletRequest request, PrintWriter out) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		
		if(!CommUtils.isEmpty(cityId)) {
			List<Areas> list = areasMapper.findByCityId(cityId);
		    resp.setData(list);
		}else if(CommUtils.isEmpty(cityId)&&CommUtils.isEmpty(areaId)){
			List<Areas> list = areasMapper.findAll();
			resp.setData(list);
		}else if(!CommUtils.isEmpty(areaId)) {
			Areas areas = areasMapper.findByAreaId(areaId);
			resp.setObject(areas);
		}
		CommResp.printStrJson(resp, out);
	}

}
