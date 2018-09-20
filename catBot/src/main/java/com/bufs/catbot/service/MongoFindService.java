package com.bufs.catbot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bufs.catbot.domain.MongoFindDTO;
import com.bufs.catbot.persistence.MongoFindDAO;

@Service
public class MongoFindService {

	

	
	@Autowired
	private MongoFindDAO findDAO;
	
	public Map<String, Map<String, Object>> findGuide(){
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		Map<String, Object> message = new HashMap<String, Object>();
		
		message.put("text", "찾을 사람의 이름 아니면 부서를 입력하라냥. 규칙을 알려주겠다냥. #전화# 라고 앞에 붙여야 검색이 된다냥. \n"
				+ "예를 들면 #전화#냥냥, #전화#러시아 이런 식으로 검색을 하라냥. \n 두 글자이상 적어야 한다냥. 명심하라냥. 아니면 안 찾아줄꺼라냥. ");
		
		result.put("message", message);
		
		return result;
		
		
		
	}
	
	
	public  Map<String, Map<String, Object>> findPerson(String key){
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		Map<String, Object> message = new HashMap<String, Object>();
		String resultScript = "";

		
		String rawStr = key.replace("#전화#", "");
		
		if(rawStr.trim().length() < 2) {
			
			resultScript = "2글자 이상 입력하라냥.";
			message.put("text", resultScript);
			result.put("message", message);

			return result;

			
		}
		
		String[] temp = rawStr.split(" ");
		List<String> keywords = new ArrayList<String>();
		
		for(String str : temp) {
			
			if(str.trim().length()>0)
				keywords.add(str);
		}
		
		
		System.out.println("keywords : " + keywords);
		
		List<MongoFindDTO> peoples = findDAO.findPeople(keywords);

		for(MongoFindDTO people : peoples) {
		resultScript += 
				"이름 : " + people.get이름() + "\n" +
				"부서 : " + people.get부서() + "\n" +
				"전화번호 : " + people.get전화번호() + "\n" +
				"위치 : " + people.get위치() + "\n" +
				"----------------------------------\n";

		}
		
		if(resultScript.trim().length()<1) {
			
			resultScript = "검색 결과가 없다냥";
			
		}
		
		message.put("text", resultScript);
		result.put("message", message);

		return result;
		
	}
	
	
	
}
