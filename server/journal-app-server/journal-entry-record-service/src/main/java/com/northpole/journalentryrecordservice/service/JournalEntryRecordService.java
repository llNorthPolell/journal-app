package com.northpole.journalentryrecordservice.service;

import com.northpole.common.entity.GeneralResponseBody;
import com.northpole.common.entity.JournalEntry;

public interface JournalEntryRecordService {
    public GeneralResponseBody save(JournalEntry payload);
}
