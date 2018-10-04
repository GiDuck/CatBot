package com.bufs.catbot.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
/*
 * 
 * 
 * JAXB를 사용하여 XML 매핑할 때 사용하는 도메인 클래스
 * 
 * 
 * */
@XmlRootElement(name="body")
public class HolidayItemsDTO {
	

	private List<HolidayItemDTO> items;

	
	@XmlElementWrapper(name="items")
	@XmlElement(name="item")
	public List<HolidayItemDTO> getItems() {
		return items;
	}


	public void setItems(List<HolidayItemDTO> items) {
		this.items = items;
	}
	
	

		
}




