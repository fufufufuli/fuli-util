package com.fuli.pudding.util;

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
    /**
     * 返回body信息
     */
    private String result;
}
