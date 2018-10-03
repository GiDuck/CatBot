package com.bufs.catbot.persistence;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.bufs.catbot.domain.CatRequestForm;
import com.bufs.catbot.domain.HistorySession;
import com.bufs.catbot.domain.MongoDTO;

@Repository
public class MongoDAO {

	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	public MongoDTO getDefaultMessage() {
		
	
		Criteria criteria = new Criteria("name").is("냥냥봇");
		
		Query query = new Query(criteria);
		MongoDTO result = mongoTemplate.findOne(query, MongoDTO.class, "catDb");
		System.out.println("get Result... " + result);
		
		return result;
		
		
	}
	
	
	
	public MongoDTO getCatAnswer(String key) {
		
		MongoDTO result = new MongoDTO();
		try {
		Criteria criteria = new Criteria("name").is(key);
		Query query = new Query(criteria);
		result = mongoTemplate.findOne(query, MongoDTO.class, "catDb");
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	
	}
	
	
	
	public void putHistory(HistorySession history) {
	
		try {
			
			mongoTemplate.insert(history, "catBackHistory");
						
			}catch(Exception e) {
				e.printStackTrace();
			}
		
		
		
	}
	
	public List<HistorySession> getHistory(String user_key){
		
		List<HistorySession> backs = null;
		try {
			Criteria criteria = new Criteria("user_id").is(user_key);
			Query query = new Query(criteria);
			query.with(new Sort(Sort.Direction.ASC, "time"));
			
			backs = mongoTemplate.find(query, HistorySession.class, "catBackHistory");
						
			}catch(Exception e) {
				e.printStackTrace();
			}
		
		return backs;

	}
	
	public void removeAllHistory(String user_key) {
		
		
		try {
			Criteria criteria = new Criteria("user_id").is(user_key);
			Query query = new Query(criteria);
			mongoTemplate.remove(query, "catBackHistory");		
			
		}catch(Exception e) {
				e.printStackTrace();
			}
		
	}
	
	public void removeOneHistory(HistorySession history) {
		
		
		try {

			mongoTemplate.remove(history, "catBackHistory");		
			
		}catch(Exception e) {
				e.printStackTrace();
			}
		
	}
	
	
	public void insertCatRequest(CatRequestForm form) {
		
		
		mongoTemplate.insert(form, "catDialogue");
		
		
	}
	
	
	public void insertHolidayInfo(List<Map<String, String>> items) {
		
		
		
		for(Map<String, String> item : items) {
			
		mongoTemplate.insert(item, "catHoliday");
		
		}
		
	}
	
	
	public boolean findHoliday(String date) {
		
		
		Criteria criteria = new Criteria("date");
		criteria.is(date);
		Query query = new Query(criteria);
		boolean isExsist = mongoTemplate.exists(query, "catHoliday");
		
		return isExsist;
		
	}
	
	public boolean findUniversityHoliday(String date) {
		
		
		Criteria criteria = new Criteria("date").is(date);
		Query query = new Query(criteria.orOperator(new Criteria("type").is("학교휴일")));
		boolean isExsist = mongoTemplate.exists(query, "catHoliday");
		
		return isExsist;
		
	}
	
	
	
	
	
	
}
