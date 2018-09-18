package com.bufs.catbot.domain;

import org.springframework.data.annotation.Id;

public class MongoBusDTO {


	@Id
	private String id;
	private String 시간;
	private String 타입;
	private Boolean isWeekend;
	private String station;
	private String bound;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String get시간() {
		return 시간;
	}
	public void set시간(String 시간) {
		this.시간 = 시간;
	}
	public String get타입() {
		return 타입;
	}
	public void set타입(String 타입) {
		this.타입 = 타입;
	}
	public Boolean getIsWeekend() {
		return isWeekend;
	}
	public void setIsWeekend(Boolean isWeekend) {
		this.isWeekend = isWeekend;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getBound() {
		return bound;
	}
	public void setBound(String bound) {
		this.bound = bound;
	}
	
	
	
	
	
}
