package com.bufs.catbot.domain;

public class MongoBusTimeFormat {

	private String time;
	private Integer hour;
	private Integer min;
	

	public MongoBusTimeFormat() {
		super();
	}
	

	
	public MongoBusTimeFormat(String time, Integer hour, Integer min) {
		super();
		this.time = time;
		this.hour = hour;
		this.min = min;
	}



	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer min) {
		this.min = min;
	}

	
	
	
	
	
}
