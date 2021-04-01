package com.web.training.service;

import com.web.training.models.serviceModels.LoginLogServiceModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LogService {

    void saveLog(String message);

    List<LoginLogServiceModel> getLog();
}
