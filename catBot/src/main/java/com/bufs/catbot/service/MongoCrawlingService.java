package com.bufs.catbot.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bufs.catbot.domain.MongoMealDTO;
import com.bufs.catbot.persistence.MongoCrawlingDAO;


@Service
public class MongoCrawlingService {
	
	
	@Autowired
	private MongoCrawlingDAO mongoCrawlingDAO;
	
	//셀레늄을 사용하기 위한 웹 드라이버
	private WebDriver driver;
	
	//드라이버 및 URL 정보
	public static final String DRIVER_PATH = "/usr/bin/chromedriver";
	public static final String DRIVER_NAME = "webdriver.chrome.driver";
	public static final String MEAL_TABLE_URL = "http://app.bufs.ac.kr/food.aspx";

	
	//학식을 크롤링 하는 로직
	public void CrawlMealTable() {
		
		//크롬 드라이버로 설정
		ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
		

		System.setProperty(DRIVER_PATH, DRIVER_NAME);
		driver = new ChromeDriver(chromeOptions);
		
		driver.get(MEAL_TABLE_URL);
		WebElement element = null;
		
		try {
		
		//기숙사 학식 정보 가져오기	
		BatchInsert(element);
			

		new Select(driver.findElement(By.id("ddl식당"))).selectByVisibleText("교직원 식당"); 
	    //driver.findElement(By.cssSelector("option[value=\"25\"]")).click();
	    Thread.sleep(500);
		BatchInsert(element);

	    
	    //학생식당K 학식 정보 가져오기
	    new Select(driver.findElement(By.id("ddl식당"))).selectByVisibleText("학생식당 K");  
	    Thread.sleep(500); //시간 500밀리세컨드 동안 슬립
		BatchInsert(element);


	    
	    
	    //학생식당O 학식 정보 가져오기
	    new Select(driver.findElement(By.id("ddl식당"))).selectByVisibleText("학생식당 O");
	    Thread.sleep(500);
		BatchInsert(element);


	    
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			driver.close();
			
		}
		
	}
	
	
	//DB에 있는 학식 정보를 가져오는 서비스
	public Map<String, Object> getTodayMenu(String token) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		Date today =new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nowDateStr = dateFormat.format(today);
		// 오늘 날짜를 기준으로 학식 정보를 가져온다.
		
		List<MongoMealDTO> meals = mongoCrawlingDAO.getMealInfo(token, nowDateStr);
		

		//만약 가져온 학식 정보가 없으면 정보가 없다는 안내 메시지를 담음
		if(meals == null || meals.size()== 0)
		{
			result.put("text", "학식 정보가 없다냥");
			
			return result;
			
		}
		
		
		//String Buffer를 통해 학식 메시지를 출력한다.
		StringBuffer messageBuffer = new StringBuffer("오늘 날짜 : ").append(nowDateStr).append("\n");
		messageBuffer.append("여기는 ").append(meals.get(0).getRestaurantType()).append(" 이라냥 \n\n");
	
		
		for(MongoMealDTO meal : meals) {

			messageBuffer.append("메뉴 : ").append(meal.getMenu()).append("\n")
			.append("내용 : ").append(meal.getContent()).append("\n")
			.append("가격 : ").append(meal.getPrice()).append("\n")
			.append("판매 시작시간 : ").append(meal.getStartTime()).append("\n")
			.append("판매 종료시간 : ").append(meal.getEndTime()).append("\n")
			.append("---------------------------------")
			.append("\n\n");

			
		}
		
		result.put("text", messageBuffer.toString());
		
		return result;
		
		
		
	}
	
	
	//크롤링한 데이터를 가져와서 DTO로 매핑하여 DB에 삽입하는 서비스
	public void BatchInsert(WebElement element) {
		
		
		//IblTitle이라는 이름을 가진 요소를 찾는다. 요소 안의 값은 [name] 이런식으로 되어있는데 여기서 [] 를 없애준다. (전처리 과정)
		element = driver.findElement(By.id("lblTitle"));
		String restaurantName = element.getText().replace("[","").replace("]", "");
		restaurantName = restaurantName.replace(" ", "");
		
		//DTO 리스트 초기화
		List<MongoMealDTO> meals = new ArrayList<>();
		
		
		//학식 목록 테이블에서 tr 태그를 추출하여 이터레이터로 travels 한다.
	    element = driver.findElement(By.className("tbl-type01")).findElement(ByTagName.tagName("tbody"));
	    List<WebElement> elements = element.findElements(ByTagName.tagName("tr"));
	    
	    Iterator<WebElement> itr = elements.iterator();	    
	    
	    
	    for(;itr.hasNext();) {
	    
	    	//이터레이터를 통해 tr 태그 안의 td 태그를 순회할 때 리스트 같이 순서대로 나오기 때문에 count 변수를 통해 DTO에 들어갈 변수를 구분한다.
    		int count = 0;
	    	WebElement line = itr.next();
	    	
	    	if(line.isDisplayed()) {

	    		
	    		List<WebElement> innerElements = line.findElements(ByTagName.tagName("td"));	
		    	MongoMealDTO mealDto = new MongoMealDTO();
		    	mealDto.setRestaurantType(restaurantName);

	    		
	    		Iterator<WebElement> innerItr = innerElements.iterator();
	    	
	    		for(;innerItr.hasNext();) {
	    			
	    			WebElement innerElement = innerItr.next();
	    			String content = innerElement.getText();
	    			   				    			
	    			switch(count) {
	    			
	    			case 0 : mealDto.setDate(content); break;
	    			case 1 : mealDto.setMenu(content); break;
	    			case 2 : mealDto.setContent(content); break;
	    			case 3 : mealDto.setPrice(content); break;
	    			case 4 : mealDto.setStartTime(content); break;
	    			case 5 : mealDto.setEndTime(content); break;
	    			default : break;
	    			
	    			}
	    			
		    		
	    			count++;


	    	}
	    		
	    		meals.add(mealDto);

		    

	    		
	    		}	    			    		
	    		
	    
	    	
	    }
	    
	    
	   //만약 DB상에 이미 날짜가 존재하고 있지 않으면 학식 정보를 insert한다.
	   if(!mongoCrawlingDAO.checkExistMealInfo(restaurantName, meals.get(0).getDate()))	{
	    	
	    	mongoCrawlingDAO.InsertMealInfo(meals); 
	    	
	   }
	    	
	    
		
		
	}

	
	
	
	

}
