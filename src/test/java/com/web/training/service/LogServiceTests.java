package com.web.training.service;

import com.web.training.models.entities.Log;
import com.web.training.models.serviceModels.LoginLogServiceModel;
import com.web.training.repositories.LogRepository;
import com.web.training.service.impl.LogServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class LogServiceTests {

    @Mock
    private LogRepository logRepository;


    private LogService logService;

    private final ModelMapper modelMapper = new ModelMapper();


    private static final Long CORRECT_LOG_ID = 1L;
    private Log testLog;

    @BeforeEach
    public void init() {
        this.testLog = new Log() {{
            setId(CORRECT_LOG_ID);
            setLocalDateTime(LocalDateTime.now());
            setMessage("DEMO");
        }};

        this.logService = new LogServiceImpl(this.logRepository, this.modelMapper);
    }

    /**
     * LogService - saveLog(...) TESTS
     */

    @Test
    void saveLogShouldSaveItCorrectly() {
        Assertions.assertDoesNotThrow(() -> this.logService.saveLog("DEMO MESSAGE"), "Log is not correct");
    }

    /**
     * LogService - getLog(...) TESTS
     */

    @Test
    void getLogShouldReturnCorrectLogsMessage() {
        LoginLogServiceModel model = new LoginLogServiceModel();
        model.setTime(this.testLog.getLocalDateTime());
        model.setMessage(this.testLog.getMessage());
        Mockito.when(this.logRepository.findAll())
                .thenReturn(List.of(this.testLog));
        List<LoginLogServiceModel> actual = this.logService.getLog();
        Assertions.assertEquals(model.getMessage(), actual.get(0).getMessage(), "Log is not correct");
    }

    @Test
    void getLogShouldReturnCorrectLogsTime() {
        LoginLogServiceModel model = new LoginLogServiceModel();
        model.setTime(this.testLog.getLocalDateTime());
        model.setMessage(this.testLog.getMessage());
        Mockito.when(this.logRepository.findAll())
                .thenReturn(List.of(this.testLog));
        List<LoginLogServiceModel> actual = this.logService.getLog();
        Assertions.assertEquals(model.getTime(), actual.get(0).getTime(), "Log is not correct");
    }
}
