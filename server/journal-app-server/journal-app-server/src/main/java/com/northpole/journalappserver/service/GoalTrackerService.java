package com.northpole.journalappserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.northpole.journalappserver.entity.Goal;

import java.util.List;
import java.util.UUID;


public interface GoalTrackerService {
    Goal saveGoal(UUID journalRef,Goal payload);
    Goal getGoalById(UUID id);
    List<Goal> getGoalPreviewsInJournal(UUID journalRef);
    List<Goal> getGoalsWithProgressInJournal(UUID journalRef);
    List<Goal> getCompletedGoals(UUID journalRef);
    Goal updateGoal(Goal payload);
    Goal deleteGoal(Goal goalId);
    String updateProgress(String message) throws JsonProcessingException;
    boolean ownsGoal(UUID journalRef,UUID goalId);
}
