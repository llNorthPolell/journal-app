package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.DashboardWidget;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.factory.WidgetPayloadFactory;
import com.northpole.journalappserver.repository.DashboardWidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardWidgetServiceImpl implements DashboardWidgetService{
    private JournalEntryService journalEntryService;

    private JournalEntryRecordService journalEntryRecordService;

    private DashboardWidgetRepository dashboardWidgetRepository;

    @Autowired
    public DashboardWidgetServiceImpl(
            JournalEntryService journalEntryService,
            JournalEntryRecordService journalEntryRecordService,
            DashboardWidgetRepository dashboardWidgetRepository
    ){
        this.journalEntryService=journalEntryService;
        this.journalEntryRecordService=journalEntryRecordService;
        this.dashboardWidgetRepository=dashboardWidgetRepository;
    }

    public List<DashboardWidget> getDashboardWidgetData(int journalId){
        List<DashboardWidget> output =this.dashboardWidgetRepository.getDashboardWidgetsByJournal(journalId);
        List<FlatRecord> recordData = journalEntryRecordService.getDashboardData(journalId);

        WidgetPayloadFactory factory = new WidgetPayloadFactory(journalId,recordData,this.journalEntryService);

        for (DashboardWidget widget : output)
            widget.setPayload(
                factory.getPayload(
                        WidgetPayloadFactory.WidgetType.getWidgetType(widget.getType()),
                        widget.getConfigs()
                )
            );


        return output;
    }
}
