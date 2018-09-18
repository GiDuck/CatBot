package com.bufs.catbot.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.bufs.catbot.domain.MongoBusDTO;


@Repository
public class MongoBusDAO {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<String> getShuttleBusInfo(String nowTime){
				
		List<String> shuttles = new ArrayList<String>();
		

		
		 Pattern pattern = Pattern.compile("^"+Pattern.quote(nowTime.substring(0, 2)));

		 Criteria criteria = new Criteria("타입").is("셔틀").andOperator(
				 Criteria.where("시간").regex(pattern),
				 Criteria.where("isWeekend").is("FALSE"));
		 Query query = new Query(criteria);
		 query.fields().include("시간");
		 query.fields().exclude("_id");
		 query.with(new Sort(Sort.Direction.ASC, "시간"));


		 
		List<MongoBusDTO> shuttlesTimeTable = mongoTemplate.find(query, MongoBusDTO.class, "catBus");
		 
		for(MongoBusDTO dto : shuttlesTimeTable) {
			 shuttles.add(dto.get시간());
			 System.out.println("들어온 시간 : " + dto.get시간());
		}
	
		return shuttles;
		
	}
	
	
	
	public List<String> getAllShuttleBusInfo(){
	
		List<String> shuttles = new ArrayList<String>();
		
		 Criteria criteria = new Criteria("타입").is("셔틀").andOperator(
				 Criteria.where("isWeekend").is("FALSE"));
		 Query query = new Query(criteria);
		 query.fields().include("시간");
		 query.fields().exclude("_id");
		 query.with(new Sort(Sort.Direction.ASC, "시간"));
		 
		
			List<MongoBusDTO> shuttlesTimeTable = mongoTemplate.find(query, MongoBusDTO.class, "catBus");

			 
			for(MongoBusDTO dto : shuttlesTimeTable) {
				 shuttles.add(dto.get시간());
				 System.out.println("들어온 시간 : " + dto.get시간());
			}

	
			
			return shuttles;

	}
	
}
