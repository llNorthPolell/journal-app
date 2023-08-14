package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.JournalEntry;
import com.northpole.journalappserver.entity.JournalEntryRecordDataSet;
import com.northpole.journalappserver.entity.JournalEntryRecordServiceInput;

import java.util.List;

public interface JournalEntryRecordService {
    GeneralResponseBody save(JournalEntry payload);

    List<FlatRecord> getRecords(JournalEntryRecordServiceInput payload);

    JournalEntryRecordDataSet getDataset(JournalEntryRecordServiceInput testInput);
}
