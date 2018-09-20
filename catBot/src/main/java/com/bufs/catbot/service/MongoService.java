package com.bufs.catbot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bufs.catbot.domain.HistorySession;
import com.bufs.catbot.domain.MongoDTO;
import com.bufs.catbot.persistence.MongoDAO;

@Service
public class MongoService {

	
	@Autowired
	private MongoDAO mongoDAO;
	
	
	
	//Catbot 기본 대답
	public Map<String, Map<String, Object>> getCatAnswer(String key, String user_key) {
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> message = new HashMap<String, Object>();

	
		if(key.equals("뒤로가기")) {
			key = popHistory(user_key);
		}
		

		MongoDTO catAnswer = mongoDAO.getCatAnswer(key);
			
		if(catAnswer == null) {
			
			catAnswer = mongoDAO.getCatAnswer("냥냥봇");
			
		}

		//만약 단말노드이면 message만 출력
		if(catAnswer.getIsTerminal()) {
			
			param.put("type", "buttons");
			param.put("buttons", getDefaultList());

			String script = "";
			Map<String, String> terminalMap = (Map<String, String>)catAnswer.getValue();
			
			Iterator<String> itr = terminalMap.keySet().iterator();
			
			while(itr.hasNext()) {
				
				String itrKey = itr.next();
				
				script += itrKey + ":" + terminalMap.get(itrKey) + "\n";
				
				script = script.replace("null", "");
				
			}
			
			message.put("text", script);
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

		}else {
		
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
	
	
	
}
