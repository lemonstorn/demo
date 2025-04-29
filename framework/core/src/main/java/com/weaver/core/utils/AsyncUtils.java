package com.weaver.core.utils;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * @Author: zh
 * @CreateTime: 2025-04-06
 * @Description: 异步编程工具类
 * @Version: 1.0
 */

public class AsyncUtils {
    //对于单个相同任务多次执行
    @SneakyThrows
    public ConcurrentHashMap<Integer, Object> asyncRun(Supplier<Object> runMethod, int num) {
        ExecutorService threadPool = Executors.newFixedThreadPool(num);
        ConcurrentHashMap<Integer, Object> resultMap = new ConcurrentHashMap<>();
        CompletableFuture.allOf(
                IntStream
                        .range(0, num)
                        .mapToObj(index ->
                                CompletableFuture
                                        .supplyAsync(runMethod, threadPool)
                                        .whenComplete((result, exception) -> {
                                            if (exception != null) {
                                                resultMap.put(index, result);
                                            }
                                        }))
                        .toArray(CompletableFuture[]::new)
        ).get();
        return resultMap;
    }
}
