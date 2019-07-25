package com.scarlatti.mappingdemo.factory;

import com.scarlatti.mappingdemo.util.Ref;

/**
 * @author Alessandro Scarlatti
 * @since Wednesday, 7/24/2019
 */
public class NodeNotFoundException extends RuntimeException {

    private Ref ref;

    public Ref getRef() {
        return ref;
    }

    public NodeNotFoundException(Ref ref) {
        this.ref = ref;
    }

    public NodeNotFoundException(String message, Ref ref) {
        super(message);
        this.ref = ref;
    }

    public NodeNotFoundException(String message, Throwable cause, Ref ref) {
        super(message, cause);
        this.ref = ref;
    }

    public NodeNotFoundException(Throwable cause, Ref ref) {
        super(cause);
        this.ref = ref;
    }

    public NodeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Ref ref) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.ref = ref;
    }
}
