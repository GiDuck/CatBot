package com.bufs.catbot.domain;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
/*
 * 
 * 
 * JAXB를 사용하여 XML 매핑할 때 사용하는 도메인 클래스
 * 
 * */
@XmlRootElement(name="response")
public class HolidayResponseVO {


	private Map<String, String> header;
	private HolidayItemsDTO body;
	

	public Map<String, String> getHeader() {
		return header;
	}
	
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	

	public HolidayItemsDTO getBody() {
		return body;
	}
	
	public void setBody(HolidayItemsDTO body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "HolidayResponseVO [header=" + header + ", body=" + body + "]";
	}
	
	
	
	
	
}
