package com.fuli.util.http;

import lombok.*;

/**
 * @Author: fuli
 * @Date: 2019/2/22 11:12
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HttpResult {

    private int code;

    private String body;
}
