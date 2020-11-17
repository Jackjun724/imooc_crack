package com.retzero.imooc.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author JackJun
 * @date 2020/6/22 9:50 下午
 */
@Data
@Builder
public class ClassDetails {
    private List<String> urls;
    private byte[] key;
}
