package com.retzero.imooc.entity;

import lombok.Data;

import java.util.List;

/**
 * @author JackJun
 * @date 2020/6/22 9:33 下午
 */
@Data
public class ClassCollect {

    private String msg;
    private Integer result;
    private InnerData data;

    @Data
    public static class InnerData {
        private List<String> cdn;
        private String info;
        private String phone;
    }
}
