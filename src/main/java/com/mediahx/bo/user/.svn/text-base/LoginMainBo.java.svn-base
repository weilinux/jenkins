package com.mediahx.bo.user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mediahx.constant.Constants;
import com.mediahx.constant.RespCode;
import com.mediahx.dao.mysql.UserInfoMapper;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.model.UserInfo;
import com.mediahx.util.CommUtils;
import com.mediahx.util.IdGenerateUtils;
import com.mediahx.util.StrUtil;

/**
 * 登录 注册
 * @author PW
 *
 */
@Component
public class LoginMainBo{

	@Autowired
	private UserInfoMapper userInfoMapper;

	
	/**
	 * 注册 、登录
	 * @param weixinId 微信用户ID
	 * @param mobilePhone 手机
	 * @param nickName 呢称
	 * @param gender 性别
	 * @param iconurl 头像
	 * @param request 
	 * @param resp
	 * @return
	 */
	public CommResp checkLogin(String weixinId, String mobilePhone, String nickName, String gender, 
			String iconurl, HttpServletRequest request, CommResp resp) {
		
		UserInfo userInfo = userInfoMapper.selectByUnionid(weixinId);//检查是有微信注册
		if (!CommUtils.isObjEmpty(userInfo)){//用户已注册
			if(!Constants.CONSTANTS_N.equals(userInfo.getStatus())){
				userInfo.setMobilePhone(mobilePhone);
				if(!CommUtils.isEmpty(iconurl)){
					try {
						userInfo.setPhoto(download(iconurl,request));
					} catch (Exception e) {
						resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "加载头像失败", RespCode.FAILURE_CODE.getRetCode());
						return resp;
					}
				}
				if(!CommUtils.isEmpty(gender))
					userInfo.setGender(gender);
				if(!CommUtils.isEmpty(nickName)){
					//是否含有特殊字符
					if(CommUtils.isSpecialChar(nickName)){
						nickName="????";
					}
					userInfo.setNickName(nickName);
				}
				userInfoMapper.updateByPrimaryKeySelective(userInfo);
				resp.setObject(userInfo);
				
			}else{
				resp = SetCommRespMsg.defaultSet(RespCode.DISABLE_USER_CODE.getRtn(), RespCode.DISABLE_USER_CODE.getRtnMsg(), RespCode.DISABLE_USER_CODE.getRetCode());
				return resp;
			}
		}else{//用户未注册 注册并登陆
			UserInfo user = new UserInfo();
			
			user.setId(IdGenerateUtils.getUUID());
			user.setMobilePhone(mobilePhone);
			user.setWeixinId(weixinId);
			if(!CommUtils.isEmpty(iconurl)){
				try {
					user.setPhoto(download(iconurl,request));
				} catch (Exception e) {
					resp = SetCommRespMsg.defaultSet(RespCode.FAILURE_CODE.getRtn(), "加载头像失败", RespCode.FAILURE_CODE.getRetCode());
					return resp;
				}
			}
			if(!CommUtils.isEmpty(gender)) 
				user.setGender(gender);
			if(!CommUtils.isEmpty(nickName)){
				//是否含有特殊字符
				if(CommUtils.isSpecialChar(nickName)){
					nickName="????";
				}
				user.setNickName(nickName);
			}
			user.setStatus(Constants.CONSTANTS_Y);
			user.setCreateTime(new Date());
			userInfoMapper.insertSelective(user);
			
			resp.setObject(user);
		}
		resp = SetCommRespMsg.defaultSet(resp, RespCode.SUCCESS_CODE.getRtn(), "登录成功！", RespCode.SUCCESS_CODE.getRetCode());
		
		return resp;
	}

	/**
	 * 读取网页图片路径并下载到本地
	 * @param urlPath
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static String download(String urlPath,HttpServletRequest request) throws Exception {
		String savePath =request.getServletContext().getRealPath("/upload") + File.separatorChar + "qqweixin" + File.separatorChar;
	    // 构造URL
	    URL url = new URL(urlPath);
	    // 打开连接
	    URLConnection con = url.openConnection();
	    //设置请求超时为5s
	    con.setConnectTimeout(5*1000);
	    // 输入流
	    InputStream is = con.getInputStream();
	    // 1K的数据缓冲
	    byte[] bs = new byte[1024];
	    // 读取到的数据长度
	    int len;
	    // 输出的文件流
	    File sf=new File(savePath);
	    if(!sf.exists()){
	        sf.mkdirs();
	    }
	    String extFileName=urlPath.substring(urlPath.lastIndexOf("/")+1,urlPath.length());//获取服务器上图片的名称
	    if(!extFileName.contains(".")){
	    	extFileName=extFileName+".png";
	    }
	    String filename =CommUtils.getNewFileName(Constants.SIX_POS, extFileName);
	    
	    OutputStream os = new FileOutputStream(sf.getPath()+File.separatorChar+filename);
	    String virtualPath=savePath+filename;//存入数据库的虚拟路径
	    // 开始读取
	    while ((len = is.read(bs)) != -1) {
	        os.write(bs, 0, len);
	    }
	    // 完毕，关闭所有链接
	    os.close();
	    is.close();
	   
	    virtualPath =StrUtil.getImgPath(request, "qqweixin"+ File.separatorChar + filename);
	    return virtualPath;
	}

	
}
