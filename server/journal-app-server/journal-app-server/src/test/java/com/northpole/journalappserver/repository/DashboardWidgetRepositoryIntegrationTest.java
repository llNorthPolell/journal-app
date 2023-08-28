package com.northpole.journalappserver.repository;

import com.northpole.journalappserver.entity.DashboardWidget;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void save_IntegrationTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        DashboardWidget mockWidget = DashboardWidget.builder()
                .type("pie-chart")
                .journal(1)
                .title("A Pie Chart")
                .position(0)
                .creationTimestamp(LocalDateTime.parse("2022-08-21T04:00:00.000+00:00",formatter))
                .lastUpdated(LocalDateTime.parse("2022-08-21T04:00:00.000+00:00",formatter))
                .build();

        dashboardWidgetRepository.save(mockWidget);
        List<DashboardWidget> results = dashboardWidgetRepository.findAll();
        assertEquals(4,results.size());
    }


    @Test
    @DisplayName("Should return all entries with journalId = 3")
    public void getDashboardWidgetsByJournal_IntegrationTest(){
        List<DashboardWidget> resultList = dashboardWidgetRepository.getDashboardWidgetsByJournal(3);

        assertEquals(2,resultList.size());
    }

}
