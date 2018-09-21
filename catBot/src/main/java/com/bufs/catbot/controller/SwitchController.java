package com.bufs.catbot.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bufs.catbot.service.MongoBusService;
import com.bufs.catbot.service.MongoCrawlingService;
import com.bufs.catbot.service.MongoFindService;
import com.bufs.catbot.service.MongoService;

/**
 * Handles requests for the application home page.
 */
@SuppressWarnings("unchecked")
@Controller
public class SwitchController {
	
	@Autowired
	private MongoService mongoService;
	
	@Autowired
	private MongoFindService mongoFindService;
	
	@Autowired
	private MongoBusService mongoBusService;
	
	
	
	@Autowired
	private MongoCrawlingService mongoCrawlingService;
	
	private static final Logger logger = LoggerFactory.getLogger(SwitchController.class);
	
	
	/**
	 *
	 *  요청을 분기시켜주는 Front Controller
	 *
	 */
	

	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		logger.info("Front Contoller 진입, MongoDB connection test...");
		return "home";
	}
	
	
	

	//맨 처음에 키보드가 init 되는 요청
	@ResponseBody
	@RequestMapping(value="/keyboard", method=RequestMethod.GET)
	public Map<String, Object> keyboard() {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", "buttons");
		param.put("buttons", mongoService.getDefaultList());
		return param;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/message", method= {RequestMethod.GET, RequestMethod.POST}, headers="Accept=application/json")
	public Map<String, Map<String, Object>> message(@RequestBody Map<String, Object> reqParam) throws Exception {
		
		
		Map<String, Map<String, Object>> message = new HashMap<String, Map<String, Object>>();
		
		String keyParam = (String)(reqParam.get("content"));
		String user_key = (String)(reqParam.get("user_key"));
				
		
		
		if(keyParam.contains("전화번호검색")) {
			
			message = mongoFindService.findGuide();
		}
		else if(keyParam.contains("#전화#")) {
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			
			Map<String, Object> tempMap = mongoFindService.findPerson(keyParam).get("message");
			message.put("message", tempMap);
			
		}else if(keyParam.contains("도서검색")) {
			
			message = mongoFindService.findBookGuide();
			
		}
		
		else if(keyParam.contains(("#도서#"))) {
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			
			Map<String, Object> tempMap = mongoFindService.findBook(keyParam).get("message");
			message.put("message", tempMap);
			
		}
		else if(keyParam.equals("냥냥봇에게 말하기")) {
			
			message = mongoService.catRequestForm();

		}else if(keyParam.contains("#냥냥#")) {
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			Map<String, Object> tempMap = mongoService.catRequestSomething(keyParam, user_key).get("message");
			message.put("message", tempMap);
		}		
		else if(keyParam.contains("현재버스(셔틀)")) {
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			message.put("message", mongoBusService.getBusInfo("셔틀", null, null));
			
		}else if(keyParam.contains("전체시간표(셔틀)")){
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			message.put("message", mongoBusService.getAllBusInfo("셔틀", null, null));
			
		}else if(keyParam.contains("(마버)구서역-외대")){
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			message.put("message", mongoBusService.getBusInfo("마버", "구서", "외대"));
			
		}else if(keyParam.contains("(마버)외대-구서역")){
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			message.put("message", mongoBusService.getBusInfo("마버", "외대", "구서"));
			
		}else if(keyParam.contains("(마버)외대-남산역")){
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			message.put("message", mongoBusService.getBusInfo("마버", "외대", "남산"));
			
		}else if(keyParam.contains("(마버T)구서역-외대")){
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			message.put("message", mongoBusService.getAllBusInfo("마버", "구서", "외대"));
			
		}else if(keyParam.contains("(마버T)외대-구서역")){
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			message.put("message", mongoBusService.getAllBusInfo("마버", "외대", "구서"));
			
		}else if(keyParam.contains("(마버T)외대-남산역")){
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			message.put("message", mongoBusService.getAllBusInfo("마버", "외대", "남산"));
			
		}else if(keyParam.contains("기숙사식당")) {
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			message.put("message", mongoCrawlingService.getTodayMenu("기숙사식당"));
		}else if(keyParam.contains("교직원식당")) {
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			message.put("message", mongoCrawlingService.getTodayMenu("교직원식당"));
		}
		
		else if(keyParam.contains("학생식당K")) {
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			message.put("message", mongoCrawlingService.getTodayMenu("학생식당K"));
		
		}else if(keyParam.contains("학생식당O")) {
			
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			message.put("message", mongoCrawlingService.getTodayMenu("학생식당O"));
		}		
		else {
			message =  mongoService.getCatAnswer(keyParam, user_key);
		}

		
		
		return message;
	}
	
	
	
	//매주 월,화,수 오후 12시에 학식 페이지에서 메뉴를 들고와 DB에 넣어주는 스케쥴러
	@Scheduled(cron = "0 0 12 ? ? 1-3")
	public void CrawlMealInfo() {
			
		mongoCrawlingService.CrawlMealTable();

		
	}
	
	
}
