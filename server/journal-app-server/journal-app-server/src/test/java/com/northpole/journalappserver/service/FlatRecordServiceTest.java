package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.RecordKey;
import com.northpole.journalappserver.repository.FlatRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class FlatRecordServiceTest {
    @Mock
    private FlatRecordRepository flatRecordRepository;


    @InjectMocks
    private FlatRecordService flatRecordService;

    private final UUID MOCK_JOURNAL_REF=UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");

    @Autowired
    public FlatRecordServiceTest(
            FlatRecordService flatRecordService
    ){
        this.flatRecordService=flatRecordService;
    }


    @Test
    @DisplayName("Should call flatRecordRepoository.findRecordKeysInJournal once when retrieving " +
            "topic-recKey pairs in a journal")
    public void getRecordFieldsInJournal_UnitTest(){
        List<RecordKey> results = flatRecordService.getRecordFieldsInJournal(MOCK_JOURNAL_REF);
        verify(flatRecordRepository,times(1)).findRecordKeysInJournal(any(UUID.class));
    }
}
