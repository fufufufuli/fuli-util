package com.fuli.util;


import org.apache.commons.io.FileUtils;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class UtilTest {


    @Test
    public void test() throws Exception {

        String game = transEnglish("床前明月光，疑是地上霜", "en").replaceAll("\n","");
        String[] split = game.split(",null")[0].split("\",\"");
        String translate = split[0].substring(4 );
        String source = split[1].substring(0,split[1].length()-1);
        System.out.println("================================");
        System.out.println("source: "+source);
        System.out.println("translate: "+translate);
        System.out.println("================================");
//        String[] array = array(game);
//        System.out.println("================================");
//        System.out.println("source: "+array[1]);
//        System.out.println("translate: "+array[0]);
//        System.out.println("================================");


    }

//    private static String[] array(String value) {
//
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
//        try (FileReader reader = new FileReader("src/main/resources/array.js")) {
//            engine.eval(reader);
//            if (engine instanceof Invocable invoke) {
//                String array1 = String.valueOf(invoke.invokeFunction("array1", value));
//                String array2 = String.valueOf(invoke.invokeFunction("array2", value));
//                String array3 = String.valueOf(invoke.invokeFunction("array3", value));
//                String array4 = String.valueOf(invoke.invokeFunction("array4", value));
//
//
//                return        array1   .split("=====");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        throw new RuntimeException();
//    }



    public String transEnglish(String str, String target) throws Exception {
        String url = "https://translate.google.cn/translate_a/single";
        List<String> list = List.of("at", "bd", "ex", "ld", "md", "qcq", "rw", "rm", "sos", "ss", "t");

        HttpResult httpResult = HttpUtil.get(url, add(str, target), "dt", list);
        FileUtils.write(FileUtils.getFile("src/main/resources/a"), httpResult.getBody(), "utf8");

        return httpResult.getBody();
    }

    public Map<String, String> add(String str, String target) {
        return Map.of("client", "webapp",
                "sl", "auto",
                "tl", target,
                "hl", "zh-CN",
                "tk", token(str),
                "q", str);
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