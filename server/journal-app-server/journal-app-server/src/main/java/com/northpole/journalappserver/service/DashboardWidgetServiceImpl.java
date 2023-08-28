package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.DashboardWidget;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.factory.WidgetPayloadFactory;
import com.northpole.journalappserver.factory.WidgetType;
import com.northpole.journalappserver.repository.DashboardWidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardWidgetServiceImpl implements DashboardWidgetService{
    private JournalEntryService journalEntryService;

    private JournalEntryRecordService journalEntryRecordService;

    private DashboardWidgetRepository dashboardWidgetRepository;

    private WidgetPayloadFactory widgetPayloadFactory;

    @Autowired
    public DashboardWidgetServiceImpl(
            JournalEntryService journalEntryService,
            JournalEntryRecordService journalEntryRecordService,
            DashboardWidgetRepository dashboardWidgetRepository,
            WidgetPayloadFactory widgetPayloadFactory
    ){
        this.journalEntryService=journalEntryService;
        this.journalEntryRecordService=journalEntryRecordService;
        this.dashboardWidgetRepository=dashboardWidgetRepository;
        this.widgetPayloadFactory=widgetPayloadFactory;
    }

    public List<DashboardWidget> getDashboardWidgetData(int journalId){
        List<DashboardWidget> output =this.dashboardWidgetRepository.getDashboardWidgetsByJournal(journalId);
        List<FlatRecord> recordData = journalEntryRecordService.getDashboardData(journalId);

        for (DashboardWidget widget : output)
            widget.setPayload(
                    widgetPayloadFactory.getPayload(
                            WidgetType.getWidgetType(widget.getType()),
                            widget.getConfigs(),
                            journalId,
                            recordData
                    )
            );


        return output;
    }


    public GeneralResponseBody createDashboardWidget(DashboardWidget payload){
        int saveId;
        LocalDateTime now = LocalDateTime.now();
        payload.setCreationTimestamp(now);
        payload.setLastUpdated(now);
        // no need to set position as position is set by trigger in Postgres

        try {
            DashboardWidget saveResult = dashboardWidgetRepository.save(payload);
            saveId = saveResult.getId();

            return GeneralResponseBody.builder()
                    .status(200)
                    .message("{\"id\":\"" + saveId + "\"}")
                    .timeStamp(System.currentTimeMillis())
                    .build();
        }
        catch(Exception e){
            System.out.println(e.getStackTrace());
            return GeneralResponseBody.builder()
                    .status(500)
                    .message(e.getStackTrace().toString())
                    .timeStamp(System.currentTimeMillis())
                    .build();
        }

    }
}
