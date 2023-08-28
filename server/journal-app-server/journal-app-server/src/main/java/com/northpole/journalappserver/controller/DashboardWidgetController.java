package com.northpole.journalappserver.controller;

import com.northpole.journalappserver.entity.DashboardWidget;
import com.northpole.journalappserver.entity.JournalEntry;
import com.northpole.journalappserver.entity.requestbody.DashboardRequestBody;
import com.northpole.journalappserver.service.DashboardWidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DashboardWidgetController {

    private DashboardWidgetService dashboardWidgetService;


    @Autowired
    public DashboardWidgetController(
            DashboardWidgetService dashboardWidgetService
    ){
        this.dashboardWidgetService=dashboardWidgetService;
    }


    @PostMapping("/dashboard")
    public List<DashboardWidget> getDashboard(@RequestBody DashboardRequestBody payload){
        return dashboardWidgetService.getDashboardWidgetData(payload.getJournal());
    }
}
