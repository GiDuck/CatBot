package com.bufs.catbot.domain;

import org.springframework.data.annotation.Id;

/*
 * 
 * 요청-응답 트리의 데이터를 가져오는 클래스
 * 
 * */

public class MongoDTO {

	@Id
	String id;
	String name;
	Double level;
	String parent;
	Boolean isTerminal;
	Object value;

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getLevel() {
		return level;
	}
	public void setLevel(Double level) {
		this.level = level;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public Boolean getIsTerminal() {
		return isTerminal;
	}
	public void setIsTerminal(Boolean isTerminal) {
		this.isTerminal = isTerminal;
	}
	
	
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "MongoDTO [id=" + id + ", name=" + name + ", level=" + level + ", parent=" + parent + ", isTerminal="
				+ isTerminal + "]";
	}

	
	
	
	
	
	
	
}
