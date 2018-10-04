package com.bufs.catbot.domain;

import org.springframework.data.annotation.Id;

/*
 * 냥냥봇으로 사용자가 요청사항을 보낼때 사용하는 도메인 
 * 
 * */

public class CatRequestForm {
	
	@Id
	String id;
	String date;
	String content;
	String user_key;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getUser_key() {
		return user_key;
	}
	public void setUser_key(String user_key) {
		this.user_key = user_key;
	}
	
	

}
