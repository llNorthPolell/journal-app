package com.northpole.journalappserver.controller;

import com.northpole.journalappserver.entity.JournalEntry;
import com.northpole.journalappserver.service.JournalEntryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class JournalEntryController {

    private JournalEntryService journalEntryService;

    @Autowired
    public JournalEntryController(JournalEntryService journalEntryService){
        this.journalEntryService=journalEntryService;
    }

    @PostMapping("/{journalRef}/journalEntries")
    @PreAuthorize("@securityService.ownsJournal(#journalRef)")
    public ResponseEntity<String> saveJournalEntry (@PathVariable("journalRef") UUID journalRef,
                                                    @Valid @RequestBody JournalEntry payload) {
        try {
            JournalEntry saveJournalEntry = journalEntryService.save(journalRef,payload);
            return new ResponseEntity<>(
                    "{\"id\":\"" + saveJournalEntry.getEntryId() + "\"}",
                    HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{journalRef}/journalEntries")
    @PreAuthorize("@securityService.ownsJournal(#journalRef)")
    public List<JournalEntry> getJournalEntriesInJournal(@PathVariable("journalRef") UUID journalRef){
        return journalEntryService.getJournalEntriesInJournal(journalRef);
    }


    @GetMapping("/{journalRef}/journalEntries/{journalEntryId}")
    @PreAuthorize("@securityService.ownsJournal(#journalRef)")
    public JournalEntry getJournalEntriesInJournal(@PathVariable("journalRef") UUID journalRef,
                                                         @PathVariable("journalEntryId") UUID journalEntryId){
        return journalEntryService.getJournalEntryById(journalEntryId);
    }

}
