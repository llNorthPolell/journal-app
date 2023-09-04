package com.northpole.journalappserver.controller;

import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.entity.Goal;
import com.northpole.journalappserver.service.GoalTrackerService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GoalController {

    private GoalTrackerService goalTrackerService;

    @Autowired
    public GoalController(
            GoalTrackerService goalTrackerService
    ){
        this.goalTrackerService=goalTrackerService;
    }

    @PostMapping("/goal")
    public ResponseEntity<GeneralResponseBody> createGoal(@Valid @RequestBody Goal payload){
        GeneralResponseBody output = goalTrackerService.saveGoal(payload);
        return new ResponseEntity<GeneralResponseBody>(output,HttpStatus.valueOf(output.getStatus()));
    }

    @GetMapping("/goal")
    public List<Goal> getGoalPreviewsInJournal(int journalId){
        return goalTrackerService.getGoalPreviewsInJournal(journalId);
    }

    @GetMapping("/goalWithTracking")
    public List<Goal> getGoalsInJournal(int journalId){
        return goalTrackerService.getGoalsWithProgressInJournal(journalId);
    }

}
