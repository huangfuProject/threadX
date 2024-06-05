package com.threadx.test.stack;

/**
 * 堆栈收集
 *
 * @author huangfukexing
 * @date 2023/3/14 13:59
 */
public class TestStackEntity {
    public TestStackEntity() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            System.out.println(stackTraceElement.toString());
        }
    }
}
