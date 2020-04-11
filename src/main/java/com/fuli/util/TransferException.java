package com.fuli.util;

/**
 * @author fuli
 */
public class TransferException extends RuntimeException {

    public static final String UN_KNOW_CLASS="未识别对象类型：";

    public TransferException(Object object) { super( UN_KNOW_CLASS+object.getClass().getSimpleName()); }

    public TransferException(final String message) { super(message); }

    public TransferException( final Throwable cause) { super( cause); }

    public TransferException(final String message, final Throwable cause) { super(message, cause); }
}
