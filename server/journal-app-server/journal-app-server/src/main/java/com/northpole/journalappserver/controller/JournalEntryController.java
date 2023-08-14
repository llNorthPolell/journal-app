package com.northpole.journalappserver.controller;

import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.JournalEntry;
import com.northpole.journalappserver.service.JournalEntryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JournalEntryController {

    private JournalEntryService journalEntryService;

    @Autowired
    public JournalEntryController(JournalEntryService journalEntryService){
        this.journalEntryService=journalEntryService;
    }

    @PostMapping("/journalEntry")
    public ResponseEntity<GeneralResponseBody> saveJournalEntry (@Valid @RequestBody JournalEntry payload) {
        HttpStatus status = HttpStatus.OK;
        System.out.println(payload.toString());


        GeneralResponseBody responseBody = journalEntryService.save(payload);

        return new ResponseEntity<>(responseBody, status);

    }

}
