package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.Goal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GoalRepository extends MongoRepository<Goal, Integer> {

}
