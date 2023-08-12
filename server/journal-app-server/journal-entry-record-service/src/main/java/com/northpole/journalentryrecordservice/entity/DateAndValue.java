package com.northpole.journalentryrecordservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateAndValue {
    private LocalDateTime dateOfEntry;

    private String recValue;
}
