package com.mediahx.controller.msg;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mediahx.constant.RespCode;
import com.mediahx.controller.BaseController;
import com.mediahx.dao.mysql.AppMessageMapper;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.model.AppMessage;

/**
 *	消息
 * @author guomf
 * 2019年5月20日
 * project： 梦幻城堡
 */
@Controller
@RequestMapping("msg/AppMessage")
public class AppMessageController extends BaseController{
	
	@Autowired
	AppMessageMapper appMessageMapper;
	@RequestMapping(value = "/queryMsgs", produces = "application/json;charset=UTF-8")
	public void queryMsgs(Model model, HttpServletRequest request, PrintWriter out) {
		CommResp resp = SetCommRespMsg.defaultSet(RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());

		//查询消息数据（最多展示最近的5条）
		List<AppMessage> amList = appMessageMapper.queryMsgs();
		resp.setData(amList);
		
		CommResp.printStrJson(resp, out);
	}
}
