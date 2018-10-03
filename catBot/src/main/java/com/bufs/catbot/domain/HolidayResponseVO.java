package com.bufs.catbot.domain;

import java.util.Map;

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
	
	
	
	
}
