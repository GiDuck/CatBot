package com.bufs.catbot.domain;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class HolidayResponseVO {


	private Map<String, String> header;
	

	private HolidayItemsDTO body;
	
	
	@XmlElementWrapper(name="response")
	@XmlElement
	public Map<String, String> getHeader() {
		return header;
	}
	
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	
	
	@XmlElementWrapper(name="response")
	@XmlElement
	public HolidayItemsDTO getBody() {
		return body;
	}
	public void setBody(HolidayItemsDTO body) {
		this.body = body;
	}
	
	
	
	
}
