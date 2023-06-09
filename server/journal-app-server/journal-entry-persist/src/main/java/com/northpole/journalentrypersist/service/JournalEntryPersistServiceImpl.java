package com.northpole.journalentrypersist.service;

import com.northpole.common.entity.GeneralResponseBody;
import com.northpole.common.entity.JournalEntry;
import com.northpole.journalentrypersist.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalEntryPersistServiceImpl implements JournalEntryPersistService{

    private JournalEntryRepository journalEntryRepository;

    @Autowired
    public JournalEntryPersistServiceImpl (JournalEntryRepository journalEntryRepository){
        this.journalEntryRepository = journalEntryRepository;
    }

    @Override
    public GeneralResponseBody save(JournalEntry payload) {
        JournalEntry result = journalEntryRepository.save(payload);
        return GeneralResponseBody.builder()
                .status(200)
                .build();
    }
}
