package com.bufs.catbot.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="items")
public class HolidayItemsDTO {
	
	private List<HolidayItemDTO> HolidayItemDTOs;


	@XmlElementWrapper(name="items")
	@XmlElement(name="item")
	public List<HolidayItemDTO> getHolidayItemDTOs() {
		return HolidayItemDTOs;
	}

	public void setHolidayItemDTOs(List<HolidayItemDTO> holidayItemDTOs) {
		HolidayItemDTOs = holidayItemDTOs;
	}
	


		
		
		
}
	

