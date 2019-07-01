package com.mediahx.bean;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.mediahx.model.Order;
import com.mediahx.model.UserInfo;

/**
 * 订单详情
 * @author guomf
 * 2019年5月9日
 * project： 梦幻城堡
 */
@SuppressWarnings("serial")
public class OrderDetailBean extends Order {
	private List<UserInfo> photo;
	@JSONField(format = "MM月dd日")
	private Date day;
	
	private String time;
	
	@JSONField(format = "yyyy.MM.dd HH:mm")
	private Date orderTimee;
	
	private String name;
	
	private Double price;
	
	private Double refundAmt;
	
	private String address;
	
	private String flag;
	
	private String isLock;
	
	private String longitude;//经度
	
	private String latitude;//纬度

	public String getIsLock() {
		return isLock;
	}

	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Date getOrderTimee() {
		return orderTimee;
	}

	public void setOrderTimee(Date orderTimee) {
		this.orderTimee = orderTimee;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<UserInfo> getPhoto() {
		return photo;
	}

	public void setPhoto(List<UserInfo> photo) {
		this.photo = photo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Double getRefundAmt() {
		return refundAmt;
	}

	public void setRefundAmt(Double refundAmt) {
		this.refundAmt = refundAmt;
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
