package com.bufs.catbot.domain;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class HolidayResponseVO {


	private Map<String, String> header;
	private List<HolidayItemDTO> body;
	

	public Map<String, String> getHeader() {
		return header;
	}
	
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	
	@XmlElementWrapper(name="items")
	@XmlElement(name="item")
	public List<HolidayItemDTO> getBody() {
		return body;
	}
	
	public void setBody(List<HolidayItemDTO> body) {
		this.body = body;
	}
	
	
	
	
}
