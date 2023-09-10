package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.*;
import com.northpole.journalappserver.entity.Record;
import com.northpole.journalappserver.repository.FlatRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JournalEntryRecordServiceImpl implements JournalEntryRecordService {

    private FlatRecordRepository flatRecordRepository;

    private JournalService journalService;

    private ObjectMapper objectMapper;

    @Autowired
    public JournalEntryRecordServiceImpl(
            FlatRecordRepository flatRecordRepository,
            JournalService journalService,
            ObjectMapper objectMapper
    ){
        this.flatRecordRepository=flatRecordRepository;
        this.journalService=journalService;
        this.objectMapper=objectMapper;
    }

    private List<FlatRecord> getFlattenedRecords(
            UUID journal, UUID journalEntry,LocalDateTime dateOfEntry,
            String topic, List<Record> recordList, List<FlatRecord> output,
            boolean newIdRequired) {
        if (recordList.isEmpty()) return output;

        Record input = recordList.remove(0);

        FlatRecord newFlatRecord = FlatRecord.builder()
                .journal(journal)
                .journalEntry(journalEntry)
                .topic(topic)
                .recKey(input.getRecKey())
                .recValue(input.getRecValue())
                .dateOfEntry(dateOfEntry)
                .build();

        if(newIdRequired)
            newFlatRecord.setId(UUID.randomUUID());

        output.add(newFlatRecord);

        return getFlattenedRecords(journal,journalEntry,dateOfEntry,
                topic,recordList,output,newIdRequired);
    }


    @Override
    public List<FlatRecord> save(JournalEntry payload){
        List<FlatRecord> recordsToSave = new ArrayList<>();

        for(JournalBodyItem j : payload.getJournalBodyItems()){
            List<Record> recordList = new ArrayList<>(j.getRecordList());
            List<FlatRecord> newRecords = getFlattenedRecords(
                    payload.getJournal(),
                    payload.getEntryId(),
                    payload.getDateOfEntry(),
                    j.getTopic(),recordList,
                    new ArrayList<>(),
                    true);

            recordsToSave.addAll(newRecords);
        }

        List<FlatRecord> saveResults = flatRecordRepository.saveAll(recordsToSave);

        return saveResults;

    }


    @Override
    public List<FlatRecord> getDashboardData(UUID journalRef) {
        AggregationResults<FlatRecord> results = flatRecordRepository.getDashboardData(journalRef);
        return results.getMappedResults();
    }

    @Override
    @Transactional
    public List<FlatRecord> updateRelatedFlatRecords(UUID journalEntryId, JournalEntry payload) {
        List<FlatRecord> recordsToDelete = flatRecordRepository.findAllByJournalEntry(journalEntryId);
        if (recordsToDelete == null) return null;

        List<FlatRecord> payloadRecords = new ArrayList<>();
        for(JournalBodyItem j : payload.getJournalBodyItems()){
            List<Record> recordList = new ArrayList<>(j.getRecordList());
            List<FlatRecord> tempFlatRecords = getFlattenedRecords(
                    payload.getJournal(),
                    payload.getEntryId(),
                    payload.getDateOfEntry(),
                    j.getTopic(),recordList,
                    new ArrayList<>(),
                    true);

            payloadRecords.addAll(tempFlatRecords);
        }

        flatRecordRepository.deleteAll(recordsToDelete);
        List<FlatRecord> updateResults = flatRecordRepository.saveAll(payloadRecords);


        return updateResults;

    }


    @Override
    public List<FlatRecord> deleteRelatedFlatRecords(UUID journalEntryId){
        List<FlatRecord> flatRecordsToDelete =
                flatRecordRepository.findAllByJournalEntry(journalEntryId);

        if(flatRecordsToDelete.isEmpty()) return null;

        flatRecordRepository.deleteAll(flatRecordsToDelete);

        return flatRecordsToDelete;
    }

}
