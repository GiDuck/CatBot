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
	
	
	
	
	
	public List<String> getBusInfo(String nowTime, String type, String station, String bound, Boolean isWeekend, Boolean isSeasonalSem){
				
		List<String> busTable = new ArrayList<String>();
		String nowTimeToken = nowTime.substring(0, 2);
		int nowTimeH = Integer.parseInt(nowTimeToken);

		String afterH = nowTimeH + 1 < 10 ? "0" + (nowTimeH+1) : String.valueOf(nowTimeH +1);
		String beforeH = nowTimeH - 1 < 10 ? "0" + (nowTimeH-1) : String.valueOf(nowTimeH- 1);


		Criteria criteria= null;
		Query query = null;
		Pattern pattern = Pattern.compile(
				 "^" + Pattern.quote(nowTimeToken)+ "|" 
				 + Pattern.quote(afterH)+ "|" 
				 + Pattern.quote(beforeH));
		
		if(type.contains("셔틀")) {

		 criteria = new Criteria("타입").is("셔틀").andOperator(
				 Criteria.where("시간").regex(pattern),
				 Criteria.where("isWeekend").is(isSeasonalSem));
		 query = new Query(criteria);
		 query.fields().include("시간");
		 query.fields().exclude("_id");
		 query.with(new Sort(Sort.Direction.ASC, "시간"));

		}else if(type.contains("마을")) {
			
			
			 criteria = new Criteria("타입").is("마을").andOperator(
					 Criteria.where("시간").regex(pattern),
					 Criteria.where("station").is(station),
					 Criteria.where("bound").is(bound),
					 Criteria.where("isWeekend").is(String.valueOf(isWeekend).toUpperCase()));
			 query = new Query(criteria);
			 query.fields().include("시간");
			 query.fields().exclude("_id");
			 query.with(new Sort(Sort.Direction.ASC, "시간"));
			
			
			
			
		}

		 
		List<MongoBusDTO> shuttlesTimeTable = mongoTemplate.find(query, MongoBusDTO.class, "catBus");
		 
		for(MongoBusDTO dto : shuttlesTimeTable) {
			busTable.add(dto.get시간());
		}
	
		return busTable;
		
	}
	
	
	
	public List<String> getAllBusInfo(Boolean isShuttleBus, String station, String bound, Boolean isWeekend, Boolean isSeasonalSem){
	
		List<String> busTable = new ArrayList<String>();
		Criteria criteria= null;
		Query query = null;
		
		if(isShuttleBus) {

			 criteria = new Criteria("타입").is("셔틀").andOperator(
					 Criteria.where("isWeekend").is(String.valueOf(isSeasonalSem)));
			 query = new Query(criteria);
			 query.fields().include("시간");
			 query.fields().exclude("_id");
			 query.with(new Sort(Sort.Direction.ASC, "시간"));

		}else {
				
				 criteria = new Criteria("타입").is("마을").andOperator(
						 Criteria.where("station").is(station),
						 Criteria.where("bound").is(bound),
						 Criteria.where("isWeekend").is(String.valueOf(String.valueOf(isSeasonalSem)).toUpperCase()));
				 query = new Query(criteria);
				 query.fields().include("시간");
				 query.fields().exclude("_id");
				 query.with(new Sort(Sort.Direction.ASC, "시간"));
				
		}

		
			List<MongoBusDTO> pBusTable = mongoTemplate.find(query, MongoBusDTO.class, "catBus");

			 
			for(MongoBusDTO dto : pBusTable) {
				busTable.add(dto.get시간());
			}

	
			
			return busTable;

	}
	
}
