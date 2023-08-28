package com.northpole.journalappserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.service.JournalEntryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
