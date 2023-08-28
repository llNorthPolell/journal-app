package com.northpole.journalappserver.service;


import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.JournalEntry;

import java.util.List;


public interface JournalEntryService {

    GeneralResponseBody save(JournalEntry payload);

    List<JournalEntry> getJournalEntriesById(int journalId);

    JournalEntry getLastEntryInJournal(int journalId);
}
