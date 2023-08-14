package com.northpole.journalappserver.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntryRecordDataSet {

    private List<DateAndValue> x;

    private List<DateAndValue> y;
}
