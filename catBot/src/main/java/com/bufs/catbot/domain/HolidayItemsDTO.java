package com.bufs.catbot.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="items")
public class HolidayItemsDTO {
	

	private List<HolidayItemDTO> items;

	
	@XmlElementWrapper(name="body")
	@XmlElement(name="items")
	public List<HolidayItemDTO> getItems() {
		return items;
	}

	public void setItems(List<HolidayItemDTO> items) {
		this.items = items;
	}
	
	

		
}




