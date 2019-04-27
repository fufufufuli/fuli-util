package com.fuli.pudding.util;

public  enum ChannelEnum {
    CONFIG("未定义的配置信息:"),
    API_NO("未定义的接口编号:"),
    METHOD("未定义的加密方式:"),
    TYPE("未定义的类型:");

    private String msg;

    public String getMsg() {
        return msg;
    }

    ChannelEnum(String msg) {
        this.msg = msg;
    }
}
