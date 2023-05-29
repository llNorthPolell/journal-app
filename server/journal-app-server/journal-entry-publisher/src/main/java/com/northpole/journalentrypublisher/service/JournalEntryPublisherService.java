package com.northpole.journalentrypublisher.service;


import com.northpole.journalentrypublisher.entity.JournalEntry;

import java.util.UUID;

public interface JournalEntryPublisherService {

    public UUID processJournalEntry(JournalEntry payload);
}
