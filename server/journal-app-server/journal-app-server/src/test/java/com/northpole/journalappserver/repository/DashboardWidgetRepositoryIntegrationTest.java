package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.DashboardWidget;
import com.northpole.journalappserver.entity.WidgetDataConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class DashboardWidgetRepositoryIntegrationTest {
    @Container
    private static PostgreSQLContainer postgresDB = (PostgreSQLContainer) new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
            .withInitScript("testdbinitsql/test_db.sql");

    private DashboardWidgetRepository dashboardWidgetRepository;

    @Autowired
    public DashboardWidgetRepositoryIntegrationTest(
            DashboardWidgetRepository dashboardWidgetRepository
    ){
        this.dashboardWidgetRepository=dashboardWidgetRepository;
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username",postgresDB::getUsername);
        registry.add("spring.datasource.password",postgresDB::getPassword);
    }


    @Test
    @DisplayName("Should save new dashboard widget configuration")
    public void createWidget_IntegrationTest(){
        final long count = dashboardWidgetRepository.count();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");

        List<WidgetDataConfig> mockConfigList = new ArrayList<>();
        mockConfigList.add(
                WidgetDataConfig.builder()
                        .label("test")
                        .type("x")
                        .rule("a")
                        .color("#123456")
                        .build()
        );

        DashboardWidget mockWidget = DashboardWidget.builder()
                .type("pie-chart")
                .journal(1)
                .title("A Pie Chart")
                .position(0)
                .creationTimestamp(LocalDateTime.parse("2022-08-21T04:00:00.000+00:00",formatter))
                .lastUpdated(LocalDateTime.parse("2022-08-21T04:00:00.000+00:00",formatter))
                .configs(mockConfigList)
                .build();

        DashboardWidget saveResult = dashboardWidgetRepository.save(mockWidget);

        long newCount = dashboardWidgetRepository.count();
        assertEquals(count+1,newCount);

        Optional<DashboardWidget> findNewWidgetResult = dashboardWidgetRepository.findById(saveResult.getId());

        assertTrue(findNewWidgetResult.isPresent());

        DashboardWidget newWidget = findNewWidgetResult.get();

        assertEquals(mockWidget.getType(), newWidget.getType());
        assertEquals(mockWidget.getJournal(), newWidget.getJournal());
        assertEquals(mockWidget.getTitle(), newWidget.getTitle());
        assertNotEquals(mockWidget.getPosition(), newWidget.getPosition());
        assertNull(newWidget.getPayload());

        List<WidgetDataConfig> newConfigList= mockWidget.getConfigs();
        assertNotNull(newConfigList);
        assertEquals(mockConfigList.size(),newConfigList.size());

        WidgetDataConfig newConfig = newConfigList.get(0);
        WidgetDataConfig mockConfig = mockConfigList.get(0);

        assertEquals(mockConfig.getLabel(),newConfig.getLabel());
        assertEquals(mockConfig.getType(),newConfig.getType());
        assertEquals(mockConfig.getColor(),newConfig.getColor());
    }


    @Test
    @DisplayName("Should return all entries with journalId = 3")
    public void getDashboardWidgetsByJournal_IntegrationTest(){
        List<DashboardWidget> resultList = dashboardWidgetRepository.getDashboardWidgetsByJournal(3);

        assertEquals(2,resultList.size());
    }

    @Test
    @DisplayName("Should update dashboard widget configuration")
    public void updateWidget_IntegrationTest(){
        final int mockUpdateId = 2;
        final long count = dashboardWidgetRepository.count();
        LocalDateTime now = LocalDateTime.now();

        Optional<DashboardWidget> findWidgetToUpdate = dashboardWidgetRepository.findById(mockUpdateId);
        assertTrue(findWidgetToUpdate.isPresent());
        DashboardWidget widgetToUpdate = findWidgetToUpdate.get();

        List<WidgetDataConfig> mockConfigList = findWidgetToUpdate.get().getConfigs();
        assertTrue(!mockConfigList.isEmpty());

        List<WidgetDataConfig> filteredMockConfig = mockConfigList.stream()
                .filter(x->x.getLabel().equals("Current"))
                .collect(Collectors.toList());

        assertEquals(1,filteredMockConfig.size());
        WidgetDataConfig sampleMockConfig = filteredMockConfig.get(0);

        sampleMockConfig.setColor("#abcdef");
        sampleMockConfig.setLabel("updated this config");
        sampleMockConfig.setRule("updated");
        sampleMockConfig.setType("y");

        DashboardWidget mockPayload = DashboardWidget.builder()
                .title("updated")
                .type("line-graph")
                .lastUpdated(now)
                .configs(mockConfigList)
                .build();

        widgetToUpdate.setTitle(mockPayload.getTitle());
        widgetToUpdate.setType(mockPayload.getType());
        widgetToUpdate.setLastUpdated(mockPayload.getLastUpdated());
        widgetToUpdate.setConfigs(mockPayload.getConfigs());

        DashboardWidget updateResult = dashboardWidgetRepository.save(widgetToUpdate);

        long newCount = dashboardWidgetRepository.count();
        assertEquals(count,newCount);

        Optional<DashboardWidget> findUpdatedWidget = dashboardWidgetRepository.findById(mockUpdateId);
        assertTrue(findUpdatedWidget.isPresent());

        DashboardWidget updatedWidget = findUpdatedWidget.get();

        assertEquals(mockPayload.getTitle(),updatedWidget.getTitle());
        assertEquals(mockPayload.getType(),updatedWidget.getType());

        List<WidgetDataConfig> updatedConfigList = updatedWidget.getConfigs();

        assertEquals(mockConfigList.size(),updatedConfigList.size());

        List<WidgetDataConfig> filteredUpdatedConfig = mockConfigList.stream()
                .filter(x->x.getLabel().equals("updated this config"))
                .collect(Collectors.toList());

        assertEquals(1,filteredUpdatedConfig.size());
        WidgetDataConfig sampleUpdatedConfig = filteredUpdatedConfig.get(0);

        assertEquals(sampleMockConfig.getLabel(),sampleUpdatedConfig.getLabel());
        assertEquals(sampleMockConfig.getColor(),sampleUpdatedConfig.getColor());
        assertEquals(sampleMockConfig.getRule(),sampleUpdatedConfig.getRule());
    }

    @Test
    @DisplayName("Should delete dashboard widget")
    public void deleteWidget_IntegrationTest(){
        final int mockDeleteId = 1;
        final long count = dashboardWidgetRepository.count();

        Optional<DashboardWidget> widgetToDelete = dashboardWidgetRepository.findById(mockDeleteId);

        assertTrue(widgetToDelete.isPresent());

        dashboardWidgetRepository.delete(widgetToDelete.get());

        long newCount = dashboardWidgetRepository.count();
        assertEquals(count-1,newCount);

        Optional<DashboardWidget> deletedWidget = dashboardWidgetRepository.findById(mockDeleteId);

        assertTrue(deletedWidget.isEmpty());
    }
}
