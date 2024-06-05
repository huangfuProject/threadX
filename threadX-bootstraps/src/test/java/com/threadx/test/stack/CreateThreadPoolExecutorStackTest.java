package com.threadx.test.stack;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池堆栈创建
 *
 * @author huangfukexing
 * @date 2023/3/13 09:56
 */
public class CreateThreadPoolExecutorStackTest {
    private static final TestStackEntity threadPoolExecutor = new TestStackEntity();

    public static void main(String[] args) {
        System.out.println("===========");
        TestStackEntity threadPoolExecutor = new TestStackEntity();
        System.out.println("=======");
        test();
    }


    public static void test(){
        for (int i = 0; i < 5; i++) {
            System.out.println("=============");
            TestStackEntity threadPoolExecutor = new TestStackEntity();
        }
    }
}
