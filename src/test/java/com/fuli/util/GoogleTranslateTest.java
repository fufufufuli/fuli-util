package com.fuli.util;


import org.junit.Test;

public class GoogleTranslateTest {

    @Test
    public void translate() throws Exception{
        String game = GoogleTranslate.trans("en", "床前明月光，疑是地上霜");
        System.out.println(game);
    }


}