package com.retzero.imooc.entity;

import lombok.Data;

/**
 * @author JackJun
 * @date 2020/6/22 9:46 下午
 */
@Data
public class ClassSecondLevel {
    private String msg;
    private Integer code;
    private ClassCollect.InnerData data;

    @Data
    public static class InnerData {
        private String info;
    }
}
