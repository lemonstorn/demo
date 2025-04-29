package com.weaver.ddd.domain;

import cn.hutool.core.util.IdUtil;
import com.weaver.core.exception.WeaverBaseException;
import lombok.Getter;

import java.time.Instant;

/**
 * @Author: zh
 * @CreateTime: 2025-04-20
 * @Description: 事件驱动基类
 * @Version: 1.0
 */
@Getter
public abstract class DomainEvent {
    private final String id;//事件ID
    private final DomainEventType type;//事件类型
    private final Instant raisedAt;//事件产生时间

    protected DomainEvent(DomainEventType type) {
        if (type == null){
            throw new WeaverBaseException("领域事件生成失败");
        }
        this.id = IdUtil.getSnowflakeNextIdStr();
        this.type = type;
        this.raisedAt = Instant.now();
    }
}
