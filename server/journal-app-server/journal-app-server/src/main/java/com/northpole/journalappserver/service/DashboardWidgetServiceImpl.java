package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.DashboardWidget;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.factory.WidgetPayloadFactory;
import com.northpole.journalappserver.factory.WidgetType;
import com.northpole.journalappserver.repository.DashboardWidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public DashboardWidget updateDashboardWidget(UUID journalRef,DashboardWidget payload) {
        // check if user owns widget
        LocalDateTime now = LocalDateTime.now();

        payload.setLastUpdated(now);
        payload.setJournal(journalService.getJournalId(journalRef));

        return dashboardWidgetRepository.save(payload);
    }

    @Override
    public boolean ownsWidget(UUID journalRef, int widgetId) {
        Optional<DashboardWidget> widget = dashboardWidgetRepository.findById(widgetId);
        if (widget.isEmpty()) return false;
        return widget.get().getJournal().equals(journalService.getJournalId(journalRef));
    }
}
