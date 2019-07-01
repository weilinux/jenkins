package com.mediahx.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.mediahx.constant.RespCode;
import com.mediahx.message.Resp;
import com.mediahx.message.SetRespMsg;
import com.mediahx.util.CommUtils;
/**
 * 
 * @author ZHE
 *
 */
public class ParamsInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String versionString = request.getParameter("terminal");
		String url = request.getRequestURI();
		System.out.println("************："+url+"，000000000000000000000");
		//筛选不过滤的白名单
		String trulUrlmini = "/streetball/notifypay/miniProgramNotify";
		if((!trulUrlmini.equals(url) && CommUtils.isEmpty(versionString)) || (!CommUtils.isEmpty(versionString)) && !versionString.equals("ios") && !versionString.equals("android")){
			Resp resp = SetRespMsg.defaultSet(RespCode.PARAMTER_ERR_CODE.getRtn(), "当前客户端不允许请求！", RespCode.PARAMTER_ERR_CODE.getRetCode());
			Resp.printJson(resp, response.getWriter());
			return false;
		}
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
	throws Exception {
		// TODO Auto-generated method stub
		
	}

}