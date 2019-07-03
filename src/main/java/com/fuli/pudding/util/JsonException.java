package com.fuli.pudding.util;

/**
 * Created by kui.luo on 14-2-20.
 */
public final class JsonException extends RuntimeException {
    static final String GET_VALUE_ERROR = "获取json中key值异常";

    public JsonException(final String message) {
        super(message);
    }

    public JsonException(final String message, final Throwable cause) {
        super(message, cause);
    }
}