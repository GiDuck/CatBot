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
	
	private WebDriver driver;
	public static final String DRIVER_PATH = "/usr/bin/chromedriver";
	public static final String DRIVER_NAME = "webdriver.chrome.driver";
	public static final String MEAL_TABLE_URL = "http://app.bufs.ac.kr/food.aspx";

	
	public void CrawlMealTable() {
		
		System.out.println("logging point check 1");
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
	
	
	public Map<String, Object> getTodayMenu(String token) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		Date today =new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nowDateStr = dateFormat.format(today);
		
		List<MongoMealDTO> meals = mongoCrawlingDAO.getMealInfo(token, nowDateStr);
		
		if(meals == null || meals.size()== 0)
		{
			result.put("text", "학식 정보가 없다냥");
			
			return result;
			
		}
		
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
	
	public void BatchInsert(WebElement element) {
		
		
		element = driver.findElement(By.id("lblTitle"));
		String restaurantName = element.getText().replace("[","").replace("]", "");
		restaurantName = restaurantName.replace(" ", "");
		List<MongoMealDTO> meals = new ArrayList<>();
		
		
	    element = driver.findElement(By.className("tbl-type01")).findElement(ByTagName.tagName("tbody"));
	    List<WebElement> elements = element.findElements(ByTagName.tagName("tr"));
	    Iterator<WebElement> itr = elements.iterator();	    
	     
	    
	    for(;itr.hasNext();) {
	    	
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
	    
	    
	    
	   if(!mongoCrawlingDAO.checkExistMealInfo(restaurantName, meals.get(0).getDate()))	{
	    	
	    	mongoCrawlingDAO.InsertMealInfo(meals); 
	    	
	   }
	    	
	    
		
		
	}

	
	
	
	

}
