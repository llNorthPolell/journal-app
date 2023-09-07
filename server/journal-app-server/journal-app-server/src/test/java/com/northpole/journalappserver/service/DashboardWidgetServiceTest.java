package com.northpole.journalappserver.service;

import com.northpole.journalappserver.entity.DashboardWidget;
import com.northpole.journalappserver.entity.FlatRecord;
import com.northpole.journalappserver.entity.WidgetDataConfig;
import com.northpole.journalappserver.entity.widgetpayload.ChartPayload;
import com.northpole.journalappserver.repository.DashboardWidgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DashboardWidgetServiceTest {
    @Mock
    private JournalService journalService;

    @Mock
    private JournalEntryRecordService journalEntryRecordService;

    @Mock
    private DashboardWidgetRepository dashboardWidgetRepository;

    @InjectMocks
    private DashboardWidgetService dashboardWidgetService;

    private final UUID MOCK_JOURNAL_REF=UUID.fromString("e958ac56-2f12-4d35-ba8e-979aca28b4a6");

    @Autowired
    public DashboardWidgetServiceTest(
            JournalService journalService,
            JournalEntryRecordService journalEntryRecordService,
            DashboardWidgetService dashboardWidgetService,
            DashboardWidgetRepository dashboardWidgetRepository
    ){
        this.journalService=journalService;
        this.journalEntryRecordService=journalEntryRecordService;
        this.dashboardWidgetService=dashboardWidgetService;
        this.dashboardWidgetRepository=dashboardWidgetRepository;
    }

    @BeforeEach
    public void setupBeforeEachTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        List<DashboardWidget> mockDashboardWidgetRepoResult = new ArrayList<>();
        List<WidgetDataConfig> mockChartConfig = new ArrayList<>();
        List<FlatRecord> mockFlatRecords = new ArrayList<>();

        mockChartConfig.add(
                WidgetDataConfig.builder()
                        .id(1)
                        .type("x")
                        .label("Date")
                        .rule("dateOfEntry")
                        .build()
        );
        mockChartConfig.add(
                WidgetDataConfig.builder()
                        .id(1)
                        .type("y")
                        .label("Current")
                        .rule("a")
                        .color("#B72714")
                        .build()
        );
        mockChartConfig.add(
                WidgetDataConfig.builder()
                        .id(1)
                        .type("y")
                        .label("Target")
                        .rule("targetA")
                        .color("#1E54BA")
                        .build()
        );

        mockDashboardWidgetRepoResult.add(
          DashboardWidget.builder()
                  .id(1)
                  .journal(3)
                  .creationTimestamp(LocalDateTime.parse("2023-08-24 17:51:10.017457",formatter))
                  .lastUpdated(LocalDateTime.parse("2023-08-24 17:51:10.017457",formatter))
                  .type("last-entry")
                  .position(0)
                  .title("Last Entry")
                  .configs(null)
                  .build()
        );

        mockDashboardWidgetRepoResult.add(
                DashboardWidget.builder()
                        .id(2)
                        .journal(3)
                        .creationTimestamp(LocalDateTime.parse("2023-08-24 17:53:59.438958",formatter))
                        .lastUpdated(LocalDateTime.parse("2023-08-24 17:53:59.438958",formatter))
                        .type("line-graph")
                        .position(1)
                        .title("Test")
                        .configs(mockChartConfig)
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("My first topic")
                        .recKey("a")
                        .recValue("6")
                        .dateOfEntry(LocalDateTime.parse("2022-08-21T04:00:00.000+00:00",formatter2))
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("6")
                        .dateOfEntry(LocalDateTime.parse("2022-08-21T04:00:00.000+00:00",formatter2))
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("3")
                        .dateOfEntry(LocalDateTime.parse("2022-08-20T04:00:00.000+00:00",formatter2))
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("My first topic")
                        .recKey("a")
                        .recValue("1")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter2))
                        .build()
        );

        mockFlatRecords.add(
                FlatRecord.builder()
                        .id(UUID.randomUUID())
                        .journal(MOCK_JOURNAL_REF)
                        .topic("My first topic")
                        .recKey("targetA")
                        .recValue("2")
                        .dateOfEntry(LocalDateTime.parse("2022-08-19T04:00:00.000+00:00",formatter2))
                        .build()
        );

        when(journalService.getJournalId(any(UUID.class))).thenReturn(3);
        when(dashboardWidgetRepository.getDashboardWidgetsByJournal(3))
                .thenReturn(mockDashboardWidgetRepoResult);

        when(journalEntryRecordService.getDashboardData(MOCK_JOURNAL_REF))
                .thenReturn(mockFlatRecords);
    }

    @Test
    @DisplayName("Should return dataset for widgets")
    public void get_widget_data_UnitTest(){
        List<DashboardWidget> resultList = dashboardWidgetService.getDashboardWidgetData(MOCK_JOURNAL_REF);

        assertEquals(2,resultList.size());
    }

    @Test
    @DisplayName("Should return dataset for chart-type widgets, sorted by dateOfEntry (desc), topic(asc) and recKey(asc)")
    public void get_chart_data_UnitTest(){
        List<DashboardWidget> resultList = dashboardWidgetService.getDashboardWidgetData(MOCK_JOURNAL_REF);

        assertEquals(2,resultList.size());

        DashboardWidget resultLineGraph = resultList.get(1);

        assertNotNull(resultLineGraph.getPayload());
        assertTrue(resultLineGraph.getPayload() instanceof ChartPayload);

        ChartPayload actualPayload = (ChartPayload) resultLineGraph.getPayload();
        assertNotNull(actualPayload.getLabels());

        List<String> actualLabels = actualPayload.getLabels();
        List<String> expectedLabels = new ArrayList<>();

        expectedLabels.add("2022-08-21T04:00");
        expectedLabels.add("2022-08-20T04:00");
        expectedLabels.add("2022-08-19T04:00");

        assertEquals(expectedLabels,actualLabels);

        assertEquals(2,actualPayload.getDatasets().size());
        List<String> expectedA =  new ArrayList<>();
        List<String> actualA = actualPayload.getDatasets().get(0).getData();

        expectedA.add("6");
        expectedA.add(null);
        expectedA.add("1");

        assertEquals(expectedA,actualA);

        List<String> expectedTargetA = new ArrayList<>();
        List<String> actualTargetA = actualPayload.getDatasets().get(1).getData();

        expectedTargetA.add("6");
        expectedTargetA.add("3");
        expectedTargetA.add("2");

        assertEquals(expectedTargetA,actualTargetA);
    }
}
