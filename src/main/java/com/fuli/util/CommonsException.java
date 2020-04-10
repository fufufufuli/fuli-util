package com.fuli.util;

/**
 * @author fuli
 */
public class CommonsException extends RuntimeException{
    public CommonsException(String message) {
        super(message);
    }

    public CommonsException(String message, Throwable cause) {
        super(message, cause);
    }
}
