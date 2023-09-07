package com.northpole.journalappserver.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
public class SecurityServiceImpl implements SecurityService{

    private JournalService journalService;
    private DashboardWidgetService dashboardWidgetService;
    private GoalTrackerService goalTrackerService;

    private Authentication authentication;

    public SecurityServiceImpl(
            JournalService journalService,
            DashboardWidgetService dashboardWidgetService,
            GoalTrackerService goalTrackerService
    ){
        this.journalService=journalService;
        this.dashboardWidgetService=dashboardWidgetService;
        this.goalTrackerService=goalTrackerService;
    }

    @Override
    public boolean ownsJournal(UUID journalRef){
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        return journalService.ownsJournal(authentication.getName(),journalRef);
    }

    @Override
    public boolean ownsDashboardWidget(UUID journalRef, int widgetId) {
        return dashboardWidgetService.ownsWidget(journalRef,widgetId);
    }

    @Override
    public boolean ownsGoal(UUID journalRef, UUID goalId){
        return goalTrackerService.ownsGoal(journalRef, goalId);
    }
}
