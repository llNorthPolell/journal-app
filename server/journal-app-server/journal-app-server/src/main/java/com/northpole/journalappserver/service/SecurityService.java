package com.northpole.journalappserver.service;

import java.util.UUID;

public interface SecurityService {
    boolean ownsJournal(UUID journalRef);

    boolean ownsJournalEntry(UUID journalRef, UUID journalEntryId);

    boolean ownsDashboardWidget(UUID journalRef, int widgetId);

    boolean ownsGoal(UUID journalRef, UUID goalId);
}
