package com.fuli.util;


import com.google.common.collect.Maps;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilTest {


    @Test
    public void test() throws Exception {
        String url = "https://translate.google.cn/translate_a/single";
        List<String> list = List.of("at", "bd", "ex", "ld", "md", "qcq", "rw", "rm", "sos", "ss", "t");
        HttpResult httpResult = HttpUtil.get(url, add("blue"), "dt", list);
        System.out.println(httpResult.getBody());
    }

    public Map<String, String> add(String str) {
        Map<String, String> map = Maps.newTreeMap();
        map.put("client", "webapp");
        map.put("sl", "auto");
        map.put("tl", "zh-CN");
        map.put("hl", "zh-CN");
        map.put("tk", token(str));
        map.put("q", str);
        return map;
    }

    private static String token(String value) {
        String result = "";
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try (FileReader reader = new FileReader("src/main/resources/google.js")) {
            engine.eval(reader);
            if (engine instanceof Invocable invoke) {
                result = String.valueOf(invoke.invokeFunction("token", value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}