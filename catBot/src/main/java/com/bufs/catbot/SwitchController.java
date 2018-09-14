package com.bufs.catbot;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SwitchController {
	
	@Autowired
	private MongoService mongoService;
	
	private static final Logger logger = LoggerFactory.getLogger(SwitchController.class);
	
	
	/**
	 *
	 *  요청을 분기시켜주는 Front Controller
	 *
	 */
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		logger.info("Front Contoller 진입, MongoDB connection test...");
		mongoService.getAnyway();
		
		return "home";
	}
	
	
	@ResponseBody
	@RequestMapping(value="/keyboard", method=RequestMethod.GET)
	public Map<String, Object> keyboard() {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", "buttons");
		param.put("buttons", mongoService.getAnyway().getValue());
		
		
		return param;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/message", method= {RequestMethod.GET, RequestMethod.POST, }, headers="Accept=application/json")
	public Map<String, Map<String, Object>> message(@RequestBody Map<String, Object> reqParam) throws Exception {
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		
		Map<String, Object> param = new HashMap<String, Object>();

		String keyParam = (String)(reqParam.get("content"));
		
		try {
		logger.info("들어온 원본.. " + reqParam);
		logger.info("들어온 파라미터.. " + keyParam);

		
		param.put("type", "buttons");
		param.put("buttons", mongoService.getCatAnswer(keyParam).getValue());
		logger.info("결과값.. " + mongoService.getCatAnswer(keyParam).toString());
		logger.info("결과값 Array.. " + mongoService.getCatAnswer(keyParam).getValue());

		}catch (Exception e) {
			
			
			param.put("type", "buttons");
			param.put("buttons", mongoService.getCatAnswer("냥냥봇").getValue());
			logger.info("결과값 Error.. " + mongoService.getCatAnswer("냥냥봇").getValue());

			
		}
		
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("text", keyParam + "이라냥");
		
		result.put("message", message);
		result.put("keyboard", param);
		return result;
	}
	
	
}
