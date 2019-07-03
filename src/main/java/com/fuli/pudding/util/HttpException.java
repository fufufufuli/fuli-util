package com.fuli.pudding.util;

/**
 * @author fuli
 */
public class HttpException extends RuntimeException {
    public static final String MISS_TYPE="不支持提交类型：";
    public HttpException(final String message) { super(message); }

    public HttpException( final Throwable cause) { super( cause); }


    public HttpException(String reason, final String message) { super(reason + message); }

    public HttpException(final String message, final Throwable cause) { super(message, cause); }
}
