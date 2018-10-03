package com.bufs.catbot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bufs.catbot.domain.HolidayItemDTO;
import com.bufs.catbot.domain.HolidayItemsDTO;

@Service
public class MongoApiService {

	private RestTemplate restTemplate = new RestTemplate();
	private final String serviceKey = "LRfAv2S42k%2BQXh7V7nYx28VbrpCEbf6rCX4GU5OBi1k%2FHUGXcsXy80ONnVUi%2FfJ64Xd0IT2ouRvlCUwDj1xXlw%3D%3D";
	private final String holidayURL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo?ServiceKey="
			+ serviceKey;
	private String requestURL;

	public void requestHolidayInfo() {

		for (int i = 1; i < 13; ++i) {

			try {
				requestURL = holidayURL + "&solYear=2018&solMonth=" + (i < 10 ? i : "0" + i);

				HolidayItemsDTO items = restTemplate.getForObject(requestURL, HolidayItemsDTO.class);

				for (HolidayItemDTO item : items.getHolidayItemDTOs()) {

					System.out.println("지금 들고온 휴일 정보는...");
					System.out.println(item.toString());

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}