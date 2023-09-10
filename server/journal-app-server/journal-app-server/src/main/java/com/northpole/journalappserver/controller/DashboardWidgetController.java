package com.northpole.journalappserver.controller;

import com.northpole.journalappserver.entity.DashboardWidget;
import com.northpole.journalappserver.service.DashboardWidgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class DashboardWidgetController {

    private DashboardWidgetService dashboardWidgetService;


    @Autowired
    public DashboardWidgetController(
            DashboardWidgetService dashboardWidgetService
    ){
        this.dashboardWidgetService=dashboardWidgetService;
    }

    @PostMapping("/{journalRef}/dashboard")
    @PreAuthorize("@securityService.ownsJournal(#journalRef)")
    public ResponseEntity<String> saveDashboard(@PathVariable("journalRef") UUID journalRef,
                                                        @RequestBody @Valid DashboardWidget payload){
        try {
            DashboardWidget saveResult = dashboardWidgetService.createDashboardWidget(journalRef, payload);

            String output = "{\"id\":" + saveResult.getId() + "}";

            return new ResponseEntity<>(output, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{journalRef}/dashboard")
    @PreAuthorize("@securityService.ownsJournal(#journalRef)")
    public List<DashboardWidget> getDashboard(
            @PathVariable("journalRef") UUID journalRef
    ){
        return dashboardWidgetService.getDashboardWidgetData(journalRef);
    }

    @PutMapping("/{journalRef}/dashboard/{widgetId}")
    @PreAuthorize("@securityService.ownsJournal(#journalRef) && " +
            "@securityService.ownsDashboardWidget(#journalRef, #widgetId)")
    public ResponseEntity<String> updateDashboardWidget(
            @PathVariable("journalRef") UUID journalRef,
            @PathVariable("widgetId") Integer widgetId,
            @Valid @RequestBody DashboardWidget payload
    ){
        try {
            DashboardWidget updateResult = dashboardWidgetService.updateDashboardWidget(widgetId, payload);

            String output = "{\"id\":" + updateResult.getId() + "}";

            return new ResponseEntity<>(output, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{journalRef}/dashboard/{widgetId}")
    @PreAuthorize("@securityService.ownsJournal(#journalRef) && " +
            "@securityService.ownsDashboardWidget(#journalRef, #widgetId)")
    public ResponseEntity<String> deleteDashboardWidget(
            @PathVariable("journalRef") UUID journalRef,
            @PathVariable("widgetId") Integer widgetId
    ){
        try {
            DashboardWidget deleteResult = dashboardWidgetService.deleteDashboardWidget(journalRef, widgetId);

            String output = "{\"id\":" + deleteResult.getId() + "}";

            return new ResponseEntity<>(output, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
