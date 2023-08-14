package com.northpole.journalappserver.service;


import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.JournalEntry;


public interface JournalEntryService {

    public GeneralResponseBody save(JournalEntry payload);
}
