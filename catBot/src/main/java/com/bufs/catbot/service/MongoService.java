package com.bufs.catbot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bufs.catbot.domain.CatRequestForm;
import com.bufs.catbot.domain.HistorySession;
import com.bufs.catbot.domain.MongoDTO;
import com.bufs.catbot.persistence.MongoDAO;

@Service
public class MongoService {

	
	@Autowired
	private MongoDAO mongoDAO;
	
	
	
	//Catbot 기본 대답
	public Map<String, Map<String, Object>> getCatAnswer(String key, String user_key) {
		
		boolean isBackTacking = false;
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> message = new HashMap<String, Object>();

	
		if(key.equals("뒤로가기")) {
			key = popHistory(user_key);
			isBackTacking = true;	
		}
		
		

		MongoDTO catAnswer = mongoDAO.getCatAnswer(key);
			
		if(catAnswer == null) {
			
			catAnswer = mongoDAO.getCatAnswer("냥냥봇");
			
		}

		//만약 단말노드이면 message만 출력
		if(catAnswer.getIsTerminal()) {
			
			param.put("type", "buttons");
			param.put("buttons", getDefaultList());

			StringBuffer script = new StringBuffer();
			Map<String, String> terminalMap = (Map<String, String>)catAnswer.getValue();
			
			Iterator<String> itr = terminalMap.keySet().iterator();
			
			while(itr.hasNext()) {
				
				String itrKey = itr.next();
				script.append(itrKey).append(" : ").append(terminalMap.get(itrKey)).append("\n");
				
			}
			
			message.put("text", script.toString().replace("null", ""));
			result.put("message", message);
			result.put("keyboard", param);
			
			
			mongoDAO.removeAllHistory(user_key);
			
			return result;


			
		}else {
			
			List<String> buttons = (List<String>)catAnswer.getValue();
			
			if(catAnswer.getLevel() > 1)
			buttons.add("뒤로가기");
			
			
			try {
				
				param.put("type", "buttons");
				param.put("buttons", buttons);
				message.put("text", key + "이라냥");


				
			}catch (Exception e) {
					
					e.printStackTrace();
					param.put("type", "buttons");
					param.put("buttons", getDefaultList());
					message.put("text", "무슨 말을 하는지 모르겠다냥");


			}
				

			result.put("message", message);
			result.put("keyboard", param);
			
			
		}
						
		if(catAnswer.getLevel() < 2) {
			
			mongoDAO.removeAllHistory(user_key);

		}
		
		
			
			if(!isBackTacking) {
				
			HistorySession history = new HistorySession();
			history.setName(catAnswer.getName());
			history.setLevel(catAnswer.getLevel());
			history.setTime(new Date());
			history.setUser_id(user_key);		
			putHistory(history);
			
			}
		
		
		System.out.println("나가는 결과,,,");
		System.out.println(result);
	
		return result;
	
	}
	
	
	public List<String> getDefaultList() {
		
		List<String> message = new ArrayList<String>();
		message = (List<String>)mongoDAO.getCatAnswer("냥냥봇").getValue();
	
		return message;

		
	}
	
	public void putHistory(HistorySession history) {
		
		
		mongoDAO.putHistory(history);
	
		
		
	}
	
	
	public String popHistory(String user_key){
		
		String result = null;
		HistorySession peekHistory = null;
		
		//히스토리 리스트를 가져온다.
		List<HistorySession> history = mongoDAO.getHistory(user_key);
		
		try {
		
		//히스토리 목록이 1개 이상이면 제일 마지막에 있는 요소를 가져옴
		if(history.size() > 0) {
			
		peekHistory = history.get(history.size()-1);
		
		}		
		
		
		if(peekHistory.getLevel() < 3) {

			result="냥냥봇";
			removeAllHistory(user_key);
			
		}else {
		
		//히스토리 목록이 2개 이상이면 peek 앞의 요소의 이름을 불러옴
		result = history.get(history.size()-2).getName();
		
		}
		
		
		mongoDAO.removeOneHistory(peekHistory);
		history.remove(history.size()-1);	
		
		}catch(Exception e) {
			
			e.printStackTrace();
			result="냥냥봇";

		}
		
		return result;
		
		
	}
	
	
	public void removeAllHistory(String user_key) {
		
	
		mongoDAO.removeAllHistory(user_key);
		
	}
	
	public void removePopHistory(HistorySession history) {
		
		
		mongoDAO.removeOneHistory(history);
		
	}
	
	
	
	public Map<String, Map<String, Object>> catRequestForm(){
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		Map<String, Object> message = new HashMap<String, Object>();
		
		message.put("text", "반갑다냥. 내가 그동안 많이 알려줬겠지만 부족한 것이 있을꺼라고 생각한다냥.\n 혹시 나에게 말하고 싶거나 건의하고 싶은게 있으면 얼마든지 말하라냥\n 개인 정보는 저장되지 않으니 안심하라냥.\n"
				+ "규칙을 알려주겠다냥. #냥냥# 이라고 앞에 붙이고 할 말을 쓰라냥. \n"
				+ "예를 들면 #냥냥#안녕 냥냥봇, #냥냥#냥냥펀치 이런 식으로 쓰라냥. \n 많은 의견을 기대하겠다냥. 고맙다냥. ");
		
		result.put("message", message);
		
		return result;
		
		
		
	}
	
	
	public  Map<String, Map<String, Object>> catRequestSomething(String key, String user_key){
		
		key = key.replace("#냥냥#", "");

		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		Map<String, Object> message = new HashMap<String, Object>();
		String resultStr = "";
		
		if(key.trim().length()<1) {
			resultStr="한 글자이상 입력하라냥";
		}else {
			
			CatRequestForm form = new CatRequestForm();
			form.setUser_key(user_key);
			form.setContent(key);
			form.setDate(new Date().toString());
			
			mongoDAO.insertCatRequest(form);
			
			resultStr ="냥냥!";
		}
		
		
		message.put("text", resultStr);
		result.put("message", message);	
	
	
		return result;
	
	
	
	}
	
	
	
	
}
