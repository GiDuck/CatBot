package com.bufs.catbot.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.bufs.catbot.domain.MongoMealDTO;

@Repository
public class MongoCrawlingDAO {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public void InsertMealInfo(List<MongoMealDTO> meals) {
		
		mongoTemplate.insert(meals, "catMeal");
		
	}
	
	
	public List<MongoMealDTO> getMealInfo(String token, String today) {
		
		
		Criteria criteria = new Criteria("restaurantType");
		criteria.is(token);
		Query query = new Query(criteria);
		query.addCriteria(new Criteria().andOperator(Criteria.where("date").is(today)));
		List<MongoMealDTO> mealTable = mongoTemplate.find(query, MongoMealDTO.class, "catMeal");
		
		return mealTable;
		
		
	}
	
	
	public void removeAllMealInfo() {
		
		
		mongoTemplate.remove("catMeal");
		
		
	}
	
	
	public Boolean checkExistMealInfo(String token, String date) {
		
		
		Criteria criteria = new Criteria("restaurantType");
		criteria.is(token);
		Query query = new Query(criteria);
		query.addCriteria(new Criteria().andOperator(Criteria.where("date").is(date)));
		List<MongoMealDTO> mealTable = mongoTemplate.find(query, MongoMealDTO.class, "catMeal");
		
		if(mealTable.size()!=0) {
			
			return true;
		}
		
		return false;
		
		
	}
	
	
}
