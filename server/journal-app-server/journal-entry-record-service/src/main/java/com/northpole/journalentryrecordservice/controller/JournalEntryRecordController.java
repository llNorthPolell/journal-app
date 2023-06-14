package com.northpole.journalentryrecordservice.controller;

import com.northpole.common.entity.GeneralResponseBody;
import com.northpole.common.entity.JournalEntry;
import com.northpole.journalentryrecordservice.service.JournalEntryRecordService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class JournalEntryRecordController {

    private JournalEntryRecordService journalEntryRecordService;

    public JournalEntryRecordController(
            JournalEntryRecordService journalEntryRecordService){
        this.journalEntryRecordService=journalEntryRecordService;
    }

    @KafkaListener(topics="${northpole.kafka.topic.journal-entry}")
    public GeneralResponseBody listen(@Payload JournalEntry payload){
        System.out.println(payload.toString());
        return journalEntryRecordService.save(payload);
    }

}
