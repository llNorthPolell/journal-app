package com.northpole.journalappserver.controller;

import com.northpole.journalappserver.entity.RecordKey;
import com.northpole.journalappserver.service.FlatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class FlatRecordController {

    private FlatRecordService flatRecordService;

    @Autowired
    public FlatRecordController(
            FlatRecordService flatRecordService
    ){
        this.flatRecordService=flatRecordService;
    }

    @GetMapping("/{journalRef}/recordFields")
    @PreAuthorize("@securityService.ownsJournal(#journalRef)")
    public List<RecordKey> getRecordFieldsInJournal(@PathVariable("journalRef")UUID journalRef){
        return flatRecordService.getRecordFieldsInJournal(journalRef);
    }
}
