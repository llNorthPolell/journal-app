package com.northpole.journalservice.repository;

import com.northpole.journalservice.entity.Journal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface JournalRepository extends CrudRepository<Journal, Integer> {
    List<Journal> getJournalsByAuthor(@Param("author") String authorId);
}
