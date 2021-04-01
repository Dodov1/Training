package com.web.training.service.impl;

import com.web.training.models.entities.Log;
import com.web.training.models.serviceModels.LoginLogServiceModel;
import com.web.training.repositories.LogRepository;
import com.web.training.service.LogService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final ModelMapper modelMapper;

    public LogServiceImpl(LogRepository logRepository, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveLog(String message) {
        Log log = new Log();
        log.setLocalDateTime(LocalDateTime.now());
        log.setMessage(message);
        this.logRepository.saveAndFlush(log);
    }

    @Override
    public List<LoginLogServiceModel> getLog() {
        return this.logRepository.findAll()
                .stream()
                .map(el -> this.modelMapper.map(el, LoginLogServiceModel.class))
                .collect(Collectors.toList());
    }
}
