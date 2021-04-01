package com.web.training.models.viewModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoginLogViewModel {

    private String message;
    private LocalDateTime time;

}
