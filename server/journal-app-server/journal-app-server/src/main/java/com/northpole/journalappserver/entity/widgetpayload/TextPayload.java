package com.northpole.journalappserver.entity.widgetpayload;

import com.northpole.journalappserver.entity.DashboardWidgetPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TextPayload implements DashboardWidgetPayload {
    private String content;
}
