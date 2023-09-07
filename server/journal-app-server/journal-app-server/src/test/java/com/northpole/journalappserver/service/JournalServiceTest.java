package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.Journal;
import com.northpole.journalappserver.repository.JournalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JournalServiceTest {
    @Mock
    private JournalRepository journalRepository;

    @InjectMocks
    private JournalService journalService;

    private final UUID MOCK_CREATE_REF = UUID.fromString("78c7fb9a-f3e1-458f-8edb-ec1666e2fb02");
    private final UUID MOCK_FIND_REF1 = UUID.fromString("c25a2251-f7e7-4f24-9e4e-ee6f38c68839");
    private final UUID MOCK_FIND_REF2 = UUID.fromString("b4a5894c-a174-4619-bb73-8e5c620aad10");
    private final UUID MOCK_NOT_EXIST_REF = UUID.fromString("f31c2d8d-6a1e-43bb-ac1e-b1af37f7e9aa");
    private final String MOCK_USER="ASD1F2SDF3W";

    @Autowired
    public JournalServiceTest(
            JournalService journalService,
            JournalRepository journalRepository
    ){
        this.journalService=journalService;
        this.journalRepository=journalRepository;
    }

    @BeforeEach
    public void setupBeforeEachTest(){
        when(journalRepository.save(any(Journal.class))).thenReturn(
                Journal.builder()
                        .journalId(1)
                        .journalRef(MOCK_CREATE_REF)
                        .build());

        when(journalRepository.findByJournalRef(MOCK_FIND_REF1))
                .thenReturn(
                        Journal.builder()
                                .journalRef(MOCK_FIND_REF1)
                                .author(MOCK_USER)
                                .build()
                );

        when(journalRepository.findByJournalRef(MOCK_FIND_REF2))
                .thenReturn(Journal.builder()
                        .journalRef(MOCK_FIND_REF1)
                        .author("123")
                        .build()
                );
    }

    @Test
    @DisplayName("Should call journalRepository.create")
    public void saveSuccess_UnitTest(){
        Journal result = journalService.createJournal(new Journal(),MOCK_USER);
        verify(journalRepository,times(1)).save(any(Journal.class));
        assertEquals(MOCK_CREATE_REF,result.getJournalRef());
    }


    @Test
    @DisplayName("Should call journalRepository.delete when journalId exists")
    public void deleteSuccess_UnitTest(){
        Journal result = journalService.deleteJournal(MOCK_FIND_REF1);
        verify(journalRepository,times(1)).findByJournalRef(any(UUID.class));
        verify(journalRepository,times(1)).delete(any(Journal.class));
    }

    @Test
    @DisplayName("Should not call journalRepository.delete when journalId does not exist")
    public void deleteNotExists_UnitTest(){
        Journal result = journalService.deleteJournal(MOCK_NOT_EXIST_REF);
        verify(journalRepository,times(1)).findByJournalRef(any(UUID.class));
        verify(journalRepository,times(0)).delete(any(Journal.class));
    }

    @Test
    @DisplayName("Should return true if journal.author is user")
    public void ownsJournal_True_UnitTest(){
        boolean result = journalService.ownsJournal(MOCK_USER, MOCK_FIND_REF1);
        verify(journalRepository,times(1)).findByJournalRef(any(UUID.class));
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false if journal.author is not user")
    public void ownsJournalNotOwned_UnitTest(){
        boolean result = journalService.ownsJournal(MOCK_USER, MOCK_FIND_REF2);
        verify(journalRepository,times(1)).findByJournalRef(any(UUID.class));
        assertTrue(!result);
    }

    @Test
    @DisplayName("Should return false if journal does not exist")
    public void ownsJournalNotExists_UnitTest(){
        boolean result = journalService.ownsJournal(MOCK_USER, MOCK_NOT_EXIST_REF);
        verify(journalRepository,times(1)).findByJournalRef(any(UUID.class));
        assertTrue(!result);
    }
}
