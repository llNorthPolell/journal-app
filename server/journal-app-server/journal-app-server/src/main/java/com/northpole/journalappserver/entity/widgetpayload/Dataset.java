package com.northpole.journalappserver.entity.widgetpayload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dataset {
    private String label;
    private List<String> data;
    private String borderColor;
    private String backgroundColor;
}
