package com.bufs.catbot;

import java.util.List;

import org.springframework.data.annotation.Id;

public class MongoDTO {

	@Id
	String id;
	String name;
	Double level;
	String parent;
	Boolean isTerminal;
	List<String> value;

	
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
	public List<String> getValue() {
		return value;
	}
	public void setValue(List<String> value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "MongoDTO [id=" + id + ", name=" + name + ", level=" + level + ", parent=" + parent + ", isTerminal="
				+ isTerminal + ", value=" + value + "]";
	}

	
	
	
	
	
	
	
}
