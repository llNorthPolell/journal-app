package com.northpole.journalappserver.controller;

import com.northpole.journalappserver.entity.Goal;
import com.northpole.journalappserver.service.GoalTrackerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class GoalController {

    private GoalTrackerService goalTrackerService;

    @Autowired
    public GoalController(
            GoalTrackerService goalTrackerService
    ){
        this.goalTrackerService=goalTrackerService;
    }

    @PostMapping("/{journalRef}/goals")
    @PreAuthorize("@securityService.ownsJournal(#journalRef)")
    public ResponseEntity<String> createGoal(@PathVariable("journalRef") UUID journalRef,
                                             @Valid @RequestBody Goal payload){
        try {
            Goal output = goalTrackerService.saveGoal(journalRef,payload);
            String message =  "{\"id\":\""+output.getId() +"\"}";
            return new ResponseEntity<>(
                    message,
                    HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{journalRef}/goals")
    @PreAuthorize("@securityService.ownsJournal(#journalRef)")
    public List<Goal> getGoalPreviewsInJournal(
            @PathVariable("journalRef") UUID journalRef,
            @RequestParam("status") Optional<String> status){
        return goalTrackerService.getGoalPreviewsInJournal(journalRef, status);
    }

    @GetMapping("/{journalRef}/goalWithTracking")
    @PreAuthorize("@securityService.ownsJournal(#journalRef)")
    public List<Goal> getGoalsInJournal(
            @PathVariable("journalRef") UUID journalRef,
            @RequestParam("status") Optional<String> status){
        return goalTrackerService.getGoalsWithProgressInJournal(journalRef, status);
    }

    @GetMapping("/{journalRef}/goals/{goalId}")
    @PreAuthorize("@securityService.ownsJournal(#journalRef) && @securityService.ownsGoal(#journalRef,#goalId)")
    public Goal getGoalById(
            @PathVariable("journalRef") UUID journalRef,
            @PathVariable("goalId") UUID goalId
    ){
        return goalTrackerService.getGoalById(goalId);
    }


    @PutMapping("/{journalRef}/goals/{goalId}")
    @PreAuthorize("@securityService.ownsJournal(#journalRef) && @securityService.ownsGoal(#journalRef,#goalId)")
    public ResponseEntity<String> updateGoal(@PathVariable("journalRef") UUID journalRef,
                                             @PathVariable("goalId") UUID goalId,
                                             @Valid @RequestBody Goal payload){
        try {
            Goal output = goalTrackerService.updateGoal(journalRef,goalId,payload);
            String message =  "{\"id\":\""+output.getId() +"\"}";
            return new ResponseEntity<>(
                    message,
                    HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{journalRef}/goals/{goalId}")
    @PreAuthorize("@securityService.ownsJournal(#journalRef) && @securityService.ownsGoal(#journalRef,#goalId)")
    public ResponseEntity<String> deleteGoal(@PathVariable("journalRef") UUID journalRef,
                                             @PathVariable("goalId") UUID goalId){
        try {
            Goal output = goalTrackerService.deleteGoal(goalId);
            String message =  "{\"id\":\""+output.getId() +"\"}";
            return new ResponseEntity<>(
                    message,
                    HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
