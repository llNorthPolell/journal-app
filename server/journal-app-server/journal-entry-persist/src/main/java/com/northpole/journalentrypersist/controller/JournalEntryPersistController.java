package com.northpole.journalentrypersist.controller;

import com.northpole.common.entity.GeneralResponseBody;
import com.northpole.common.entity.JournalEntry;
import com.northpole.journalentrypersist.service.JournalEntryPersistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class JournalEntryPersistController {

    private JournalEntryPersistService journalEntryPersistService;

    @Autowired
    public JournalEntryPersistController(JournalEntryPersistService journalEntryPersistService){
        this.journalEntryPersistService = journalEntryPersistService;
    }

    @KafkaListener(topics="${northpole.kafka.topic}")
    public GeneralResponseBody listen(@Payload JournalEntry payload){
        System.out.println(payload.toString());

        // save to data source
        return journalEntryPersistService.save(payload);
    }
}
