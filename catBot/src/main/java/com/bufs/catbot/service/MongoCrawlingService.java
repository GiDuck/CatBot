package com.bufs.catbot.service;

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Service;


@Service
public class MongoCrawlingService {
	
	//usr/bin/google-chrome
	private WebDriver driver;
	public static final String DRIVER_PATH = "/usr/bin/chromedriver";
	public static final String DRIVER_NAME = "webdriver.chrome.driver";
	public static final String MEAL_TABLE_URL = "http://app.bufs.ac.kr/food.aspx";

	
	public void CrawlMealTable() {
		
		ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
		

		System.setProperty(DRIVER_PATH, DRIVER_NAME);
		driver = new ChromeDriver(chromeOptions);
		
		driver.get(MEAL_TABLE_URL);
		WebElement element;
		
		try {
		
		//교직원식당 학식 정보 가져오기

		
		new Select(driver.findElement(By.id("ddl식당"))).selectByVisibleText("교직원 식당"); 
	    
	    Thread.sleep(1000);
	    driver.findElement(By.cssSelector("option[value=\"25\"]")).click();
	      
	    
	    element = driver.findElement(By.id("lblTitle"));
	    String text = element.getText();
	    System.out.println(text);      
	    
	    element = driver.findElement(By.className("tbl-type01"));
	    
	    List<WebElement> elements = element.findElements(ByTagName.tagName("tbody"));
	    Iterator<WebElement> itr = elements.iterator();
	    
	    System.out.println("테스팅 시작.....");
	    
	    for(;itr.hasNext();) {
	    	
	    	WebElement line = itr.next();
	    	
	    	if(line.isDisplayed()) {
	    		
	    		System.out.println(line.getText());
	    		
	    		
	    	}
	    	
	    	
	    }
	    
	    
	    text = element.getText();
	    System.out.println(text);      
	    
	   
	    
	    //학생식당K 학식 정보 가져오기

	    
	    new Select(driver.findElement(By.id("ddl식당"))).selectByVisibleText("학생식당 K");
	    
	    
	    Thread.sleep(1000); //시간 500밀리세컨드 동안 슬립
	    
	    element = driver.findElement(By.id("lblTitle"));
	    text = element.getText();
	    System.out.println(text); 
	    
	    element = driver.findElement(By.className("tbl-type01"));
	    text = element.getText();
	    System.out.println(text);    
	    
	    
	    
	    //학생식당O 학식 정보 가져오기
	    
	    
	    driver.findElement(By.cssSelector("option[value=\"30\"]")).click();
	    new Select(driver.findElement(By.id("ddl식당"))).selectByVisibleText("학생식당 O");
	   
	  
	    Thread.sleep(1000);
	    
	    element = driver.findElement(By.id("lblTitle"));
	    text = element.getText();
	    System.out.println(text);     
	    
	    element = driver.findElement(By.className("tbl-type01"));
	    text = element.getText();
	    System.out.println(text);    
	    
	    driver.findElement(By.cssSelector("option[value=\"31\"]")).click();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	

	
	

}
