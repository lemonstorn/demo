package com.weaver;

/**
 * @Author: zh
 * @CreateTime: 2025-04-29
 * @Description:
 * @Version: 1.0
 */

public class TestChild extends TestParent{
    public void execute(){
        System.out.println("开始执行");
        log();
        System.out.println("结束执行");
    }

    public static void main(String[] args) {
        new TestChild().execute();;
    }
}
