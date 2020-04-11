package com.fuli.util;


import com.fuli.disuse.Apple;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

public class JsonUtilTest {





    @Test
    public void test() throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("name", "apple");
        map.put("cost", "123");
         Apple apple = Commons.toBean(Apple.class, map);
        System.out.println(apple);
       // Banana banana = Commons.toBean(Banana.class, map);

    }

}