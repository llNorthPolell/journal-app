package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.JournalEntry;

import java.util.List;
import java.util.UUID;

public interface JournalEntryRecordService {
    List<FlatRecord> save(JournalEntry payload);

    List<FlatRecord> getDashboardData(UUID journalRef);
}
