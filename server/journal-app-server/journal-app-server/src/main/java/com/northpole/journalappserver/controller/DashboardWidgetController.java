package com.northpole.journalappserver.controller;

import com.northpole.journalappserver.entity.DashboardWidget;
import com.northpole.journalappserver.entity.GeneralResponseBody;
import com.northpole.journalappserver.service.DashboardWidgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/dashboard")
    public List<DashboardWidget> getDashboard(
            @RequestHeader(name="journalId") int journalId
    ){
        return dashboardWidgetService.getDashboardWidgetData(journalId);
    }

    @PostMapping("/dashboard")
    public GeneralResponseBody createDashboardWidget(
            @RequestBody @Valid DashboardWidget payload
    ){
        return dashboardWidgetService.createDashboardWidget(payload);
    }
}
