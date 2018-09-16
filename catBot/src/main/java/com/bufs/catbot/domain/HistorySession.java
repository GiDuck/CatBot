package com.bufs.catbot.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class HistorySession {
	
	
	@Id
	private String id;
	private String user_id;
	private String name;
	private Double level;
	private Date time;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getLevel() {
		return level;
	}
	public void setLevel(Double level) {
		this.level = level;
	}
	
	
	
	
	
	

}
