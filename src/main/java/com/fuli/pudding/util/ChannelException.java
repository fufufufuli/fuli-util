package com.fuli.pudding.util;

/**
 * @Author: fuli
 * @Date: 2019/4/25 11:51
 */
public class ChannelException extends RuntimeException {

    public ChannelException(final String message) {
        super(message);
    }

    public ChannelException(ChannelEnum channelEnum, final String message) {
        super(channelEnum.getMsg() + message);
    }

    public ChannelException(final String message, final Throwable cause) {
        super(message, cause);
    }
}


