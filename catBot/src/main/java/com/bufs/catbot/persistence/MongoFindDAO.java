package com.bufs.catbot.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.bufs.catbot.domain.MongoFindDTO;

@Repository
public class MongoFindDAO {

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<MongoFindDTO> findPeople(List<String> keys) {

		Query query = new Query();
		query.limit(10);

		for (String key : keys) {
			try {
				query.addCriteria(new Criteria().orOperator(Criteria.where("이름").regex(key),
						Criteria.where("부서").regex(key)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		List<MongoFindDTO> foundPersons = mongoTemplate.find(query, MongoFindDTO.class, "catPerson");
		return foundPersons;

	}

}
