package com.bufs.catbot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;

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
	
	
	public Map<String, Map<String, Object>> findBookGuide(){
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		Map<String, Object> message = new HashMap<String, Object>();
		
		message.put("text", "찾을 책의 키워드를 입력하라냥. 규칙을 알려주겠다냥. #도서# 라고 앞에 붙여야 검색이 된다냥. \n"
				+ "예를 들면 #도서#냥냥, #도서#소나기 이런 식으로 검색을 하라냥. \n 검색 옵션은 없다냥. 페이지로 들어가서 상세 검색을 하라냥. ");
		
		result.put("message", message);
		
		return result;
		
		
		
	}
	
	
	public  Map<String, Map<String, Object>> findPerson(String key){
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		Map<String, Object> message = new HashMap<String, Object>();
		StringBuffer resultScript = new StringBuffer();

		
		String rawStr = key.replace("#전화#", "");
		
		if(rawStr.trim().length() < 2) {
			
			resultScript.append("2글자 이상 입력하라냥.");
			message.put("text", resultScript.toString());
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
		resultScript
				.append("이름 : ").append(people.get이름()).append("\n")
				.append("부서 : ").append(people.get부서()).append("\n")
				.append("전화번호 : ").append(people.get전화번호()).append("\n")
				.append("위치 : ").append(people.get위치()).append("\n")
				.append("----------------------------------\n");

		}
		
		if(resultScript.toString().trim().length()<1) {
			
			resultScript.append("검색 결과가 없다냥");
			
		}
		
		message.put("text", resultScript.toString());
		result.put("message", message);

		return result;
		
	}

	
	public  Map<String, Map<String, Object>> findBook(String key){
		
		key = key.replace("#도서#", "");

		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		Map<String, Object> message = new HashMap<String, Object>();
		StringBuffer resultUrl = new StringBuffer();
		
		if(key.trim().length()<1) {
			
			resultUrl.append("한 글자이상 입력하라냥");
		}else {
		
		resultUrl.append("https://library.bufs.ac.kr/#/search/si?all=").append(UriEncoder.encode("1|k|a|")).append(UriEncoder.encode(key));
		
		}
		
		message.put("text", resultUrl.toString());
		result.put("message", message);	
	
	
		return result;
	
}
	
	
}
	
		

