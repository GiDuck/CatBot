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

/*
 * 
 * 사용자가 선택한 버튼의 정보를 요청 - 응답 트리에서 가져오는 서비스
 * 
 * */


@Service
public class MongoService {

	
	@Autowired
	private MongoDAO mongoDAO;
	
	
	
	//Catbot 기본 대답
	public Map<String, Map<String, Object>> getCatAnswer(String key, String user_key) {
		
		//사용자가 클릭한 버튼이 뒤로가기 버튼인지 판단한 것을 저장하는 변수
		boolean isBackTacking = false;
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> message = new HashMap<String, Object>();

	
		//만약 뒤로가기 버튼이면 사용자의 바로 이전에 선택했던 버튼을 pop 시킨다.
		if(key.equals("뒤로가기")) {
			key = popHistory(user_key);
			isBackTacking = true;	
		}
		
		

		//키에 해당하는 value값 가져오기
		MongoDTO catAnswer = mongoDAO.getCatAnswer(key);
		
		//만약 가져온 데이터가 없으면 초기 키보드 value를 전달한다.
		if(catAnswer == null) {
			
			catAnswer = mongoDAO.getCatAnswer("냥냥봇");
			
		}

		//만약 단말노드이면 message만 출력
		//단말 노드이면 value가 Object로 구성이 되어있다.
		if(catAnswer.getIsTerminal()) {
			
			param.put("type", "buttons");
			param.put("buttons", getDefaultList());

			StringBuffer script = new StringBuffer();
			Map<String, String> terminalMap = (Map<String, String>)catAnswer.getValue();
			
			
			//Object를 Map 형식으로 변환하여 iterator로 출력
			Iterator<String> itr = terminalMap.keySet().iterator();
			
			while(itr.hasNext()) {
				
				String itrKey = itr.next();
				script.append(itrKey).append(" : ").append(terminalMap.get(itrKey)).append("\n");
				
			}
			
			//null이라고 출력된 데이터는 공백으로 처리한다.
			message.put("text", script.toString().replace("null", ""));
			result.put("message", message);
			result.put("keyboard", param);
			
			
			//단말 노드이후에는 다시 기본 키보드값으로 초기화 되어야 하므로 뒤로가기 목록 삭제
			mongoDAO.removeAllHistory(user_key);
			
			return result;


		//단말 노드가 아니라면	
		}else {
			
			List<String> buttons = (List<String>)catAnswer.getValue();
			
			//만약 제일 상위 노드가 아니라면 뒤로가기 버튼을 클릭한다. (제일 상위노드는 뒤로가기가 없다)
			if(catAnswer.getLevel() > 1)
			buttons.add("뒤로가기");
			
			try {
				
				param.put("type", "buttons");
				param.put("buttons", buttons);
				message.put("text", key + "이라냥");


				
			//예외 발생 시 기본 키보드 값으로 전달
			}catch (Exception e) {
					
					e.printStackTrace();
					param.put("type", "buttons");
					param.put("buttons", getDefaultList());
					message.put("text", "무슨 말을 하는지 모르겠다냥");


			}

			result.put("message", message);
			result.put("keyboard", param);
			
		}

		//만약 현재 가져온 노드의 레벨이 제일 상위 노드이면, 뒤로가기 목록을 삭제한다.
		if(catAnswer.getLevel() < 2) {
			
			mongoDAO.removeAllHistory(user_key);

		}
		
		
		
		//만약에 지금 들어온 키값이 뒤로가기 버튼이 아니라면 현재 버튼을 뒤로가기 목록에 추가
			if(!isBackTacking) {
				
			HistorySession history = new HistorySession();
			history.setName(catAnswer.getName());
			history.setLevel(catAnswer.getLevel());
			history.setTime(new Date());
			history.setUser_id(user_key);		
			putHistory(history);
			
			}

		return result;
	
	}
	
	
	//제일 처음에 클라이언트의 채팅방에 나오는 키보드 목록
	public List<String> getDefaultList() {
		
		List<String> message = new ArrayList<String>();
		message = (List<String>)mongoDAO.getCatAnswer("냥냥봇").getValue();
	
		return message;

		
	}
	
	//뒤로가기 목록 삽입
	public void putHistory(HistorySession history) {
		
		mongoDAO.putHistory(history);
		
	}
	
	//뒤로 가기 목록에서 제일 최근에 실행한 요소 꺼내기
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
		
		
		//만약 최근에 실행한 노드의 레벨이 2 이하이면, 키보드를 초기화 시키고 모든 뒤로가기 목록 삭제.
		if(peekHistory.getLevel() < 3) {

			result="냥냥봇";
			removeAllHistory(user_key);
			
		}else {
		
		//히스토리 목록이 2개 이상이면 peek 앞의 요소의 이름을 불러옴
		result = history.get(history.size()-2).getName();
		
		}
		
		//top에 있는 히스토리 목록을 삭제
		mongoDAO.removeOneHistory(peekHistory);
		history.remove(history.size()-1);	
		
		}catch(Exception e) {
			
			e.printStackTrace();
			result="냥냥봇";

		}
		
		return result;
		
		
	}
	
	
	//모든 히스토리 목록 삭제
	public void removeAllHistory(String user_key) {
		
	
		mongoDAO.removeAllHistory(user_key);
		
	}
	
	//히스토리 pop
	public void removePopHistory(HistorySession history) {
		
		
		mongoDAO.removeOneHistory(history);
		
	}
	
	
	
	//사용자 건의사항 form을 만들어 주는 메소드
	public Map<String, Map<String, Object>> catRequestForm(){
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		Map<String, Object> message = new HashMap<String, Object>();
		
		message.put("text", "반갑다냥. 내가 그동안 많이 알려줬겠지만 부족한 것이 있을꺼라고 생각한다냥.\n혹시 나에게 말하고 싶거나 건의하고 싶은게 있으면 얼마든지 말하라냥\n개인 정보는 저장되지 않으니 안심하라냥.\n\n"
				+ "규칙을 알려주겠다냥. #냥냥# 이라고 앞에 붙이고 할 말을 쓰라냥. \n"
				+ "예를 들면 #냥냥#안녕 냥냥봇, #냥냥#냥냥펀치 이런 식으로 쓰라냥. \n많은 의견을 기대하겠다냥. 고맙다냥. ");
		
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
	
	
	
	public Map<String, Map<String, Object>> catSay(){
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		Map<String, Object> message = new HashMap<String, Object>();

		String resultStr = mongoDAO.catSay();
		message.put("text", resultStr);
		result.put("message", message);	
	
	
		return result;
	
	
	
	}
	
	
	
	
	
}
