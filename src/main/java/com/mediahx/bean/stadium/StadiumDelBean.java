package com.mediahx.bean.stadium;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 订单信息
 * 
 * @author PW
 *
 */
public class StadiumDelBean {

	/** 订单id */
	private String orderId;
	/** 组队暗号 */
	private String teamCode;
	/** 是否已加入该队伍 */
	private String isInJoin;
	/** 是否发起人 */
	private String isOriginator;
	/** 时间 */
	private String time;
	/** 球场名称+距离 */
	private String stadiumPlace;
	/** 球场地址 */
	private String address;
	/** 场次类型（包场:group，拼场: match） */
	private String orderClass;
	/** 价格 */
	private Double price;
	/** 价格描述 */
	private String priceDes;
	/** 发起人图片 */
	private String photo;
	/** 图片集合 */
	private List<Map<String, Object>> images;
	/** 锁住人数 */
	private Integer lockNum;
	/** 参与人数 */
	private Integer actualSize;
	/** 参与人数/配置场次总人数 */
	private String actualNum;
	/** 状态 */
	private String status;
	/** 状态描述 */
	private String statusDes;
	/** 发布时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date orderTime;
	/** 是否锁单 */
	private String isLock;
	/** 球场经度 */
	private String longitude;
	/** 球场纬度 */
	private String latitude;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	public String getIsInJoin() {
		return isInJoin;
	}

	public void setIsInJoin(String isInJoin) {
		this.isInJoin = isInJoin;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStadiumPlace() {
		return stadiumPlace;
	}

	public void setStadiumPlace(String stadiumPlace) {
		this.stadiumPlace = stadiumPlace;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOrderClass() {
		return orderClass;
	}

	public void setOrderClass(String orderClass) {
		this.orderClass = orderClass;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPriceDes() {
		return priceDes;
	}

	public void setPriceDes(String priceDes) {
		this.priceDes = priceDes;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public List<Map<String, Object>> getImages() {
		return images;
	}

	public void setImages(List<Map<String, Object>> images) {
		this.images = images;
	}

	public Integer getLockNum() {
		return lockNum;
	}

	public void setLockNum(Integer lockNum) {
		this.lockNum = lockNum;
	}

	public Integer getActualSize() {
		return actualSize;
	}

	public void setActualSize(Integer actualSize) {
		this.actualSize = actualSize;
	}

	public String getActualNum() {
		return actualNum;
	}

	public void setActualNum(String actualNum) {
		this.actualNum = actualNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDes() {
		return statusDes;
	}

	public void setStatusDes(String statusDes) {
		this.statusDes = statusDes;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getIsOriginator() {
		return isOriginator;
	}

	public void setIsOriginator(String isOriginator) {
		this.isOriginator = isOriginator;
	}

	public String getIsLock() {
		return isLock;
	}

	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}
