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

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GoalTrackerServiceImpl implements GoalTrackerService {

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
    ) {
        this.goalRepository = goalRepository;
        this.objectiveRepository = objectiveRepository;
        this.journalService = journalService;
        this.objectMapper = objectMapper;
    }

    private boolean isNumeric(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private List<Objective> createObjectives(UUID journalRef, UUID goalId, List<Objective> payload){
        LocalDateTime now = LocalDateTime.now();
        String defaultStatus = JournalAppStatus.IN_PROGRESS.value();

        for (Objective o : payload) {
            o.setGoalId(goalId);
            o.setJournalId(journalService.getJournalId(journalRef));
            o.setCreationTimestamp(now);
            o.setLastUpdated(now);
            o.setStatus(defaultStatus);

            if (o.getCompletionCriteria() == null)
                o.setCompletionCriteria("AND");

            for (Progress p : o.getProgressList())
                if (p.getCurrentValue() == null)
                    p.setCurrentValue(0d);
        }
        return objectiveRepository.saveAll(payload);
    }

    @Override
    @Transactional
    public Goal saveGoal(UUID journalRef, Goal payload) {
        UUID saveId = UUID.randomUUID();
        List<Objective> objectives = payload.getObjectives();
        String defaultStatus = JournalAppStatus.IN_PROGRESS.value();

        payload.setId(saveId);
        payload.setJournal(journalRef);
        payload.setStatus(defaultStatus);
        payload.setObjectives(null);    // strip objectives from MongoDB as not needed and takes up space

        Goal saveGoal = goalRepository.save(payload);
        List<Objective> saveObjectives = createObjectives(journalRef, saveId, objectives);

        saveGoal.setObjectives(saveObjectives);

        return saveGoal;
    }

    @Override
    public Goal getGoalById(UUID id) {
        Optional<Goal> goal = goalRepository.findById(id);
        List<Objective> objectives;

        if (goal.isEmpty()) return null;

        Goal output = goal.get();
        objectives = objectiveRepository.findAllByGoalId(id);

        output.setObjectives(objectives);

        return output;
    }

    @Override
    public List<Goal> getGoalPreviewsInJournal(UUID journalRef, Optional<String> status) {
        if (status.isEmpty())
            return goalRepository.findAllByJournal(journalRef);

        return goalRepository.findByStatusInJournal(
                journalRef, status.get());
    }

    private void packageGoals(List<Goal> goals, List<Objective> objectives) {
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

    public void checkAndUpdateCompletedGoals(UUID journalRef){
        List<Goal> goals = goalRepository.findByStatusInJournal(journalRef,
                JournalAppStatus.IN_PROGRESS.value());
        Set<UUID> goalIds = goals.stream()
                .map(g->g.getId())
                .collect(Collectors.toSet());

        List<Objective> objectives = objectiveRepository.findAllByGoalIdIn(goalIds);

        packageGoals(goals,objectives);

        for (Goal goal : goals){
            boolean completed = true;
            for (Objective o : objectives) {
                if (!o.getGoalId().equals(goal.getId()))
                    continue;
                if (!o.getStatus().equals(JournalAppStatus.COMPLETE.value())) {
                    completed = false;
                    break;
                }
            }

            if (completed){
                goal.setStatus(JournalAppStatus.COMPLETE.value());
                updateGoal(journalRef,goal.getId(),goal);
            }
        }
    }

    @Override
    @Transactional
    public List<Goal> getGoalsWithProgressInJournal(UUID journalRef, Optional<String> status) {
        List<Goal> goals = (status.isEmpty())?goalRepository.findAllByJournal(journalRef):
                goalRepository.findByStatusInJournal(journalRef, status.get());
        List<Objective> objectiveList = objectiveRepository.findAllByJournalId(
                journalService.getJournalId(journalRef));
        packageGoals(goals, objectiveList);

        return goals;
    }

    @Override
    @Transactional
    public Goal updateGoal(UUID journalRef, UUID goalId, Goal payload) {
        LocalDateTime now = LocalDateTime.now();
        Optional<Goal> findGoalToUpdate = goalRepository.findById(goalId);

        if (findGoalToUpdate.isEmpty()) return null;

        List<Objective> objectivesToUpdate = objectiveRepository.findAllByGoalId(goalId);
        List<Objective> payloadObjectives = payload.getObjectives();

        Goal goalToUpdate=findGoalToUpdate.get();

        goalToUpdate.setStatus(payload.getStatus());
        goalToUpdate.setAssumptions(payload.getAssumptions());
        goalToUpdate.setDescription(payload.getDescription());
        goalToUpdate.setGains(payload.getGains());
        goalToUpdate.setIcon(payload.getIcon());

        Map<Integer,Objective> payloadObjectiveMap = new HashMap<>();
        List<Objective> objectivesToCreate = new ArrayList<>();
        for (Objective o: payloadObjectives){
            if (o.getId()!=null) {
                payloadObjectiveMap.put(o.getId(), o);
                continue;
            }
            objectivesToCreate.add(o);
        }

        List<Objective> objectivesToDelete = new ArrayList<>();
        for (Objective o: objectivesToUpdate){
            Integer id = o.getId();
            if (id ==null) continue;
            if (!payloadObjectiveMap.containsKey(id)) {
                objectivesToDelete.add(o);
                continue;
            }
            Objective payloadObjective=payloadObjectiveMap.get(id);
            o.setDescription(payloadObjective.getDescription());
            o.setStatus(payloadObjective.getStatus());
            o.setDeadline(payloadObjective.getDeadline());
            o.setCompletionCriteria(payloadObjective.getCompletionCriteria());
            o.setLastUpdated(now);
            o.setTopic(payloadObjective.getTopic());

            List<Progress> payloadProgressList = payloadObjective.getProgressList();
            Set<Integer> payloadProgressIds = payloadProgressList
                    .stream()
                    .map(p->p.getId())
                    .collect(Collectors.toSet());

            List<Progress> progressToUpdate = o.getProgressList();
            List<Progress> progressToDelete = progressToUpdate
                    .stream()
                    .filter(p->!payloadProgressIds.contains(p.getId()))
                    .collect(Collectors.toList());

            progressToUpdate.clear();
            progressToUpdate.addAll(payloadProgressList);
            progressToUpdate.removeAll(progressToDelete);
        }

        objectivesToUpdate.removeAll(objectivesToDelete);

        objectiveRepository.deleteAll(objectivesToDelete);
        objectiveRepository.saveAll(objectivesToUpdate);
        List<Objective> saveObjectives = createObjectives(journalRef,
                goalId, objectivesToCreate);

        Goal saveResult = goalRepository.save(goalToUpdate);

        return saveResult;
    }

    @Override
    @Transactional
    public Goal deleteGoal(UUID goalId) {
        Optional<Goal> findGoalToDelete = goalRepository.findById(goalId);

        if(findGoalToDelete.isEmpty()) return null;

        Goal goalToDelete=findGoalToDelete.get();
        List<Objective> objectivesToDelete = objectiveRepository.findAllByGoalId(goalId);

        goalRepository.delete(goalToDelete);
        objectiveRepository.deleteAll(objectivesToDelete);

        goalToDelete.setObjectives(objectivesToDelete);

        return goalToDelete;
    }


    private FlatRecord[] extractFlatRecords(String message) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(message);
        JsonNode flatRecordsJson = root.get("flatRecords");
        return objectMapper.convertValue(flatRecordsJson, FlatRecord[].class);
    }

    @Override
    public String updateProgress(List<FlatRecord> payload) {
        UUID journalRef = payload.get(0).getJournal();
        int journalId = journalService.getJournalId(journalRef);

        if (payload.isEmpty()) return "{\"message\":\"Payload was empty. No progress was updated.\"}";

        Set<String> topics = payload.stream()
                .map(f -> f.getTopic())
                .collect(Collectors.toSet());

        // find list of objectives with topic and recKey
        List<Objective> objectives = objectiveRepository.findAllByJournalIdIsAndTopicInAndStatusIs(
                journalId, topics, "IN PROGRESS");

        for (FlatRecord f : payload) {
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

        checkAndUpdateCompletedGoals(journalRef);

        String saveIds = saveResults.stream()
                .map(o -> o.getId())
                .collect(Collectors.toList())
                .toString();

        return "{\"objectives\":" + saveIds + "}";

    }

    public boolean ownsGoal(UUID journalRef, UUID goalId) {
        Optional<Goal> goal = goalRepository.findById(goalId);
        if (goal.isEmpty()) return false;
        return goal.get().getJournal().equals(journalRef);
    }
}
