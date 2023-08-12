package com.northpole.journalentryrecordservice.service;

import com.northpole.common.entity.*;
import com.northpole.common.entity.Record;
import com.northpole.journalentryrecordservice.entity.JournalEntryRecordDataSet;
import com.northpole.journalentryrecordservice.entity.JournalEntryRecordServiceInput;
import com.northpole.journalentryrecordservice.repository.FlatRecordRepository;
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

    @Autowired
    public JournalEntryRecordServiceImpl(
            FlatRecordRepository flatRecordRepository
    ){
        this.flatRecordRepository=flatRecordRepository;
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
        List<FlatRecord> recordsToSave = new ArrayList<FlatRecord>();
        int status = HttpStatus.OK.value();
        String message = "";

        for(JournalBodyItem j : payload.getJournalBodyItems()){
            List<Record> recordList = new ArrayList<>(j.getRecordList());
            List<FlatRecord> newRecords = getFlattenedRecords(
                    payload.getJournal(),payload.getDateOfEntry(),j.getTopic(),recordList,new ArrayList<>());

            recordsToSave.addAll(newRecords);
        }

        try {
            this.flatRecordRepository.saveAll(recordsToSave);

            message = "{\"count\":"+recordsToSave.size()+","+
                    "\"flatRecords\":" +recordsToSave.toString()+"}";

            return GeneralResponseBody.builder()
                    .status(status)
                    .message(message)
                    .timeStamp(System.currentTimeMillis())
                    .build();
        }
        catch(DataAccessException e){
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
    public List<FlatRecord> getRecords(JournalEntryRecordServiceInput payload) {
        return flatRecordRepository.findAllByIndices(
                payload.getJournal(), payload.getTopic(), payload.getRecKey());

    }

    @Override
    public JournalEntryRecordDataSet getDataset(JournalEntryRecordServiceInput testInput) {
        AggregationResults<JournalEntryRecordDataSet> result =
                (testInput.getRecKeyX()==null || testInput.getRecKeyX().equals("dateOfEntry")) ?
            flatRecordRepository.getDataByDateOfEntry(
                    testInput.getJournal(),
                    testInput.getTopic(),
                    testInput.getRecKeyY()
            ) :
            flatRecordRepository.getDataByCustomField(
              testInput.getJournal(),
              testInput.getTopic(),
              testInput.getRecKeyX(),
              testInput.getRecKeyY()
            );


        return result.getMappedResults().get(0);
    }


}
