package com.fuli.util;


import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleTranslateTest {
    @Test
    public void test() throws Exception {
        String array = FileUtils.readFileToString(FileUtils.getFile("src/main/resources/translate"), "utf8");
        checkArray(array);
    }

    /**
     * 根据替换后的字符串长度是否一致来判断是否是一个完整的数组
     */
    private static void checkArray(String array) {
        Preconditions.checkArgument(Commons.isNotEmpty(array));
        String data = array.replaceAll("\n", "");
        List<Integer> left = appearIndex(data, "\\[");
        List<Integer> right = appearIndex(data, "]");
        Preconditions.checkArgument(left.size() == right.size());

        //right-left差值最小则是为一个数组内

        Map<Integer, Integer> map = new HashMap<>();

        List<Integer> make = new ArrayList<>(List.copyOf(left));

        for (Integer r : right) {
            add(map, make, r);
            make.remove(map.get(r));
        }


        map.forEach((k, v) -> System.out.println(data.substring(v, k + 1)));
        List<Node<Integer>> list = new ArrayList<>();
        for (Integer integer : right) {
            Node<Integer> node = new Node<>();
            node.setSelf(integer);
            if (integer < data.length() - 1 && ']' == (data.charAt(integer + 1))) {
                //integer+1 father
                //integer self
                node.setFather(integer + 1);

            }
            if (integer < data.length() - 2 && '[' == (data.charAt(integer + 2))) {
                //integer+2,integer同级
                node.setBrother(integer + 2);
            }
            list.add(node);

        }
        for (Node<Integer> node : list) {
            String substring = data.substring(map.get(node.self),node.self+1 );
           System.out.println("self======="+substring);

            Optional<Integer> optional = findKey(map, node.brother);
            if (optional.isPresent()){
                 substring = data.substring(node.brother,optional.get()+1);
                System.out.println("brother======="+substring);
            }
        }
    }

    private static <E> Optional<E> findKey(Map<E, E> map, E self) {
        for (Map.Entry<E, E> entry : map.entrySet()) {
            if (entry.getValue().equals(self)) {
                return Optional.of(entry.getKey()) ;
            }
        }
        return Optional.empty();
    }

    @Getter
    @Setter
    @ToString
    private static class Node<E> {
        E self;
        E father;
        E brother;
    }


    //order
    private static void add(Map<Integer, Integer> map, List<Integer> make, Integer r) {
        int flag = 2048;
        for (Integer l : make) {
            int temp = r - l;
            if (temp > 0 && temp < flag) {
                flag = temp;
                map.put(r, l);
            }
        }
    }


    private static List<Integer> appearIndex(String source, String target) {
        List<Integer> list = Lists.newArrayList();
        Matcher matcher = Pattern.compile(target).matcher(source);
        while (matcher.find()) {
            list.add(matcher.start());
        }
        Preconditions.checkArgument(Commons.isNotEmpty(list));
        return list;
    }

    @Test
    public void translate() {

     String game = GoogleTranslate.trans("en","床前明月光，疑是地上霜");

    }


}