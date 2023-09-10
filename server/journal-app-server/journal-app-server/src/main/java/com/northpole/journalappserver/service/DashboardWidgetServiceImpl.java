package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.DashboardWidget;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.WidgetDataConfig;
import com.northpole.journalappserver.factory.WidgetPayloadFactory;
import com.northpole.journalappserver.factory.WidgetType;
import com.northpole.journalappserver.repository.DashboardWidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardWidgetServiceImpl implements DashboardWidgetService{
    private JournalService journalService;

    private JournalEntryService journalEntryService; //placeholder for text widgets

    private JournalEntryRecordService journalEntryRecordService;

    private DashboardWidgetRepository dashboardWidgetRepository;

    private WidgetPayloadFactory widgetPayloadFactory;

    @Autowired
    public DashboardWidgetServiceImpl(
            JournalService journalService,
            JournalEntryService journalEntryService,
            JournalEntryRecordService journalEntryRecordService,
            DashboardWidgetRepository dashboardWidgetRepository,
            WidgetPayloadFactory widgetPayloadFactory
    ){
        this.journalService=journalService;
        this.journalEntryService=journalEntryService;
        this.journalEntryRecordService=journalEntryRecordService;
        this.dashboardWidgetRepository=dashboardWidgetRepository;
        this.widgetPayloadFactory=widgetPayloadFactory;
    }

    @Override
    public DashboardWidget createDashboardWidget(UUID journalRef,DashboardWidget payload){
        LocalDateTime now = LocalDateTime.now();

        payload.setCreationTimestamp(now);
        payload.setLastUpdated(now);
        payload.setJournal(journalService.getJournalId(journalRef));
        payload.setPosition(null); // no need to set position as position is set by trigger in Postgres

        return dashboardWidgetRepository.save(payload);
    }

    public List<DashboardWidget> getDashboardWidgetData(UUID journalRef){
        int journalId=journalService.getJournalId(journalRef);
        List<DashboardWidget> output = dashboardWidgetRepository.getDashboardWidgetsByJournal(journalId);
        List<FlatRecord> recordData = journalEntryRecordService.getDashboardData(journalRef);

        for (DashboardWidget widget : output)
            widget.setPayload(
                    widgetPayloadFactory.getPayload(
                            WidgetType.getWidgetType(widget.getType()),
                            widget.getConfigs(),
                            journalRef,
                            recordData
                    )
            );


        return output;
    }

    @Override
    public DashboardWidget updateDashboardWidget(int widgetId, DashboardWidget payload) {
        LocalDateTime now = LocalDateTime.now();
        Optional<DashboardWidget> findWidgetToUpdate = dashboardWidgetRepository.findById(widgetId);

        if (findWidgetToUpdate.isEmpty()) return null;

        DashboardWidget widgetToUpdate = findWidgetToUpdate.get();

        widgetToUpdate.setLastUpdated(now);
        widgetToUpdate.setTitle(payload.getTitle());
        widgetToUpdate.setType(payload.getType());
        widgetToUpdate.setPosition(payload.getPosition());

        List<WidgetDataConfig> payloadConfigs = payload.getConfigs();
        Set<Integer> payloadConfigIds = payloadConfigs
                .stream()
                .map(c->c.getId())
                .collect(Collectors.toSet());

        List<WidgetDataConfig> configsToUpdate = widgetToUpdate.getConfigs();

        List<WidgetDataConfig> toDelete = configsToUpdate
                .stream()
                .filter(c->!payloadConfigIds.contains(c.getId()))
                .collect(Collectors.toList());


        configsToUpdate.clear();
        configsToUpdate.addAll(payloadConfigs);
        configsToUpdate.removeAll(toDelete);
        widgetToUpdate.setPayload(null);

        return dashboardWidgetRepository.save(widgetToUpdate);
    }

    @Override
    public DashboardWidget deleteDashboardWidget(UUID journalRef, int widgetId){
        Optional<DashboardWidget> findWidgetToDelete = dashboardWidgetRepository.findById(widgetId);
        if(findWidgetToDelete.isEmpty()) return null;
        DashboardWidget widgetToDelete = findWidgetToDelete.get();
        dashboardWidgetRepository.delete(widgetToDelete);
        return widgetToDelete;
    }

    @Override
    public boolean ownsWidget(UUID journalRef, int widgetId) {
        Optional<DashboardWidget> widget = dashboardWidgetRepository.findById(widgetId);
        if (widget.isEmpty()) return false;
        return widget.get().getJournal().equals(journalService.getJournalId(journalRef));
    }
}
