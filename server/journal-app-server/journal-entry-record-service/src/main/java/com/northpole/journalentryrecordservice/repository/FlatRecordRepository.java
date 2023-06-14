package com.northpole.journalentryrecordservice.repository;

import com.northpole.common.entity.FlatRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface FlatRecordRepository extends MongoRepository<FlatRecord, UUID> {
}
