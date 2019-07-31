package com.fuli.util;

/**
 * @author fuli
 */
public class MapException extends RuntimeException{
    public MapException(String message) {
        super(message);
    }

    public MapException(String message, Throwable cause) {
        super(message, cause);
    }
}
