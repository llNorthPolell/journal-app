package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.JournalEntry;

import java.util.List;

public interface JournalEntryRecordService {
    GeneralResponseBody save(JournalEntry payload);

    List<FlatRecord> getDashboardData(int journalId);
}
