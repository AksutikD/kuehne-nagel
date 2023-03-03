package com.logistic.kuehnenagel.error;

import java.util.Optional;

/**
 * Global exception class for our application to prevent {@link com.logistic.kuehnenagel.rest.ExceptionControllerHandler} from growing.
 */
public class ServiceException extends RuntimeException {

    private final ServiceError error;
    private final transient Object[] messageParameters;

    private ServiceException(Builder builder) {
        super();
        this.error = builder.error;
        this.messageParameters = builder.messageParameters;
    }

    public static Builder builder(ServiceError error) {
        return new Builder(error);
    }

    public ServiceError getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return error.getMessage(messageParameters);
    }

    public static class Builder {

        private final ServiceError error;
        private Object[] messageParameters;

        private Builder(ServiceError error) {
            this.error = error;
            this.messageParameters = new Object[0];
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
