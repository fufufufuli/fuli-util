package com.fuli.util.json;

/**
 * @author fuli
 */
final class JsonException extends RuntimeException {

    public static final String CONVERT_ERROR = "convert json error:";

    public JsonException(final String message) { super(message); }

    public JsonException(final String message, final Throwable cause) { super(message, cause); }
}