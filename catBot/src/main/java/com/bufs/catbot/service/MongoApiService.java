package com.bufs.catbot.service;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.util.UriEncoder;

@Service
public class MongoApiService {

	private RestTemplate restTemplate = new RestTemplate();
	private final String serviceKey = "LRfAv2S42k%2BQXh7V7nYx28VbrpCEbf6rCX4GU5OBi1k%2FHUGXcsXy80ONnVUi%2FfJ64Xd0IT2ouRvlCUwDj1xXlw%3D%3D";
	private final String holidayURL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo";
	private String requestURL = null;

	public void requestHolidayInfo() {

		for (int i = 1; i < 13; ++i) {

			String solMonth = i < 10 ? "0" + i : String.valueOf(i);
			
			try {
				
				MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
				params.add("ServiceKey", serviceKey);
				params.add("solYear", "2018");
				params.add("solMonth", solMonth);
				
/*				String requestURI = UriComponentsBuilder.fromHttpUrl(holidayURL)
						.queryParams(params).build().toString();*/
										
				
				String requestURI = holidayURL + "?ServiceKey="+ UriEncoder.decode(serviceKey) + "&solYear=2018&" + "solMonth=" + solMonth;
				
				System.out.println("요청하는 URL은.. " + requestURI);

				String value = restTemplate.getForObject(requestURI, String.class);

				System.out.println("지금 들고온 휴일 정보는...");
				System.out.println(value);
				
/*				for (HolidayItemDTO item : items.getHolidayItemDTOs()) {

					System.out.println("지금 들고온 휴일 정보는...");
					System.out.println(item.toString());

				}*/

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}