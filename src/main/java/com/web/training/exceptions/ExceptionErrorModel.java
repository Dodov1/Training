package com.web.training.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExceptionErrorModel {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    private String message;

    private ExceptionErrorModel() {
        timestamp = LocalDateTime.now();
    }

    public ExceptionErrorModel(HttpStatus status) {
        this();
        this.status = status;
    }

    public ExceptionErrorModel(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }
}
