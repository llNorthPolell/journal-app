package com.northpole.journalentrypersist.service;

import com.northpole.common.entity.GeneralResponseBody;
import com.northpole.common.entity.JournalEntry;

public interface JournalEntryPersistService {

    public GeneralResponseBody save(JournalEntry payload);

}
