package com.weaver;

/**
 * @Author: zh
 * @CreateTime: 2025-04-29
 * @Description:
 * @Version: 1.0
 */

public class TestParent {
    public void log(){
        StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        stackWalker.forEach(System.out::println);

        Class<?> callerClass = stackWalker.getCallerClass();
        System.out.println("Caller class: " + callerClass.getName());
    }
}
