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

    private JournalService journalService;

    private ObjectMapper objectMapper;

    @Autowired
    public GoalTrackerServiceImpl(
            GoalRepository goalRepository,
            ObjectiveRepository objectiveRepository,
            JournalService journalService,
            ObjectMapper objectMapper
    ){
        this.goalRepository=goalRepository;
        this.objectiveRepository=objectiveRepository;
        this.journalService=journalService;
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
    public Goal saveGoal(UUID journalRef, Goal payload) {
        LocalDateTime now = LocalDateTime.now();
        UUID saveId = UUID.randomUUID();
        String defaultStatus = "IN PROGRESS";
        Goal saveGoal;
        List<Objective> saveObjectives;
        List<Objective> objectives = payload.getObjectives();

        payload.setId(saveId);
        payload.setJournal(journalRef);
        payload.setObjectives(null);    // strip objectives from MongoDB as not needed and takes up space

        for (Objective o : objectives) {
            o.setGoalId(saveId);
            o.setJournalId(journalService.getJournalId(journalRef));
            o.setCreationTimestamp(now);
            o.setLastUpdated(now);
            o.setStatus(defaultStatus);

            if (o.getCompletionCriteria() == null)
                o.setCompletionCriteria("AND");

            for (Progress p : o.getProgressList())
                if (p.getCurrentValue()==null)
                    p.setCurrentValue(0d);
        }

        saveGoal = goalRepository.save(payload);
        saveObjectives = objectiveRepository.saveAll(objectives);

        saveGoal.setObjectives(saveObjectives);

        return saveGoal;
    }

    @Override
    public Goal getGoalById(UUID id) {
        Optional<Goal> goal = goalRepository.findById(id);
        Goal output;
        List<Objective> objectives;

        if (goal.isEmpty()) return null;

        output = goal.get();
        objectives=objectiveRepository.findAllByGoalId(id);

        output.setObjectives(objectives);

        return output;
    }

    @Override
    @Transactional
    public List<Goal> getGoalPreviewsInJournal(UUID journalRef){
            return goalRepository.findAllByJournal(journalRef);
    }

    private void packageGoals(List<Goal> goals, List<Objective> objectives){
        Map<UUID, List<Objective>> goalObjectiveMap = new HashMap<>();
        for (Objective o : objectives) {
            if (!goalObjectiveMap.containsKey(o.getGoalId()))
                goalObjectiveMap.put(o.getGoalId(), new ArrayList<>());

            goalObjectiveMap.get(o.getGoalId()).add(o);
        }

        for (Goal g : goals) {
            if (!goalObjectiveMap.containsKey(g.getId())) {
                g.setObjectives(new ArrayList<>());
                continue;
            }
            g.setObjectives(goalObjectiveMap.get(g.getId()));
        }
    }


    @Override
    @Transactional
    public List<Goal> getGoalsWithProgressInJournal(UUID journalRef) {
        List<Goal> goals = goalRepository.findAllByJournal(journalRef);
        List<Objective> objectiveList = objectiveRepository.findAllByJournalId(
                journalService.getJournalId(journalRef));
        packageGoals(goals,objectiveList);

        return goals;
    }

    @Override
    public List<Goal> getCompletedGoals(UUID journalRef) {
        return goalRepository.findCompletedGoalsInJournal(journalRef);
    }

    @Override
    public Goal updateGoal(Goal payload) {
        return null;
    }

    @Override
    public Goal deleteGoal(Goal goalId) {
        return null;
    }


    private FlatRecord[] extractFlatRecords(String message) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(message);
        JsonNode flatRecordsJson = root.get("flatRecords");
        return  objectMapper.convertValue(flatRecordsJson,FlatRecord[].class);
    }

    @Override
    public String updateProgress(String message) throws JsonProcessingException {
        // extract flatRecords from message (output of JournalEntryRecordService.save)
        FlatRecord[] flatRecords = extractFlatRecords(message);
        Set<String> topics = Arrays.stream(flatRecords)
                .map(f -> f.getTopic())
                .collect(Collectors.toSet());

        // find list of objectives with topic and recKey
        List<Objective> objectives = objectiveRepository.findAllByJournalIdIsAndTopicInAndStatusIs(
                journalService.getJournalId(flatRecords[0].getJournal()), topics, "IN PROGRESS");

        for (FlatRecord f : flatRecords) {
            if (!isNumeric(f.getRecValue()))
                continue;
            for (Objective o : objectives)
                for (Progress p : o.getProgressList()) {
                    if (p.getEntryDateLastChecked() != null &&
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

        String saveIds = saveResults.stream()
                .map(o->o.getId())
                .collect(Collectors.toList())
                .toString();


        return "{\"objectives\":" + saveIds + "}";

    }

    public boolean ownsGoal(UUID journalRef,UUID goalId){
        Optional<Goal> goal= goalRepository.findById(goalId);
        if (goal.isEmpty()) return false;
        return goal.get().getJournal().equals(journalRef);
    }
}
