package com.mediahx.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 地图工具
 * 
 * @author huangZQ
 *
 */
public class MapUtil {
	/**
	 * 补充：计算两点之间真实距离
	 * 
	 * @return 米
	 */
	public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
		// PI 返回圆周率（约等于3.14159）
		// 维度
		double lat1 = (Math.PI / 180) * latitude1;
		double lat2 = (Math.PI / 180) * latitude2;

		// 经度
		double lon1 = (Math.PI / 180) * longitude1;
		double lon2 = (Math.PI / 180) * longitude2;

		// 地球半径
		double R = 6371;
		// acos(x) 返回数的反余弦值;sin(x) 返回数的正弦;cos(x)返回数的余弦;
		// 两点间距离 km，如果想要米的话，结果*1000就可以了
		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

		return d * 1000;
	}

	/**
	 * 获取当前用户一定距离以内的经纬度值 单位米 return minLat 最小经度 minLng 最小纬度 maxLat 最大经度 maxLng 最大纬度
	 * minLat
	 */
	public static Map<String, Object> getAround(String latStr, String lngStr, String raidus) {
		Map<String, Object> map = new HashMap<String, Object>();

		Double latitude = Double.parseDouble(latStr);// 传值给纬度
		Double longitude = Double.parseDouble(lngStr);// 传值给经度

		Double degree = (24901 * 1609) / 360.0; // 获取每度
		double raidusMile = Double.parseDouble(raidus);

		Double mpdLng = Double.parseDouble((degree * Math.cos(latitude * (Math.PI / 180)) + "").replace("-", ""));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		// 获取最小经度
		Double minLng = longitude - radiusLng;
		// 获取最大经度
		Double maxLng = longitude + radiusLng;

		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		// 获取最小纬度
		Double minLat = latitude - radiusLat;
		// 获取最大纬度
		Double maxLat = latitude + radiusLat;

		map.put("minLat", minLat);// 最小纬度 maxLat
		map.put("maxLat", maxLat);// 最大纬度 minLat
		map.put("minLng", minLng);// 最小经度 minLng
		map.put("maxLng", maxLng);// 最大经度 maxLng

		return map;
	}
}
