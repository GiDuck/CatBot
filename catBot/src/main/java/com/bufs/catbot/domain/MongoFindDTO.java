package com.bufs.catbot.domain;

import org.springframework.data.annotation.Id;

public class MongoFindDTO {
	
	@Id
	String _id;
	String 이름;
	String 부서;
	String 전화번호;
	String 위치;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String get이름() {
		return 이름;
	}
	public void set이름(String 이름) {
		this.이름 = 이름;
	}
	public String get부서() {
		return 부서;
	}
	public void set부서(String 부서) {
		this.부서 = 부서;
	}
	public String get전화번호() {
		return 전화번호;
	}
	public void set전화번호(String 전화번호) {
		this.전화번호 = 전화번호;
	}
	public String get위치() {
		return 위치;
	}
	public void set위치(String 위치) {
		this.위치 = 위치;
	}
	
	
	
	
	
	
}
