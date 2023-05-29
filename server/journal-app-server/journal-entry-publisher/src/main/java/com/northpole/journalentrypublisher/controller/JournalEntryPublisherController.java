package com.northpole.journalentrypublisher.controller;

import com.northpole.journalentrypublisher.entity.GeneralResponseBody;
import com.northpole.journalentrypublisher.entity.JournalEntry;
import com.northpole.journalentrypublisher.service.JournalEntryPublisherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class JournalEntryPublisherController {

    private JournalEntryPublisherService journalEntryPublisherService;

    @Autowired
    public JournalEntryPublisherController(JournalEntryPublisherService journalEntryPublisherService){
        this.journalEntryPublisherService=journalEntryPublisherService;
    }

    @PostMapping("/publishJournalEntry")
    public ResponseEntity<GeneralResponseBody> publishJournalEntry (@Valid @ModelAttribute JournalEntry payload) {
        HttpStatus status = HttpStatus.OK;
        UUID result = journalEntryPublisherService.processJournalEntry(payload);

        GeneralResponseBody responseBody = GeneralResponseBody.builder()
                .status(status.value())
                .message((result!=null)?result.toString():"Failed to get journal entry id")
                .timeStamp(System.currentTimeMillis())
                .build();

        return new ResponseEntity<>(responseBody, status);
    }

}
