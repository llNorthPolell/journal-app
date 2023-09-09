package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.Journal;
import com.northpole.journalappserver.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class JournalServiceImpl implements JournalService{

    private JournalRepository journalRepository;

    @Autowired
    public JournalServiceImpl(
            JournalRepository journalRepository
    ){
        this.journalRepository=journalRepository;
    }

    @Override
    public Journal createJournal(String author, Journal payload) {
        LocalDateTime now = LocalDateTime.now();
        UUID saveRef = UUID.randomUUID();
        payload.setAuthor(author);
        payload.setJournalRef(saveRef);
        payload.setCreationTimestamp(now);
        payload.setLastUpdated(now);
        Journal saveJournal = journalRepository.save(payload);
        return saveJournal;
    }

    @Override
    public Journal getJournalById(UUID journalRef){
        return journalRepository.findByJournalRef(journalRef);
    }


    @Override
    public List<Journal> getJournalsByAuthor(String uid) {
        return journalRepository.findJournalsByAuthor(uid);
    }

    @Override
    public int getJournalId(UUID journalRef){
        return journalRepository.findJournalId(journalRef);
    }

    @Override
    public UUID getJournalRef(int journalId) {
        return journalRepository.findJournalRef(journalId);
    }

    @Override
    public Journal updateJournal(UUID journalRef, String uid, Journal payload) {
        LocalDateTime now = LocalDateTime.now();
        Journal journalToUpdate = journalRepository.findByJournalRef(journalRef);

        if(journalToUpdate==null) return null;

        journalToUpdate.setName(payload.getName());
        journalToUpdate.setImg(payload.getImg());
        journalToUpdate.setLastUpdated(now);
        Journal output = journalRepository.save(journalToUpdate);

        return output;
    }

    @Override
    public Journal deleteJournal(UUID journalRef){
            Journal toDelete = journalRepository.findByJournalRef(journalRef);
            if (toDelete==null)
                return null;

            journalRepository.delete(toDelete);
            return toDelete;

    }

    @Override
    public boolean ownsJournal(String uid, UUID journalRef) {
        Journal journal = journalRepository.findByJournalRef(journalRef);
        if(journal==null) return false;
        return journal.getAuthor().equals(uid);
    }


}
