package com.fuli.pudding.util;

/**
 * @Author: fuli
 * @Date: 2019/4/25 11:51
 */
class JSONException extends RuntimeException {

    JSONException(final String message) {
        super(message);
    }

    JSONException(final String message, final Throwable cause) {
        super(message, cause);
    }
}


