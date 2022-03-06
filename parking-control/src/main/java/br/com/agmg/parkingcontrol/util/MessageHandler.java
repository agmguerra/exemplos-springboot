package br.com.agmg.parkingcontrol.util;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class MessageHandler {

    private MessageSource messageSource;
    
    public MessageHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, Locale.getDefault());
    }
    
}
