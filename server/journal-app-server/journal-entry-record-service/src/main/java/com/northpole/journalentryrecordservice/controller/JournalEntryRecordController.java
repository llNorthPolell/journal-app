package com.northpole.journalentryrecordservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.common.entity.FlatRecord;
import com.northpole.common.entity.GeneralResponseBody;
import com.northpole.common.entity.JournalEntry;
import com.northpole.journalentryrecordservice.entity.JournalEntryRecordDataSet;
import com.northpole.journalentryrecordservice.entity.JournalEntryRecordServiceInput;
import com.northpole.journalentryrecordservice.service.JournalEntryRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JournalEntryRecordController {

    private JournalEntryRecordService journalEntryRecordService;

    private ObjectMapper objectMapper;

    @Autowired
    public JournalEntryRecordController(
            JournalEntryRecordService journalEntryRecordService,
            ObjectMapper objectMapper){
        this.journalEntryRecordService=journalEntryRecordService;
        this.objectMapper=objectMapper;
    }

    @KafkaListener(topics="${northpole.kafka.topic.journal-entry}")
    public GeneralResponseBody listen(@Payload JournalEntry payload){
        System.out.println(payload.toString());
        return journalEntryRecordService.save(payload);
    }

    @PostMapping("/records")
    public List<FlatRecord> handleGetRecordsRequest(
            @Valid @RequestBody JournalEntryRecordServiceInput input) throws JsonProcessingException {
        return journalEntryRecordService.getRecords(input);
    }


    @PostMapping("/records/dataset")
    public JournalEntryRecordDataSet handleGetDatasetRequest(
            @Valid @RequestBody JournalEntryRecordServiceInput input) throws JsonProcessingException {
        return journalEntryRecordService.getDataset(input);
    }
}
