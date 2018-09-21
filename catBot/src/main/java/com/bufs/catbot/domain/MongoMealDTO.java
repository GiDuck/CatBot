package com.bufs.catbot.domain;

import org.springframework.data.annotation.Id;

public class MongoMealDTO {

	
	@Id
	private String id;
	private String restaurantType;
	private String menu;
	private String date;
	private String content;
	private String price;
	private String startTime;
	private String endTime;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRestaurantType() {
		return restaurantType;
	}
	public void setRestaurantType(String restaurantType) {
		this.restaurantType = restaurantType;
	}
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	@Override
	public String toString() {
		return "MongoMealDTO [id=" + id + ", restaurantType=" + restaurantType + ", menu=" + menu + ", date=" + date
				+ ", content=" + content + ", price=" + price + ", startTime=" + startTime + ", endTime=" + endTime
				+ "]";
	}
	
	
	
	
	
	
}
