package com.bufs.catbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoService {

	
	@Autowired
	private MongoPersistence mongoDAO;
	
	public MongoDTO getAnyway() {
	
		return mongoDAO.getAnyway();
	
	}
	
	
	public MongoDTO getCatAnswer(String key) {
		
		return mongoDAO.getCatAnswer(key);
	
	}
	
	
	
}
