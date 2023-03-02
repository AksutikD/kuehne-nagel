package com.logistic.kuehnenagel.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;
import java.util.Locale;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
/**
 * Class contains all thrown error types by our application.
 */
public enum ServiceError {

    MISSING_OBJECT_BY_ID(HttpStatus.NOT_FOUND, "[{0}] with an id [{1}] not found.");

    private HttpStatus status;
    private String message;

    public String getMessage(Object... parameters) {
        if (parameters == null || parameters.length == 0) {
            return message;
        }

        MessageFormat formatter = new MessageFormat(message, Locale.ROOT);
        return formatter.format(parameters);
    }
}
