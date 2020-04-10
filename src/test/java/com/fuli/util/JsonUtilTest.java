package com.fuli.util;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.Test;

public class JsonUtilTest {
    @Getter
    @Setter
    @ToString
    class Apple {
        String name = "a";

        String money = "123";
    }

    Apple apple = new Apple();

    @Getter
    @Setter
    @ToString
    class Banana {
        String name = "b";

        double money = 456;
    }

    Banana banana = new Banana();

    @Test
    public void test() throws Exception {
        System.out.println(MapUtil.toMapStr(apple));
        System.out.println(MapUtil.toMap(banana));
    }

}