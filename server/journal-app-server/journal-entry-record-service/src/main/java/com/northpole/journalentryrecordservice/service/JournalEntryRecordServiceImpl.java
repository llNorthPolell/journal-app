package com.northpole.journalentryrecordservice.service;

import com.northpole.common.entity.*;
import com.northpole.common.entity.Record;
import com.northpole.journalentryrecordservice.repository.FlatRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

            // send to retry loop

            return GeneralResponseBody.builder()
                    .status(status)
                    .message(message)
                    .timeStamp(System.currentTimeMillis())
                    .build();
        }

    }
}
