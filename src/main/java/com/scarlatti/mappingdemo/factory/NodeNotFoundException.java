package com.scarlatti.mappingdemo.factory;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public class NodeNotFoundException extends RuntimeException {

    private String ref;

    public String getRef() {
        return ref;
    }

    public NodeNotFoundException(String ref) {
        this.ref = ref;
    }

    public NodeNotFoundException(String message, String ref) {
        super(message);
        this.ref = ref;
    }

    public NodeNotFoundException(String message, Throwable cause, String ref) {
        super(message, cause);
        this.ref = ref;
    }

    public NodeNotFoundException(Throwable cause, String ref) {
        super(cause);
        this.ref = ref;
    }

    public NodeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String ref) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.ref = ref;
    }
}
