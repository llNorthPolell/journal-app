package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.Goal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GoalRepository extends MongoRepository<Goal, UUID> {
    @Query("{journal: ?0}")
    List<Goal> findAllByJournalId(int journalId);
}
