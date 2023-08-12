package com.northpole.journalentryrecordservice.service;

import com.northpole.common.entity.FlatRecord;
import com.northpole.common.entity.GeneralResponseBody;
import com.northpole.common.entity.JournalEntry;
import com.northpole.journalentryrecordservice.entity.JournalEntryRecordDataSet;
import com.northpole.journalentryrecordservice.entity.JournalEntryRecordServiceInput;

import java.util.List;

public interface JournalEntryRecordService {
    GeneralResponseBody save(JournalEntry payload);

    List<FlatRecord> getRecords(JournalEntryRecordServiceInput payload);

    JournalEntryRecordDataSet getDataset(JournalEntryRecordServiceInput testInput);
}
