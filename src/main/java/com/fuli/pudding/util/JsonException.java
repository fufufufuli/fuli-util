package com.fuli.pudding.util;

/**
 * @author fuli
 */
public final class JsonException extends RuntimeException {
    public static final String GET_VALUE_ERROR = "获取json中key值异常";

    public JsonException(final String message) { super(message); }

    public JsonException(final String message, final Throwable cause) { super(message, cause); }
}