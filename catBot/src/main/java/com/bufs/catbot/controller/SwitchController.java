package com.bufs.catbot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	
	


	@ResponseBody
	@RequestMapping(value="/keyboard", method=RequestMethod.GET)
	public Map<String, Object> keyboard() {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", "buttons");
		param.put("buttons", (List<String>)mongoService.getAnyway().getValue());
		
		
		
		return param;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/message", method= {RequestMethod.GET, RequestMethod.POST}, headers="Accept=application/json")
	public Map<String, Map<String, Object>> message(@RequestBody Map<String, Object> reqParam) throws Exception {
		
		
		Map<String, Map<String, Object>> message = new HashMap<>();
		
		String keyParam = (String)(reqParam.get("content"));
		String user_key = (String)(reqParam.get("user_key"));
		
		logger.info(keyParam + " " + user_key);
		
		
		if(keyParam.contains("전화번호검색")) {
			message = mongoFindService.findGuide();
		}
		else if(keyParam.contains(("#전화#"))) {
			logger.info("#전화# : " + keyParam);
			message = mongoService.getCatAnswer("냥냥봇", user_key);
			
			Map<String, Object> tempMap = mongoFindService.findPerson(keyParam).get("message");
			logger.info("tempMap : " + tempMap);
 
			message.put("message", tempMap);
			
		}
		else {
			message =  mongoService.getCatAnswer(keyParam, user_key);
		}
		
		
		logger.info("최종 응답 메시지 : " + message);
		
		
		
		return message;
	}
	
	
}
