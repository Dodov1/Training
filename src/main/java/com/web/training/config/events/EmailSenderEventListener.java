package com.web.training.config.events;

import com.web.training.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.web.training.config.appConstants.AppConstants.EMAIL_VERIFICATION_MESSAGE;

@Component
public class EmailSenderEventListener {

    private final EmailService emailService;

    public EmailSenderEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleAsyncEvent(EmailSenderEvent e) {
        String format = String.format(EMAIL_VERIFICATION_MESSAGE, e.getUser().getUsername(), e.getUser().getId());
        this.emailService.sendSimpleMessage(e.getUser().getEmail(), "Email Verification", format);
    }
}
