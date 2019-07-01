package com.mediahx.bean;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.mediahx.model.Order;
import com.mediahx.model.UserInfo;

/**
 *
 * @author guomf
 * 2019年5月8日
 * project： 梦幻城堡
 */
@SuppressWarnings("serial")
public class OrderBean extends Order{
	private List<UserInfo> photoList;
	private String photo;
	@JSONField(format = "MM月dd日")
	private Date day;
	
	private String time;
	
	private String name;
	
	private Double price;

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
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

	public List<UserInfo> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<UserInfo> photoList) {
		this.photoList = photoList;
	}
}
