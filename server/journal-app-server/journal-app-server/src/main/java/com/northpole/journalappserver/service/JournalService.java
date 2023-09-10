package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.Journal;

import java.util.List;
import java.util.UUID;

public interface JournalService {
    Journal createJournal(String author, Journal payload);

    Journal getJournalById(UUID journalRef);

    List<Journal> getJournalsByAuthor(String uid);

    int getJournalId(UUID journalRef);

    UUID getJournalRef(int journalId);

    Journal updateJournal(UUID journalRef, String author, Journal payload);

    Journal deleteJournal(UUID journalRef);

    boolean ownsJournal(String uid, UUID journalRef);
}
