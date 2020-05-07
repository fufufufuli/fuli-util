package com.fuli.util;

import com.fuli.util.http.HttpResult;
import com.fuli.util.http.HttpUtil;
import org.apache.commons.io.FileUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author fuli
 * @create 2020/4/23
 */
public class GoogleTranslate {

    private static final String FILE_PATH = "src/main/resources/translate";
    private static final String URL = "https://translate.google.cn/translate_a/single";

    /**
     * @param language 语言
     * @param source   原文
     * @return 译文
     */
    public static String trans(String language, String source) {
        List<String> list = List.of("at", "bd", "ex", "ld", "md", "qcq", "rw", "rm", "sos", "ss", "t");
        HttpResult httpResult = HttpUtil.get(URL, buildParam(language, source), "dt", list);
        writeFile(FileUtils.getFile(FILE_PATH), httpResult.getBody());
        String[] split = httpResult.getBody()
                .replaceAll("\n", "")
                .split(",null")[0]
                .split("\",\"");
        return split[0].substring(4);
    }

    public static String toCN(String source) {
        return trans("zh-CN", source);
    }

    public static String toEN(String source) {
        return trans("en", source);
    }


    private static void writeFile(File file, String source) {
        try {
            FileUtils.write(file, source, "utf8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> buildParam(String language, String source) {
        return Map.of("client", "webapp",
                "sl", "auto",
                "tl", language,
                "hl", "zh-CN",
                "tk", token(source),
                "q", source);
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