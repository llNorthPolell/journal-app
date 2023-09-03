package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.Goal;



public interface GoalTrackerService {
    GeneralResponseBody saveGoal(Goal payload);
    GeneralResponseBody updateProgress(String message);
}
