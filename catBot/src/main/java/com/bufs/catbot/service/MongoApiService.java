package com.bufs.catbot.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bufs.catbot.domain.HolidayItemDTO;
import com.bufs.catbot.domain.HolidayResponseVO;
import com.bufs.catbot.persistence.MongoDAO;

@Service
public class MongoApiService {

	@Autowired
	private MongoDAO mongoDAO;
	
	private RestTemplate restTemplate = new RestTemplate();
	private final String serviceKey = "LRfAv2S42k%2BQXh7V7nYx28VbrpCEbf6rCX4GU5OBi1k%2FHUGXcsXy80ONnVUi%2FfJ64Xd0IT2ouRvlCUwDj1xXlw%3D%3D";
	private final String holidayURL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo";
	private URI requestURI;
	
	
	public void requestHolidayInfo() {
		
		List<HolidayItemDTO> items = null;

		for (int i = 1; i < 13; ++i) {

			String solMonth = i < 10 ? "0" + i : String.valueOf(i);
			
			try {
				
				requestURI = new URI(holidayURL + "?ServiceKey="+ serviceKey + "&solYear=2018&" + "solMonth=" + solMonth);
				
				HolidayResponseVO response = restTemplate.getForObject(requestURI, HolidayResponseVO.class);		
				
				
			items = response.getBody().getItems();
			
			List<Map<String, String>> holidayInfos = new ArrayList<>();
			Map<String, String> holiday = null;
			
			for(HolidayItemDTO item : items) {
				
				if(!item.getIsHoliday().equals("Y"))
					continue;
				
				holiday = new HashMap<>();
				holiday.put("date", item.getLocdate().substring(1, 4) + "-" + item.getLocdate().substring(5, 6) + "-" + item.getLocdate().substring(7, 8));
				holiday.put("name", item.getDateName());
				holiday.put("type", "공휴일");
			
				holidayInfos.add(holiday);
				
				
			}
			
			mongoDAO.insertHolidayInfo(holidayInfos);


			} catch (Exception e) {
				e.printStackTrace();	

			}


			
		}
	
	}
}