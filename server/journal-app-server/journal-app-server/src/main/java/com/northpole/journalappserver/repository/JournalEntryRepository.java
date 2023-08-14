package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, UUID> {

}
