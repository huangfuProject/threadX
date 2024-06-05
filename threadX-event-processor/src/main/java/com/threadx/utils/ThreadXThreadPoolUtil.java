package com.threadx.utils;

import com.threadx.constant.ThreadPoolProxyMake;
import com.threadx.thread.BusinessThreadXRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池工具类
 *
 * @author huangfukexing
 * @date 2023/3/15 09:23
 */
public class ThreadXThreadPoolUtil {

    private final static String THREAD_POOL_GROUP_NAME_TEMPLATE = "%s#%s:%d";

    private final static String THREAD_POOL_NAME_TEMPLATE = "%s-%d";

    /**
     * 获取对象的id
     *
     * @param obj 要运算的对象
     * @return id值
     */
    public static String getObjectId(Object obj) {
        if(obj instanceof BusinessThreadXRunnable) {
            return ((BusinessThreadXRunnable) obj).getTaskId();
        }
        return String.valueOf(System.identityHashCode(obj));
    }

    /**
     * 返回一个代理标记
     *
     * @return 代理标记
     */
    public static String proxyMask() {
        if (ConfirmCheckUtil.isIntercept()) {
            return ThreadPoolProxyMake.THREAD_X_PROXY.getMake();
        }
        return ThreadPoolProxyMake.NOT_PROXY.getMake();
    }

    /**
     * 根据线程堆栈信息，生成一个线程池的名称
     * 遍历整个堆栈信息，查找 java.util.concurrent.* 结束后的下一个，就是真正创建线程池的地方！
     *
     * @return 线程池的名称
     */
    public static String generateThreadPoolGroupName() {
        //获取反转之后的堆栈信息
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        String superior = "";
        boolean scanMask = false;
        for (StackTraceElement stackTrace : stackTraces) {
            String className = stackTrace.getClassName();
            String methodName = stackTrace.getMethodName();
            int lineNumber = stackTrace.getLineNumber();

            if (className.startsWith("java.util.concurrent.")) {
                scanMask = true;
                continue;
            }

            if (scanMask) {
                superior = generateThreadPoolGroupName(className, methodName, lineNumber);
                break;
            }

        }
        return superior;
    }

    /**
     * 生成线程池的名称
     *
     * @param threadPoolGroupName 线程池组的名称
     * @param poolExecutor        线程池
     * @return 返回真正的名称
     */
    public static String generateThreadPoolName(String threadPoolGroupName, ThreadPoolExecutor poolExecutor) {
        return String.format(THREAD_POOL_NAME_TEMPLATE, threadPoolGroupName, System.identityHashCode(poolExecutor));
    }

    /**
     * 生成线程池的名称
     *
     * @param poolExecutor 线程池
     * @return 返回真正的名称
     */
    public static String generateThreadPoolName(ThreadPoolExecutor poolExecutor) {
        String groupName = generateThreadPoolGroupName();
        return String.format(THREAD_POOL_NAME_TEMPLATE, groupName, System.identityHashCode(poolExecutor));
    }

    /**
     * 生成线程池的名称
     *
     * @param className  创建线程池的雷类名
     * @param methodName 创建线程池的方法名
     * @param lineNumber 线程池在哪里创建的
     * @return 线程池的名称
     */
    public static String generateThreadPoolGroupName(String className, String methodName, int lineNumber) {
        return String.format(THREAD_POOL_GROUP_NAME_TEMPLATE, className, methodName, lineNumber);
    }

    /**
     * 获取线程池堆栈信息
     *
     * @return 线程池堆栈信息
     */
    public static String getThreadPoolStack() {
        StringBuilder stackBuild = new StringBuilder();
        //获取堆栈信息
        List<StackTraceElement> stackTraces = reversalThreadPoolStack();
        //转换
        ThreadXCollectionUtils.iterationList(stackTraces, entity -> stackBuild.append(entity.toString()).append(System.lineSeparator()));
        return stackBuild.toString();
    }

    /**
     * 翻转堆栈链路
     *
     * @return 翻转后的堆栈链路
     */
    public static List<StackTraceElement> reversalThreadPoolStack() {

        //获取堆栈信息
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        List<StackTraceElement> stackTraceElements = new ArrayList<>(stackTraces.length);
        //倒序转换
        ThreadXCollectionUtils.iterationList(Arrays.asList(stackTraces), stackTraceElements::add, true);
        return stackTraceElements;
    }

    /**
     * 获取创建流程
     * @return 创建流
     */
    public static String getCreateFlow() {
        String threadPrefix = "java.util.concurrent.ThreadPoolExecutor";
        List<String> stackFlow = new ArrayList<>();
        //获取堆栈信息
        List<StackTraceElement> stackTraceElements = reversalThreadPoolStack();
        //生成流
        for (StackTraceElement stackTrace : stackTraceElements) {
            String className = stackTrace.getClassName();
            String methodName = stackTrace.getMethodName();
            int lineNumber = stackTrace.getLineNumber();
            if(className.contains(threadPrefix)) {
                break;
            }


            String simpleNode = String.format("%s#%s:%s", className, methodName, lineNumber);
            stackFlow.add(simpleNode);
        }
        return ThreadXCollectionUtils.join(stackFlow, "->");
    }
}
