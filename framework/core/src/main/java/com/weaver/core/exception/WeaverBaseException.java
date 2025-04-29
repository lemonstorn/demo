package com.weaver.core.exception;

import org.springframework.core.NestedRuntimeException;

import java.io.Serializable;

/**
 * @Author: zh
 * @CreateTime: 2025-04-04
 * @Description: 常规异常
 * @Version: 1.0
 */

public class WeaverBaseException extends NestedRuntimeException implements Serializable {
    public WeaverBaseException(String msg) {
        super(msg);
    }
}
