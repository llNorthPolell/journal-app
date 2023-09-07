package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.*;
import com.northpole.journalappserver.entity.Record;
import com.northpole.journalappserver.repository.FlatRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            UUID journal, LocalDateTime dateOfEntry, String topic, List<Record> recordList, List<FlatRecord> output) {
        if (recordList.isEmpty()) return output;

        Record input = recordList.remove(0);

        FlatRecord newFlatRecord = FlatRecord.builder()
                .journal(journal)
                .id(UUID.randomUUID())
                .topic(topic)
                .recKey(input.getRecKey())
                .recValue(input.getRecValue())
                .dateOfEntry(dateOfEntry)
                .build();

        output.add(newFlatRecord);

        return getFlattenedRecords(journal,dateOfEntry,topic,recordList,output);
    }


    @Override
    public String save(JournalEntry payload) throws JsonProcessingException {
        List<FlatRecord> recordsToSave = new ArrayList<>();

        for(JournalBodyItem j : payload.getJournalBodyItems()){
            List<Record> recordList = new ArrayList<>(j.getRecordList());
            List<FlatRecord> newRecords = getFlattenedRecords(
                    payload.getJournal(),
                    payload.getDateOfEntry(),
                    j.getTopic(),recordList,
                    new ArrayList<>());

            recordsToSave.addAll(newRecords);
        }

        List<FlatRecord> saveResults = flatRecordRepository.saveAll(recordsToSave);
        String flatRecordJson = objectMapper.writeValueAsString(saveResults);

        return "{\"count\":"+recordsToSave.size()+","+
                    "\"flatRecords\":" +flatRecordJson+"}";

    }


    @Override
    public List<FlatRecord> getDashboardData(UUID journalRef) {
        AggregationResults<FlatRecord> results = flatRecordRepository.getDashboardData(journalRef);
        return results.getMappedResults();
    }




}
