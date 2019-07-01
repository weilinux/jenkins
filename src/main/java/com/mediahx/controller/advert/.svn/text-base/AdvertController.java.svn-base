package com.mediahx.controller.advert;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mediahx.constant.RespCode;
import com.mediahx.controller.BaseController;
import com.mediahx.dao.mysql.AdvertMapper;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.model.Advert;
import com.mediahx.util.CommUtils;

/**
 * 广告
 * @author PW
 *
 */
@Controller
@RequestMapping("/advert")
public class AdvertController extends BaseController{
	
	@Autowired
	private AdvertMapper advertMapper;
	
	
	/**
	 * 获取banner图片
	 * @param model
	 * @param request
	 * @param out
	 */
	@RequestMapping(value = "/getAdvert", produces = "application/json;charset=UTF-8")
	public void getAdvert(Model model, HttpServletRequest request, PrintWriter out){
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.PARAMTER_ERR_CODE.getRtn(), RespCode.PARAMTER_ERR_CODE.getRtnMsg(), RespCode.PARAMTER_ERR_CODE.getRetCode());
    
		List<Advert> advert = advertMapper.selectAllAdvert();
		
		resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		resp.setData(advert);
		resp.setCount(advert.size());
		
	    CommResp.printStrJson(resp, out);
	}
	
	/**
	 * 展示广告详情
	 * @param model
	 * @param request
	 * @param out
	 */
	@RequestMapping(value = "/showAdvertDetails", produces = "application/json;charset=UTF-8")
	public String showAdvertDetails(Model model, HttpServletRequest request, PrintWriter out){
		Map<String, Object> map = getAllParams(request, model);
		String advertId = map.get("advertId") + "";
		
		String path = null;
		if(!CommUtils.isEmpty(advertId)){
			Advert advertDetail = advertMapper.selectByPrimaryKey(advertId);
			
			if (!CommUtils.isObjEmpty(advertDetail)) {
				if("0".equals(advertDetail.getType()) && !CommUtils.isEmpty(advertDetail.getImgPath())){
					model.addAttribute("advertDetail", advertDetail);
					path = getCurrentUrl(request);
				}else if("1".equals(advertDetail.getType()) && !CommUtils.isEmpty(advertDetail.getReferUrl())){
					path = advertDetail.getReferUrl();
					path=path.trim();
					String http_str = "http://";
					String https_str = "https://";
					if (!path.contains(http_str)&&!path.contains(https_str)) {
						path = http_str + path;
					}
					return "redirect:" + path;
				}
			}else if(CommUtils.isObjEmpty(advertDetail)){
				model.addAttribute("hideMsg", "无信息");
				model.addAttribute("errorMsg", "该广告内容已删除");
				return getCurrentUrl(request);
			}
		}
		return path;
	}
}
