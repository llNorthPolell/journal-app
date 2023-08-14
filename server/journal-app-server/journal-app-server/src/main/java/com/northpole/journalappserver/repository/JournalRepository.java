package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.Journal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface JournalRepository extends CrudRepository<Journal, Integer> {
    List<Journal> getJournalsByAuthor(@Param("author") String authorId);
}
