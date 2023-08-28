package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.JournalEntry;
import com.northpole.journalappserver.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class JournalEntryServiceImpl implements JournalEntryService {
    private JournalEntryRepository journalEntryRepository;
    private JournalEntryRecordService journalEntryRecordService;
    private ObjectMapper objectMapper;

    @Autowired
    public JournalEntryServiceImpl(
            JournalEntryRepository journalEntryRepository,
            JournalEntryRecordService journalEntryRecordService,
            ObjectMapper objectMapper) {
        this.journalEntryRepository=journalEntryRepository;
        this.journalEntryRecordService=journalEntryRecordService;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public GeneralResponseBody save(JournalEntry payload) {
        UUID saveId = UUID.randomUUID();
        payload.setEntryId(saveId);

        try {
            JournalEntry saveResult = journalEntryRepository.save(payload);
            GeneralResponseBody extractAndSaveFlatRecordResult = journalEntryRecordService.save(payload);

            return GeneralResponseBody.builder()
                    .status(200)
                    .message("{\"id\":\"" + saveId + "\"}")
                    .timeStamp(System.currentTimeMillis())
                    .build();
        }
        catch(Exception e){
            System.out.println(e.getStackTrace());
            return GeneralResponseBody.builder()
                    .status(500)
                    .message(e.getStackTrace().toString())
                    .timeStamp(System.currentTimeMillis())
                    .build();
        }
    }

    @Override
    public List<JournalEntry> getJournalEntriesById(int journalId) {
        AggregationResults<JournalEntry> result = this.journalEntryRepository.findAllByJournal(journalId);

        if (result == null)
            return null;

        return result.getMappedResults();
    }

    @Override
    public JournalEntry getLastEntryInJournal(int journalId) {
        AggregationResults<JournalEntry> result = this.journalEntryRepository.findLastEntryInJournal(journalId);
        List<JournalEntry> resultList = result.getMappedResults();
        if (resultList.isEmpty())
            return null;

        return resultList.get(0);
    }
}
