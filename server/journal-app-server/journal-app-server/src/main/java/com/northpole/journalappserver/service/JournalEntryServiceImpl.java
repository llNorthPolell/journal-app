package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.JournalEntry;
import com.northpole.journalappserver.repository.JournalEntryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class JournalEntryServiceImpl implements JournalEntryService {
    private JournalEntryRepository journalEntryRepository;
    private JournalEntryRecordService journalEntryRecordService;
    private GoalTrackerService goalTrackerService;
    private ObjectMapper objectMapper;

    @Autowired
    public JournalEntryServiceImpl(
            JournalEntryRepository journalEntryRepository,
            JournalEntryRecordService journalEntryRecordService,
            GoalTrackerService goalTrackerService,
            ObjectMapper objectMapper) {
        this.journalEntryRepository=journalEntryRepository;
        this.journalEntryRecordService=journalEntryRecordService;
        this.goalTrackerService=goalTrackerService;
        this.objectMapper = objectMapper;
    }
    
    @Override
    @Transactional
    public JournalEntry save(UUID journalRef, JournalEntry payload) throws JsonProcessingException{
        LocalDateTime now = LocalDateTime.now();
        UUID saveId = UUID.randomUUID();
        payload.setEntryId(saveId);
        payload.setJournal(journalRef);
        payload.setCreationTimestamp(now);
        payload.setLastUpdated(now);
        JournalEntry saveResult = journalEntryRepository.save(payload);
        String extractAndSaveFlatRecordResult = journalEntryRecordService.save(payload);
        String updateProgressResult = goalTrackerService.updateProgress(
                extractAndSaveFlatRecordResult);

        if (saveResult==null || extractAndSaveFlatRecordResult==null || updateProgressResult==null)
            return null;

        return saveResult;

    }

    @Override
    public JournalEntry getJournalEntryById(UUID journalEntryId) {
        Optional<JournalEntry> result = journalEntryRepository.findById(journalEntryId);
        return (result.isPresent())? result.get() : null;
    }

    @Override
    public List<JournalEntry> getJournalEntriesInJournal(UUID journalRef) {
        AggregationResults<JournalEntry> result = this.journalEntryRepository.findAllByJournal(journalRef);

        if (result == null)
            return null;

        return result.getMappedResults();
    }

    @Override
    public JournalEntry getLastEntryInJournal(UUID journalRef) {
        AggregationResults<JournalEntry> result = this.journalEntryRepository.findLastEntryInJournal(journalRef);
        List<JournalEntry> resultList = result.getMappedResults();
        if (resultList.isEmpty())
            return null;

        return resultList.get(0);
    }
}
