package com.northpole.journalappserver.controller;

import com.northpole.journalappserver.entity.Journal;
import com.northpole.journalappserver.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
public class JournalController {

    private JournalService journalService;

    @Autowired
    public JournalController(
            JournalService journalService
    ){
        this.journalService=journalService;
    }

    @PostMapping("/journals")
    public ResponseEntity<String> createJournal(@RequestBody Journal payload, Principal principal){
        try {
            Journal outputJournal = journalService.createJournal(principal.getName(),payload);
            return new ResponseEntity<>(
                    "{\"id\":\""+outputJournal.getJournalRef()+"\"}",
                    HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/journals/{journalRef}")
    @PreAuthorize("@securityService.ownsJournal(#journalRef)")
    public ResponseEntity<String> updateJournal(@PathVariable("journalRef")UUID journalRef,
                                                @RequestBody Journal payload,
                                                Principal principal){
        try{
            Journal outputJournal = journalService.updateJournal(
                    journalRef,
                    principal.getName(),
                    payload);

            return new ResponseEntity<>(
                    "{\"id\":\""+outputJournal.getJournalRef()+"\"}",
                    HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/journals/{journalRef}")
    @PreAuthorize("@securityService.ownsJournal(#journalRef)")
    public Journal getJournalById(@PathVariable("journalRef")UUID journalRef){
        return journalService.getJournalById(journalRef);
    }


    @GetMapping("/journals")
    public List<Journal> getJournalsByUser(Principal principal){
        return journalService.getJournalsByAuthor(principal.getName());
    }

    @DeleteMapping("/journals/{journalRef}")
    @PreAuthorize("@securityService.ownsJournal(#journalRef)")
    public ResponseEntity<String> deleteJournal(@PathVariable("journalRef") UUID journalRef){
        try {
            Journal outputJournal = journalService.deleteJournal(journalRef);
            String message="Journal with id " + journalRef + " does not exist...";
            HttpStatus status = HttpStatus.NOT_FOUND;

            if (outputJournal!=null) {
                message = "{\"id\":"+outputJournal.getJournalRef()+"}";
                status= HttpStatus.OK;
            }

            return new ResponseEntity<>(
                    message,
                    status);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
