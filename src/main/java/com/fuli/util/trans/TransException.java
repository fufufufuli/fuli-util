package com.fuli.util.trans;

/**
 * @author fuli
 */
final class TransException extends RuntimeException {

    public static final String UN_KNOW_CLASS="未识别对象类型：";

    public TransException(Object object) { super( UN_KNOW_CLASS+object.getClass().getSimpleName()); }

    public TransException(final String message) { super(message); }

    public TransException(final Throwable cause) { super( cause); }

    public TransException(final String message, final Throwable cause) { super(message, cause); }
}
