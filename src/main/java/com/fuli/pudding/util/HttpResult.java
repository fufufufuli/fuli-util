package com.fuli.pudding.util;

/**
 * @Author: fuli
 * @Date: 2019/2/22 11:12
 */
public class HttpResult {
    private int code;
    /**
     * 返回body信息
     */
    private String result;

    public int getCode() {
        return code;
    }

    public String getResult() {
        return result;
    }

    public HttpResult(int code, String result) {
        this.code = code;
        this.result = result;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
            "code=" + code +
            ", result='" + result + '\'' +
            '}';
    }
}
