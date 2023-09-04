package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.Goal;

import java.util.List;


public interface GoalTrackerService {
    GeneralResponseBody saveGoal(Goal payload);
    List<Goal> getGoalsWithProgressInJournal(int journalId);
    GeneralResponseBody updateProgress(String message);
}
