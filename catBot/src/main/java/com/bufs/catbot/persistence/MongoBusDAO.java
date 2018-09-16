package com.bufs.catbot.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MongoBusDAO {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<String> getShuttleBusInfo(String nowTime){
		
		
		 Criteria criteria = new Criteria("시간");
		 criteria.regex("/^"+nowTime.substring(0, 1)+"/");
		 Query query = new Query(criteria);
		 query.addCriteria(new Criteria().andOperator(Criteria.where("타입").is("셔틀"), Criteria.where("isWeekend")).is("FALSE"));
		 
		 
		 List<String> shuttles = (List) mongoTemplate.find(query, List.class, "catBus");
		 
		 

		 
		
		return shuttles;
		
	}
	

}
