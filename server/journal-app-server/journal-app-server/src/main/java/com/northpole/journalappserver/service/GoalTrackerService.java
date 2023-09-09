package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.Goal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface GoalTrackerService {
    Goal saveGoal(UUID journalRef,Goal payload);
    Goal getGoalById(UUID id);
    List<Goal> getGoalPreviewsInJournal(UUID journalRef, Optional<String> status);
    List<Goal> getGoalsWithProgressInJournal(UUID journalRef, Optional<String> status);
    Goal updateGoal(UUID journalRef, UUID goalId, Goal payload);
    Goal deleteGoal(UUID goalId);
    String updateProgress(List<FlatRecord> payload);
    boolean ownsGoal(UUID journalRef,UUID goalId);
    void checkAndUpdateCompletedGoals(UUID journalRef);
}
