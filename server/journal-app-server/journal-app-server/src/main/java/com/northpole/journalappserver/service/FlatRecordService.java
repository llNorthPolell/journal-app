package com.northpole.journalappserver.service;


import com.northpole.journalappserver.entity.RecordKey;

import java.util.List;
import java.util.UUID;

public interface FlatRecordService {
    List<RecordKey> getRecordFieldsInJournal(UUID journalRef);
}
