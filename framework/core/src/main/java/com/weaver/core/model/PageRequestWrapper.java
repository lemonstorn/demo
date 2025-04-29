package com.weaver.core.model;

import lombok.Data;

import java.util.Map;

/**
 * @author zh
 */
@Data
public class PageRequestWrapper<T> {

    private T bean;
    private Integer pageSize;
    private Integer page;
    private Map<String, String> sorts;
}
