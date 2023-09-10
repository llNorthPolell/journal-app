package com.northpole.journalappserver.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.northpole.journalappserver.entity.JournalEntry;

import java.util.List;
import java.util.UUID;


public interface JournalEntryService {

    JournalEntry publishJournalEntry(UUID journalRef, JournalEntry payload) throws JsonProcessingException;

    JournalEntry getJournalEntryById(UUID journalEntryId);

    List<JournalEntry> getJournalEntriesInJournal(UUID journalRef);

    JournalEntry getLastEntryInJournal(UUID journalRef);

    JournalEntry updateJournalEntry(UUID journalEntryId, JournalEntry payload) throws JsonProcessingException;

    JournalEntry deleteJournalEntry(UUID journalEntryId);

    boolean ownsJournalEntry(UUID journalRef, UUID journalEntryId);
}
