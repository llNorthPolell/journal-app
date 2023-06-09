package com.northpole.journalentrypersist.repository;

import com.northpole.common.entity.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, UUID> {

}
