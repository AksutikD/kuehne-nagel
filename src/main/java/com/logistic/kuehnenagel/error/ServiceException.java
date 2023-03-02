package com.logistic.kuehnenagel.error;

import java.util.Optional;

import static lombok.Lombok.checkNotNull;

/**
 * Global exception class for our application to prevent
 */
public class ServiceException extends RuntimeException {

    private final ServiceError error;
    private final transient Object[] messageParameters;

    private ServiceException(Builder builder) {
        super(builder.cause);
        this.error = builder.error;
        this.messageParameters = builder.messageParameters;
    }

    public static Builder builder(ServiceError error) {
        return new Builder(error);
    }

    public ServiceError getError() {
        return error;
    }

    public Object[] getMessageParameters() {
        return messageParameters.clone();
    }

    @Override
    public String getMessage() {
        return error.getMessage(messageParameters);
    }

    public static class Builder {

        private final ServiceError error;
        private Throwable cause;
        private Object[] messageParameters;

        private Builder(ServiceError error) {
            this.error = checkNotNull(error, "Service exception's error cannot be null");
            this.cause = null;
            this.messageParameters = new Object[0];
        }

        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public Builder messageParameters(Object... params) {
            this.messageParameters = Optional.ofNullable(params).orElseGet(() -> new Object[]{ null });
            return this;
        }

        public ServiceException build() {
            return new ServiceException(this);
        }
    }
}
