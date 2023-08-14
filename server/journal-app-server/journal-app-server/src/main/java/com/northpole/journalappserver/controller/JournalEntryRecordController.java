package com.northpole.journalappserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.JournalEntryRecordDataSet;
import com.northpole.journalappserver.entity.JournalEntryRecordServiceInput;
import com.northpole.journalappserver.service.JournalEntryRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
