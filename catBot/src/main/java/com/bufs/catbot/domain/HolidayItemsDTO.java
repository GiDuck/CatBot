package com.bufs.catbot.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class HolidayItemsDTO {
	
	@XmlElementWrapper(name="items")
	@XmlElement(name="item")
	private List<HolidayItemDTO> HolidayItemDTOs;

	public List<HolidayItemDTO> getHolidayItemDTOs() {
		return HolidayItemDTOs;
	}

	public void setHolidayItemDTOs(List<HolidayItemDTO> holidayItemDTOs) {
		HolidayItemDTOs = holidayItemDTOs;
	}
		
		
		
}
	

