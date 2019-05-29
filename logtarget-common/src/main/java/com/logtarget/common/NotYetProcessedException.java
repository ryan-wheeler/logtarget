package com.logtarget.common;

/**
 * NotYetProcessedException indicates that the processing required to service a request is not yet complete,
 * but may be completed in the future
 */
public class NotYetProcessedException extends Exception {
    public NotYetProcessedException() {
    }

    public NotYetProcessedException(String message) {
        super(message);
    }

    public NotYetProcessedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotYetProcessedException(Throwable cause) {
        super(cause);
    }

    public NotYetProcessedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
