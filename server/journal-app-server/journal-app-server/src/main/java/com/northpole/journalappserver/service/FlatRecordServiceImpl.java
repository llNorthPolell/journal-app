package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.RecordKey;
import com.northpole.journalappserver.repository.FlatRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FlatRecordServiceImpl implements  FlatRecordService{

    private FlatRecordRepository flatRecordRepository;

    @Autowired
    public FlatRecordServiceImpl(
            FlatRecordRepository flatRecordRepository
    ){
        this.flatRecordRepository=flatRecordRepository;
    }

    @Override
    public List<RecordKey> getRecordFieldsInJournal(UUID journalRef) {
        return flatRecordRepository.findRecordKeysInJournal(journalRef);
    }
}
