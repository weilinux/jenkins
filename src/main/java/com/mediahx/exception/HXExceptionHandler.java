package com.mediahx.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.mediahx.constant.RespCode;
import com.mediahx.message.Resp;
import com.mediahx.message.SetRespMsg;
/**
 * 
 * @author ZHE
 *
 */
public class HXExceptionHandler implements HandlerExceptionResolver {  
	private static Logger logger = LoggerFactory.getLogger(HXExceptionHandler.class);
	
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,  
            Exception ex) {  
    	logger.error("后台异常",ex);
    	Map<String, Object> model = new HashMap<String, Object>();  
        
        String message=ex.getMessage();
        if(message!=null){
        	message=message.replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>");
        }
        // 根据不同错误设置描述
        if(ex instanceof BadSqlGrammarException){
            message="数据库异常,sql语法错误";
        }else if(ex instanceof UncategorizedSQLException){
            message="数据库异常,无法转换为内部表示";
        }else if(ex instanceof NumberFormatException){
        	message="转换成数字类型异常";
        }
        Resp resp = SetRespMsg.defaultSet(RespCode.SYSTEM_ERROR_CODE.getRtn(), message, RespCode.SYSTEM_ERROR_CODE.getRetCode());
        model.put("ex", resp);
        return new ModelAndView("common/500", model);  
    }  
    
} 