package com.mediahx.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.mediahx.bean.PageInfo;
import com.mediahx.bean.RequestVo;
import com.mediahx.message.CommResp;
import com.mediahx.message.Resp;
import com.mediahx.util.CommUtils;
import com.mediahx.util.ListUtils;

/**
 * Controller基类
 * 
 * @author ZHE
 *
 */
public class BaseController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String getCurrentUrl(HttpServletRequest request){
		return request.getServletPath().substring(1);
	}
	
	/**
	 * map转换json
	 * @param map
	 * @return
	 */
	public String ConvertMapToJson(Map<String,Object> map){
		String json=null;
		if(map!=null && map.size()!=0){
				json = JSON.toJSONString(map);
		}
		return json;
	}
	
	/**
	 * 
	 * 对请求参数做特殊处理
	 */
	public void beforeInvoke(Map<String, Object> params) {
		    params.put("pageNum", params.get("pageCurrent"));
			params.put("numPerPage",params.get("pageSize"));
	}

	/**
	 * 
	 * 对返回值做特殊处理
	 */
	public void afterInvoke(Model model, Resp resp) {
		
	}
	
	/**
     * 得到表单的所有数据 功能描述
     * 
     */
    public Map<String, Object> getAllParams(HttpServletRequest request, Model model)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<String> enu = request.getParameterNames();
        String name = "";
        String value = "";
        while (enu.hasMoreElements())
        {
            name = (String) enu.nextElement();
            value = request.getParameter(name);
            if (!CommUtils.isEmpty(value))
            {
                map.put(name, value);
            }
        }
        if (!map.isEmpty())
        {
            logger.info("页面传入参数: " + map);
            model.addAllAttributes(map);
        }
        return map;
    }
    
    /**
     * 用于分页 功能描述
     * 
     * @param request
     * @return
     */
    public RequestVo getPagerParams(HttpServletRequest request, Model model)
    {
        Map<String, Object> pageMap = getAllParams(request, model);
        this.beforeInvoke(pageMap);
        PageInfo page = new PageInfo();
//        if (!pageMap.isEmpty())
//        {
            ListUtils.converPager(pageMap, page);
            // model.addAllAttributes(pageMap);
//        }
        logger.info("请求参数：{}",pageMap);
        model.addAttribute("pager", page);
        return new RequestVo(page, pageMap);
    }
    
    public RequestVo getPagerCommParams(HttpServletRequest request, Model model,CommResp resp)
    {
        Map<String, Object> pageMap = getAllParams(request, model);
        this.beforeInvoke(pageMap);
//        if (!pageMap.isEmpty())
//        {
            ListUtils.converPager(pageMap, resp);
            // model.addAllAttributes(pageMap);
//        }
        logger.info("请求参数：{}",pageMap);
        model.addAttribute("pager", resp);
        return new RequestVo(resp, pageMap);
    }

    /**
     * 获取操作机器的IP 功能描述
     * 
     */
    public String getRemortIP(HttpServletRequest request)
    {
        if (request.getHeader("x-forwarded-for") == null)
        {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
	
	// 获取基于应用程序的url绝对路径
	public final String getAppbaseUrl(HttpServletRequest request, String url) {
		return request.getContextPath() + url;
	}

	// 根据url获取当前控制器名称
	public String[] controllerName(HttpServletRequest request) {
		String url = request.getRequestURI();
		String[] urlArr = url.split("/");
		return urlArr;
	}

	// 根据url获取当前操作名称
	public String actionName(String url) {
		return url;
	}
}
