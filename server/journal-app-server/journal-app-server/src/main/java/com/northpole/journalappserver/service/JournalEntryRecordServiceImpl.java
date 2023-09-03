package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.*;
import com.northpole.journalappserver.entity.Record;
import com.northpole.journalappserver.repository.FlatRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class JournalEntryRecordServiceImpl implements JournalEntryRecordService {

    private FlatRecordRepository flatRecordRepository;

    private ObjectMapper objectMapper;

    @Autowired
    public JournalEntryRecordServiceImpl(
            FlatRecordRepository flatRecordRepository,
            ObjectMapper objectMapper
    ){
        this.flatRecordRepository=flatRecordRepository;
        this.objectMapper=objectMapper;
    }

    private List<FlatRecord> getFlattenedRecords(
            int journal, LocalDateTime dateOfEntry, String topic, List<Record> recordList, List<FlatRecord> output) {
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
    public GeneralResponseBody save(JournalEntry payload) {
        List<FlatRecord> recordsToSave = new ArrayList<>();
        int status = HttpStatus.OK.value();
        String message;

        for(JournalBodyItem j : payload.getJournalBodyItems()){
            List<Record> recordList = new ArrayList<>(j.getRecordList());
            List<FlatRecord> newRecords = getFlattenedRecords(
                    payload.getJournal(),payload.getDateOfEntry(),j.getTopic(),recordList,new ArrayList<>());

            recordsToSave.addAll(newRecords);
        }

        try {
            List<FlatRecord> saveResults = flatRecordRepository.saveAll(recordsToSave);
            String flatRecordJson = objectMapper.writeValueAsString(saveResults);

            message = "{\"count\":"+recordsToSave.size()+","+
                    "\"flatRecords\":" +flatRecordJson+"}";

            return GeneralResponseBody.builder()
                    .status(status)
                    .message(message)
                    .timeStamp(System.currentTimeMillis())
                    .build();
        }
        catch(DataAccessException | JsonProcessingException e){
            status=HttpStatus.INTERNAL_SERVER_ERROR.value();
            message=e.getMessage();

            // TODO: send to retry topic

            return GeneralResponseBody.builder()
                    .status(status)
                    .message(message)
                    .timeStamp(System.currentTimeMillis())
                    .build();
        }
    }


    @Override
    public List<FlatRecord> getDashboardData(int journalId) {
        AggregationResults<FlatRecord> results = flatRecordRepository.getDashboardData(journalId);
        return results.getMappedResults();
    }




}
