package com.northpole.journalappserver.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
public class SecurityServiceImpl implements SecurityService{

    private JournalService journalService;
    private JournalEntryService journalEntryService;
    private DashboardWidgetService dashboardWidgetService;
    private GoalTrackerService goalTrackerService;

    private Authentication authentication;

    public SecurityServiceImpl(
            JournalService journalService,
            JournalEntryService journalEntryService,
            DashboardWidgetService dashboardWidgetService,
            GoalTrackerService goalTrackerService
    ){
        this.journalService=journalService;
        this.journalEntryService=journalEntryService;
        this.dashboardWidgetService=dashboardWidgetService;
        this.goalTrackerService=goalTrackerService;
    }

    @Override
    public boolean ownsJournal(UUID journalRef){
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        return journalService.ownsJournal(authentication.getName(),journalRef);
    }

    @Override
    public boolean ownsJournalEntry(UUID journalRef, UUID journalEntryId) {
        return journalEntryService.ownsJournalEntry(journalRef, journalEntryId);
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
