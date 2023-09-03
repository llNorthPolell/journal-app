package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northpole.journalappserver.entity.*;
import com.northpole.journalappserver.repository.GoalRepository;
import com.northpole.journalappserver.repository.ObjectiveRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoalTrackerServiceImpl implements GoalTrackerService{

    private GoalRepository goalRepository;

    private ObjectiveRepository objectiveRepository;

    private ObjectMapper objectMapper;

    @Autowired
    public GoalTrackerServiceImpl(
            GoalRepository goalRepository,
            ObjectiveRepository objectiveRepository,
            ObjectMapper objectMapper
    ){
        this.goalRepository=goalRepository;
        this.objectiveRepository=objectiveRepository;
        this.objectMapper=objectMapper;
    }

    private boolean isNumeric(String input){
        try {
            Double.parseDouble(input);
            return true;
        }catch (NumberFormatException e) {
            return false;
        }
    }


    @Override
    @Transactional
    public GeneralResponseBody saveGoal(Goal payload) {
        LocalDateTime now = LocalDateTime.now();
        UUID saveId = UUID.randomUUID();
        int journalId = payload.getJournal();
        String defaultStatus="IN PROGRESS";
        Goal resultGoal;

        payload.setId(saveId);
        payload.setCreationTimestamp(now);
        payload.setLastUpdated(now);

        List<Objective> objectives = payload.getObjectives();

        for (Objective o : objectives) {
            o.setGoalId(saveId);
            o.setJournalId(journalId);
            o.setCreationTimestamp(now);
            o.setLastUpdated(now);
            o.setStatus(defaultStatus);
        }

        try {
            resultGoal=goalRepository.save(payload);

            List<Objective> objectiveSaveResults = objectiveRepository.saveAll(objectives);

            String objectiveIds = objectiveSaveResults.stream()
                    .map(o-> o.getId())
                    .collect(Collectors.toList())
                    .toString();

            return GeneralResponseBody.builder()
                    .status(200)
                    .message("{\"goalId\":\"" + resultGoal.getId().toString() + "\", \"objectiveIds\":"+objectiveIds+"}")
                    .timeStamp(System.currentTimeMillis())
                    .build();
        }
        catch(Exception e){
            System.out.println(e.getStackTrace());
            e.printStackTrace();
            return GeneralResponseBody.builder()
                    .status(500)
                    .message(e.getStackTrace().toString())
                    .timeStamp(System.currentTimeMillis())
                    .build();
        }
    }

    private FlatRecord[] extractFlatRecords(String message) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(message);
        JsonNode flatRecordsJson = root.get("flatRecords");
        return  objectMapper.convertValue(flatRecordsJson,FlatRecord[].class);
    }

    @Override
    public GeneralResponseBody updateProgress(String message)  {
        try {
            // extract flatRecords from message (output of JournalEntryRecordService.save)
            FlatRecord[] flatRecords = extractFlatRecords(message);
            Set<String> topics = Arrays.stream(flatRecords)
                    .map(f-> f.getTopic())
                    .collect(Collectors.toSet());

            // find list of objectives with topic and recKey
            List<Objective> objectives = objectiveRepository.findAllByJournalIdIsAndTopicInAndStatusIs(
                    flatRecords[0].getJournal(),topics, "IN PROGRESS");

            for (FlatRecord f: flatRecords){
                if (!isNumeric(f.getRecValue()))
                    continue;
                for (Objective o : objectives)
                    for (Progress p: o.getProgressList()) {
                        if (p.getEntryDateLastChecked() !=null &&
                                p.getEntryDateLastChecked().isBefore(f.getDateOfEntry()))
                            continue;

                        if (o.getTopic().equals(f.getTopic()) && p.getRecKey().equals(f.getRecKey())) {
                            p.setCurrentValue(Double.parseDouble(f.getRecValue()));
                            o.setLastUpdated(LocalDateTime.now());
                            p.setEntryDateLastChecked(f.getDateOfEntry());
                        }

                    }
            }

            // save updates
            List<Objective> saveResults = objectiveRepository.saveAll(objectives);
            String saveResultJson = objectMapper.writeValueAsString(saveResults);

            return GeneralResponseBody.builder()
                    .status(200)
                    .message("{\"objectives\":"+saveResultJson+"}")
                    .timeStamp(System.currentTimeMillis())
                    .build();
        }
        catch (Exception e){
            System.out.println(e.getStackTrace());
            e.printStackTrace();
            return GeneralResponseBody.builder()
                    .status(500)
                    .message(e.getStackTrace().toString())
                    .timeStamp(System.currentTimeMillis())
                    .build();
        }

    }
}
