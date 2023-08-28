package com.northpole.journalappserver.entity.widgetpayload;

import com.northpole.journalappserver.entity.DashboardWidgetPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartPayload implements DashboardWidgetPayload {

    private List<String> labels;
    private List<Dataset> datasets;


}
