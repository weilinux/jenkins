package com.mediahx.bo.stadium;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mediahx.bean.stadium.StadiumBean;
import com.mediahx.bean.stadium.StadiumDelBean;
import com.mediahx.bean.stadium.StadiumDetailBean;
import com.mediahx.bean.stadium.StadiumInfoBean;
import com.mediahx.constant.Constants;
import com.mediahx.constant.RespCode;
import com.mediahx.dao.mysql.OrderDetailMapper;
import com.mediahx.dao.mysql.OrderMapper;
import com.mediahx.dao.mysql.StadiumDetailMapper;
import com.mediahx.dao.mysql.StadiumMapper;
import com.mediahx.message.CommResp;
import com.mediahx.message.SetCommRespMsg;
import com.mediahx.model.Stadium;
import com.mediahx.model.UserInfo;
import com.mediahx.util.Arith;
import com.mediahx.util.CommUtils;
import com.mediahx.util.SortListUtil;

/**
 * 球场
 * @author PW
 *
 */
@Component
public class StadiumBo {
	
	@Autowired
	private StadiumMapper stadiumMapper;
	
	@Autowired
	private StadiumDetailMapper stadiumDetailMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderDetailMapper orderDetailMapper;

	/**
	 * 主页获取球场信息
	 * @param longitude
	 * @param latitude
	 * @param resp
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public CommResp getAllStadiumInfo(String longitude, String latitude, CommResp resp) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("nowTime", CommUtils.formatDate2Str(new Date(), "yyyy-MM-dd"));
		map.put("time30", CommUtils.addDay(CommUtils.str2Date(CommUtils.formatDate2Str(new Date(), "yyyy-MM-dd")), 30));
		List<Stadium> stadiumList = stadiumMapper.selectAllStadium(map);
		
		List<StadiumBean> stadiums = new ArrayList<StadiumBean>();
		for (Stadium stadium : stadiumList) {
			StadiumBean sb = new StadiumBean();
			sb.setStadium(stadium);
			
			if(!CommUtils.isEmpty(longitude) && !CommUtils.isEmpty(latitude)&!CommUtils.isEmpty(stadium.getLongitude()) && !CommUtils.isEmpty(stadium.getLatitude())){
				//1.计算相隔距离
				String distance = getDistance(stadium.getLongitude(), stadium.getLatitude(), longitude, latitude)+"";
				sb.setDistance(distance.substring(0,distance.indexOf(".")));
				String distanceDes = Arith.div(Double.valueOf(sb.getDistance()), 1000, 1) < 1 ? 
						sb.getDistance() +"m" : Arith.div(Double.valueOf(sb.getDistance()), 1000, 1)+"km";
				sb.setDistanceDes(distanceDes);
			}
			
			//2.计算场地费 :显示30天内后台设置的时间段包场最低～包场最高(包含今天)
			map.put("stadiumId", stadium.getId());
		
			Map<String,Object> price = stadiumDetailMapper.findStadiumPice(map);
			if(price != null && price.size() >0){
				String siteFees = Arith.subZeroAndDot(price.get("minPrice")+"") + "-" +Arith.subZeroAndDot(price.get("maxPrice")+"")+"元/场";
				sb.setSiteFees(siteFees);
				sb.setMinPrice(Double.valueOf(price.get("minPrice")+""));
			}
			
			//3.已预定用户=30天以内（含当天）包场成功用户+拼场成功发起人用户
			List<String> pohtos = orderMapper.findSucceedOrder(stadium.getId());
			Set<String> set = new HashSet<String>();
			List<String> list = new ArrayList<String>();
			for(String string:pohtos){
				 if(set.add(string)&&!CommUtils.isEmpty(string)){
					 list.add(string);
				 }
			}
			
			sb.setPepoleNum(list.size());
			sb.setImages(list.size() > 6 ? list.subList(0, 6) : list);//只需要前六张头像
			
			stadiums.add(sb);
		}
		//4.排序
		if(!CommUtils.isEmpty(longitude) && !CommUtils.isEmpty(latitude)){
			stadiums = (List<StadiumBean>) SortListUtil.sort(stadiums, "distance", SortListUtil.ASC);
		}else{
			stadiums = (List<StadiumBean>) SortListUtil.sort(stadiums, "minPrice", SortListUtil.ASC);
		}
		
		resp = SetCommRespMsg.defaultSet(resp, RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		resp.setData(stadiums);
		return resp;
	}
	
    /**
	 * 补充：计算两点之间真实距离
	 * @return 米
	 */
	public static double getDistance(String longitude1, String latitude1,String longitude2, String latitude2) {
		// PI 返回圆周率（约等于3.14159）
		// 维度
		double lat1 = (Math.PI / 180) * Double.valueOf(latitude1);
		double lat2 = (Math.PI / 180) * Double.valueOf(latitude2);

		// 经度
		double lon1 = (Math.PI / 180) * Double.valueOf(longitude1);
		double lon2 = (Math.PI / 180) * Double.valueOf(longitude2);

		// 地球半径
		double R = 6371;
		//acos(x)	返回数的反余弦值;sin(x)	返回数的正弦;cos(x)返回数的余弦;
		// 两点间距离 km，如果想要米的话，结果*1000就可以了
		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)* Math.cos(lat2) * Math.cos(lon2 - lon1))* R;

		return d * 1000;
	}

	/**
	 * 获取球场详情信息
	 * @param stadiumId
	 * @param time
	 * @param resp
	 * @return
	 */
	public CommResp getStadiumInfo(String stadiumId, String time, CommResp resp) {
		
		StadiumInfoBean stadiumIB = new StadiumInfoBean();
		//1.获取球场信息
		Stadium stadium = stadiumMapper.selectByPrimaryKey(stadiumId);
		if(!CommUtils.isObjEmpty(stadium) && Constants.CONSTANTS_Y.equals(stadium.getStatus())){
			//2.返回时间段的集合(传入时间30天内最大场次数)
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("stadiumId", stadiumId);
			map.put("startDay", time);
			map.put("endDay", CommUtils.addDay(CommUtils.str2Date(time), 30));
			List<StadiumDetailBean> stadiumDels = stadiumDetailMapper.selectByIdAndDay(map);
			
			List<StadiumDetailBean> list = new ArrayList<StadiumDetailBean>();
			int quantity=0;
			if(stadiumDels.size()>0){
				for(StadiumDetailBean st:stadiumDels){
					if(st.getQuantity()>quantity){
						quantity=st.getQuantity();
					}
					if(!CommUtils.isObjEmpty(st.getDay())&&time.equals(CommUtils.formatDate2Str(st.getDay(), "yyyy-MM-dd"))){
						list.add(st);
					}
				}
			}
			stadium.setQuantity(quantity);
			stadiumIB.setStadium(stadium);
			stadiumIB.setStadiumDels(list);
			stadiumIB.setTime(time);
			
			resp = SetCommRespMsg.defaultSet(resp, RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
			resp.setObject(stadiumIB);
		}else{
			resp = SetCommRespMsg.defaultSet(resp, RespCode.FAILURE_CODE.getRtn(), "未找到该球场", RespCode.FAILURE_CODE.getRetCode());
		}
		return resp;
	}

	/**
	 * 球场信息页面 加入拼场列表
	 * @param stadiumDelId
	 * @param longitude
	 * @param latitude 
	 * @param resp
	 * @return
	 * @throws ParseException 
	 */
	public CommResp getJoinStadiumList(String stadiumDelId, String longitude, String latitude, CommResp resp) throws ParseException {
		
		List<Map<String,Object>> joinList = stadiumDetailMapper.selectInfoById(stadiumDelId);
		List<StadiumDelBean> stadiumDels = parsingMap(longitude, latitude, joinList);
		
		if(!stadiumDels.isEmpty() && stadiumDels.size() >0){
			Collections.sort(stadiumDels,new Comparator<StadiumDelBean>() {
				@Override
				public int compare(StadiumDelBean o1, StadiumDelBean o2) {
					int ret = 0;
					//比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
					//1.比较价格
					if (o1.getPrice() > o2.getPrice()) {
						return 1;
					}else if (o1.getPrice() < o2.getPrice()){
						return -1;
					}else{
						//2.相等则比较参加人数(按参加人数排序,小于6的人数多的在前面，大于等于六的人数少的在前面)
						if(o1.getActualSize() < 6 && o2.getActualSize() <6){
							if(o1.getActualSize() != o2.getActualSize()){
								return o2.getActualSize() - o1.getActualSize();
							}else{
								//3.相等则比较发布时间
								ret = o2.getOrderTime().compareTo(o1.getOrderTime());
							}
						}else{
							if(o1.getActualSize() != o2.getActualSize()){
								return o1.getActualSize() - o2.getActualSize();
							}else{
								//3.相等则比较发布时间
								ret = o2.getOrderTime().compareTo(o1.getOrderTime());
							}
						}
					}
					return ret;
				}
			});
			resp = SetCommRespMsg.defaultSet(resp, RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		}else{
			resp = SetCommRespMsg.defaultSet(resp, RespCode.FAILURE_CODE.getRtn(), "该时段场次已下架.", RespCode.FAILURE_CODE.getRetCode());

		}
		
		resp.setData(stadiumDels);
		return resp;
	}
	/**
	 * 解析map 生成list返回集合
	 * @param longitude
	 * @param latitude
	 * @param joinList
	 * @return
	 * @throws ParseException
	 */
	public List<StadiumDelBean> parsingMap(String longitude, String latitude, List<Map<String, Object>> joinList) throws ParseException {
		List<StadiumDelBean> stadiumDels = new ArrayList<StadiumDelBean>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for (Map<String, Object> join : joinList) {
			
			if(Constants.ORDER.CLASS_GROUP.equals(join.get("order_class"))&&Constants.ORDER.FREE_TYPE_FRIEND.equals(join.get("free_type"))){
				continue;
			}
			String day=join.get("day")+"";
			String time=join.get("time")+"";
			if(!CommUtils.isEmpty(day)&&!CommUtils.isEmpty(time)&&time.contains("-")){
				String s = day+" "+time.substring(time.length()-5, time.length())+":00";
				if(sdf.parse(s).getTime()<new Date().getTime()){
					continue;
				}
			}
			
			StadiumDelBean sdb = new StadiumDelBean();
			sdb.setOrderId(join.get("id")+"");
			sdb.setOrderClass(join.get("order_class")+"");
			sdb.setPhoto(join.get("photo")+"");
			sdb.setLockNum(CommUtils.isEmpty(join.get("lock_size")+"") ? 0 : Integer.valueOf(join.get("lock_size")+""));
			sdb.setActualNum((join.get("actual_size")+"")+"/"+(join.get("member_size")+""));
			sdb.setStatus(join.get("status")+"");
			sdb.setActualSize(Integer.valueOf(join.get("actual_size")+""));
			sdb.setOrderTime(sdf.parse(join.get("order_time")+""));
			if(Constants.ORDER.CLASS_GROUP.equals(sdb.getOrderClass())){
				sdb.setPrice(0d);
				sdb.setPriceDes("拼场费:￥0");
			}else if(Constants.ORDER.CLASS_MATCH.equals(sdb.getOrderClass())){
				sdb.setPrice(Double.valueOf(join.get("minimum_amt")+""));
				double price=sdb.getPrice();
				double prepay_amt=Double.valueOf(join.get("prepay_amt")+"");
				if(price==prepay_amt){
					sdb.setPriceDes("拼场费:￥"+Arith.subZeroAndDot(join.get("minimum_amt")+""));
				}else{
					sdb.setPriceDes("拼场费:￥"+Arith.subZeroAndDot(join.get("minimum_amt")+"")+"-"+ Arith.subZeroAndDot(join.get("prepay_amt")+""));
				}
			}
			if(Constants.ORDER.STATUS_MATCHING.equals(join.get("status")+"")){
				sdb.setStatusDes("组队未成功");
			}else if(Constants.ORDER.STATUS_SUCCEED.equals(join.get("status")+"")){
				sdb.setStatusDes("组队成功");
			}
			sdb.setStadiumPlace(join.get("name")+"");
			if(!CommUtils.isEmpty(longitude) && !CommUtils.isEmpty(latitude)){
				//计算相隔距离
				String distance = getDistance(join.get("longitude")+"", join.get("latitude")+"", longitude, latitude)+"";
				String ss = distance.substring(0,distance.indexOf("."));
				String distanceDes = Arith.div(Double.valueOf(ss), 1000, 1) < 1 ? ss +"m" : Arith.div(Double.valueOf(ss), 1000, 1)+"km";
				
				sdb.setStadiumPlace((join.get("name")+"")+"  "+distanceDes);
			}
			sdb.setTime(isTomorrow(join.get("day")+"")+"  "+join.get("time"));
			
			stadiumDels.add(sdb);
		}
		return stadiumDels;
	}
	
	/**
	 * 获取组队打球列表
	 * @param map
	 * @param resp
	 * @return
	 * @throws ParseException 
	 */
	public CommResp getballTeamList(Map<String, Object> map, CommResp resp) throws ParseException {
		
		//删选条件 时间
		if(!CommUtils.isEmpty(map.get("startTime")+"")){
			map.put("startTime",map.get("startTime")+"-");
		}
		//删选条件 日期
		String day = map.get("day")+"";
		if(!CommUtils.isEmpty(day)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cl = Calendar.getInstance();
			
			switch (day) {
			case "today":
				map.put("startDay", sdf.format(cl.getTime()));
				break;
			case "tomorrow":
				cl.add(Calendar.DATE, 1);
				map.put("startDay", sdf.format(cl.getTime()));
				break;
			case "thisWeek":
				map = thisWeek(map);
				break;
			case "nextWeek":
				map = nextWeek(map);
				break;
			case "afterNextWeek":
				map = nextWeek(map);
				map.remove("startDay");
				break;
			default:
				break;
			}
		}
		
		List<Map<String,Object>> joinList = stadiumDetailMapper.getBallTeamList(map);
		String longitude = map.get("longitude")+"";
		String latitude = map.get("latitude")+"";
		
		List<StadiumDelBean> stadiumDels = parsingMap(longitude, latitude, joinList);
		
		resp = SetCommRespMsg.defaultSet(resp, RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
		resp.setData(stadiumDels);
		resp.setCount(stadiumDels.size());
		
		return resp;
	}
	
	
	/**
	 * 判断时间是否 今天 明天
	 * @param time
	 * @return
	 * @throws ParseException 
	 */
	public String isTomorrow(String time) throws ParseException{
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		time = sdf.format(sdf.parse(time));
		
		Calendar cl = Calendar.getInstance();
		String now = sdf.format(cl.getTime());
		cl.add(Calendar.DATE, 1);
		String tomorrow = sdf.format(cl.getTime());
		if(now.equals(time)){
			return "今天";
		}else if(tomorrow.equals(time)){
			return "明天";
		}else{
			time = sdf2.format(sdf.parse(time));
			return time;
		}
	}

	/**
	 * 获取本周时间
	 * @param map
	 * @return
	 */
	public Map<String,Object> thisWeek(Map<String,Object> map){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	     // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
	    int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
	    String nowTime = sdf.format(cal.getTime());
	    if (1 == dayWeek) {//周日则之间返回当天
	    	 map.put("startDay", nowTime);
	    	 return map;
	    }
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int day = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day + 6);
		String endDay = sdf.format(cal.getTime());
		
	    map.put("startDay", nowTime);
	    map.put("endDay", endDay);
		return map;
	}
	
	/**
	 * 获取下周时间
	 * @param map
	 * @return
	 */
	public Map<String,Object> nextWeek(Map<String,Object> map){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 获得今天日期是一周的第几天
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        // 获得入参日期相对于下周一的偏移量（在国外，星期一是一周的第二天，所以下周一是这周的第九天）
        // 若当前日期是周日，它的下周一偏移量是1
        int nextMondayOffset = dayOfWeek == 1 ? 1 : 9 - dayOfWeek;

        // 增加到入参日期的下周一
        cal.add(Calendar.DAY_OF_MONTH, nextMondayOffset);
        String nowTime = sdf.format(cal.getTime());
        //下周一加6天得到下周日
        cal.add(Calendar.DAY_OF_MONTH, 6);
        String endDay = sdf.format(cal.getTime());
		
	    map.put("startDay", nowTime);
	    map.put("endDay", endDay);
		return map;
	}
	
	/**
	 * 组队详情信息
	 * @param orderId
	 * @param userId 
	 * @param resp
	 * @return
	 * @throws ParseException 
	 */
	public CommResp ballTeamInfo(String orderId, String userId,String lock, CommResp resp) throws ParseException {
		
		Map<String,Object> ballInfo = orderMapper.selectBallTeam(orderId);
		if(ballInfo != null && ballInfo.size() >0){
		    Integer member_size= Integer.valueOf(ballInfo.get("member_size")+"");//设置球员人数
		    Integer actual_size= Integer.valueOf(ballInfo.get("actual_size")+"");//实际加入人数
		    Integer lock_size= Integer.valueOf(ballInfo.get("lock_size")+"");//锁位数量
		    boolean boo= false;
		    
		    if(!CommUtils.isObjEmpty(member_size)&&!CommUtils.isObjEmpty(actual_size)&&!CommUtils.isObjEmpty(lock_size)){
		    	
		    	if(member_size-actual_size>lock_size){
		    		boo=true;
		    	}else{
		    		if(lock_size>0&&Constants.CONSTANTS_Y.equals(lock)){
		    			boo=true;
		    		}
		    	}
		    }
		
			
			//获得图片集合 + 发起者图片
			List<UserInfo> orderDetil = orderDetailMapper.selectPhotoByOrderId(orderId);
			List<Map<String,Object>> photos = new ArrayList<Map<String,Object>>();
			Map<String,Object> sMap = new HashMap<String,Object>();
			sMap.put("id", ballInfo.get("user_id")+"");
			sMap.put("photo", ballInfo.get("photo")+"");
			photos.add(sMap);
			
			String isInJoin = "N";//是否加入该队伍
			String isOriginator = "N";//是否发起人
			if(!CommUtils.isEmpty(userId)&&userId.equals(ballInfo.get("user_id"))){
				isInJoin = "Y";
				isOriginator = "Y";
			}
			for (UserInfo userInfo : orderDetil) {
				if(!CommUtils.isEmpty(userId)&&userId.equals(userInfo.getId())){
					isInJoin = "Y";
				}
				Map<String,Object> ssMap = new HashMap<String,Object>();
				ssMap.put("id", userInfo.getId());
				ssMap.put("photo", userInfo.getPhoto());
				photos.add(ssMap);
			}
			
			StadiumDelBean sdb = new StadiumDelBean();
			sdb.setLatitude(ballInfo.get("latitude")+"");
			sdb.setLongitude(ballInfo.get("longitude")+"");
			sdb.setStatus(ballInfo.get("status")+"");
			sdb.setStatusDes(Constants.ORDER.STATUS_NAME.get(sdb.getStatus()));
			sdb.setActualNum(ballInfo.get("member_size")+"");
			sdb.setActualSize(Integer.valueOf(ballInfo.get("actual_size")+""));
			sdb.setOrderId(ballInfo.get("id")+"");
			sdb.setTeamCode(CommUtils.isEmpty(ballInfo.get("team_code")+"") ? "" : ballInfo.get("team_code")+"");
			sdb.setImages(photos);
			if(!CommUtils.isObjEmpty(ballInfo.get("lock_num"))&&Integer.valueOf(ballInfo.get("lock_num")+"")>0){
				sdb.setIsLock("Y");
			}else{
				sdb.setIsLock("N");
			}
			sdb.setIsInJoin(isInJoin);
			sdb.setIsOriginator(isOriginator);
			sdb.setStadiumPlace(ballInfo.get("name")+"");
			sdb.setAddress(ballInfo.get("address")+"");
			sdb.setOrderClass(ballInfo.get("order_class")+"");
			sdb.setLockNum(CommUtils.isEmpty(ballInfo.get("lock_size")+"") ? 0 : Integer.valueOf(ballInfo.get("lock_size")+""));
			sdb.setTime(isTomorrow(ballInfo.get("day")+"")+"  "+ballInfo.get("time"));
			if(Constants.ORDER.CLASS_GROUP.equals(sdb.getOrderClass())){
				if("Y".equals(isOriginator)){
					sdb.setPriceDes("￥"+ballInfo.get("site_amt"));
					sdb.setPrice(Double.valueOf(ballInfo.get("site_amt")+""));
				}else{
					sdb.setPriceDes("￥0");
					sdb.setPrice(0.0);
				}
			}else if(Constants.ORDER.CLASS_MATCH.equals(sdb.getOrderClass())){
				if("Y".equals(isOriginator)){
					sdb.setPriceDes("￥"+ballInfo.get("site_amt"));
					sdb.setPrice(Double.valueOf(ballInfo.get("site_amt")+""));
				}else{
					sdb.setPriceDes("￥"+ballInfo.get("prepay_amt"));
					sdb.setPrice(Double.valueOf(ballInfo.get("prepay_amt")+""));
				}
			}
			if(Constants.ORDER.STATUS_FAILED.equals(ballInfo.get("status"))){
				resp = SetCommRespMsg.defaultSet(resp, RespCode.SUCCESS_CODE.getRtn(), "该订单已失败、请加入其他组队.", RespCode.SUCCESS_CODE.getRetCode());
			} else if(!boo&&"N".equals(isInJoin)){
				resp = SetCommRespMsg.defaultSet(resp, RespCode.FAILURE_CODE.getRtn(), "人员已满.", RespCode.FAILURE_CODE.getRetCode());
			}else{
				resp = SetCommRespMsg.defaultSet(resp, RespCode.SUCCESS_CODE.getRtn(), RespCode.SUCCESS_CODE.getRtnMsg(), RespCode.SUCCESS_CODE.getRetCode());
			}
			resp.setObject(sdb);
			
		}else{
			resp = SetCommRespMsg.defaultSet(resp, RespCode.FAILURE_CODE.getRtn(), "未找到该订单。", RespCode.FAILURE_CODE.getRetCode());
		}
		return resp;
	}

	
	
}
