package com.fuli.util;


import org.junit.Test;

public class UtilTest {


    @Test
    public void test() throws Exception {
        HttpResult httpResult = HttpUtil.get("https://www.baidu.com");
        System.out.println(httpResult.getBody());

    }

}