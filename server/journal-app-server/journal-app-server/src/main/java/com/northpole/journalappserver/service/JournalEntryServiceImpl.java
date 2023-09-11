package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.JournalBodyItem;
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
    public JournalEntry publishJournalEntry(UUID journalRef, JournalEntry payload) throws JsonProcessingException{
        LocalDateTime now = LocalDateTime.now();
        UUID saveId = UUID.randomUUID();
        payload.setEntryId(saveId);
        payload.setJournal(journalRef);
        payload.setCreationTimestamp(now);
        payload.setLastUpdated(now);
        JournalEntry saveResult = journalEntryRepository.save(payload);
        List<FlatRecord> extractAndSaveFlatRecordResults = journalEntryRecordService.save(payload);
        String updateProgressResult = goalTrackerService.updateProgress(
                extractAndSaveFlatRecordResults);

        if (saveResult==null || extractAndSaveFlatRecordResults.isEmpty() || updateProgressResult==null)
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
        Optional<JournalEntry> result = this.journalEntryRepository.findLastEntryInJournal(journalRef);
        if (result.isEmpty()) return null;
        return result.get();
    }

    @Override
    public JournalEntry updateJournalEntry(UUID journalRef, UUID journalEntryId, JournalEntry payload) throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        Optional<JournalEntry> findJournalEntryToUpdate = journalEntryRepository.findById(journalEntryId);

        if (findJournalEntryToUpdate.isEmpty()) return null;

        JournalEntry journalEntryToUpdate=findJournalEntryToUpdate.get();
        journalEntryToUpdate.setOverview(payload.getOverview());
        journalEntryToUpdate.setSummary(payload.getSummary());
        journalEntryToUpdate.setLastUpdated(now);

        // replace journal body as on user will work with existing data fetched from the server when updating
        journalEntryToUpdate.setJournalBodyItems(payload.getJournalBodyItems());

        JournalEntry saveResult = journalEntryRepository.save(journalEntryToUpdate);

        List<FlatRecord> updateRelatedFlatRecordResults = journalEntryRecordService
                .updateRelatedFlatRecords(journalRef,journalEntryId,payload);

        return saveResult;
    }

    @Override
    @Transactional
    public JournalEntry deleteJournalEntry(UUID journalEntryId) {
        Optional<JournalEntry> findJournalEntryToDelete = journalEntryRepository.findById(journalEntryId);

        if(findJournalEntryToDelete.isEmpty()) return null;

        JournalEntry journalEntryToDelete = findJournalEntryToDelete.get();
        journalEntryRepository.delete(journalEntryToDelete);

        List<FlatRecord> deletedFlatRecords=journalEntryRecordService
                .deleteRelatedFlatRecords(journalEntryId);

        return journalEntryToDelete;
    }

    @Override
    public boolean ownsJournalEntry(UUID journalRef, UUID journalEntryId){
        Optional<JournalEntry> checkJournalEntry = journalEntryRepository.findById(journalEntryId);
        if (checkJournalEntry.isEmpty()) return false;
        return checkJournalEntry.get().getJournal().equals(journalRef);
    }
}
