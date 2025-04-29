package com.weaver.ddd.aggregate;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

/**
 * @Author: zh
 * @CreateTime: 2025-04-29
 * @Description: 聚合根
 * @Version: 1.0
 */
@Slf4j
public class AggregateRoot {
    protected void logDebug(Object... objects){
        StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        String stackTrace = stackWalker.walk(s -> s.filter(stackFrame -> stackFrame.getClassName().contains("com.weaver."))
                .map(StackWalker.StackFrame::toString)
                .collect(Collectors.joining("\n")));

        Class<?> callerClass = stackWalker.getCallerClass();
        log.debug("调用者==>{},调用参数{},调用堆栈信息\n{}", callerClass.getName(),objects,stackTrace);
    }
}
