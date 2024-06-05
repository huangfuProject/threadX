package com.threadx.visit;

import com.threadx.log.Logger;
import com.threadx.log.ThreadXLoggerFactoryApi;
import com.threadx.log.factory.ThreadXAgetySystemLoggerFactory;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 线程池类修改器
 *
 * @author huangfukexing
 * @date 2023/3/9 10:33
 */
public class ModifyThreadPoolExecutorClassVisitor extends ClassVisitor {

    private final static Logger LOGGER = ThreadXAgetySystemLoggerFactory.getLogger(ModifyThreadPoolExecutorClassVisitor.class);

    /**
     * 构造函数的名称
     */
    private static final String CONSTRUCTOR_NAME = "<init>";

    /**
     * 构造函数的签名
     */
    private static final String CONSTRUCTOR_DESCRIPTOR = "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;Ljava/util/concurrent/RejectedExecutionHandler;)V";

    /**
     * execute方法名
     */
    private static final String EXECUTE_METHOD_NAME = "execute";

    /**
     * execute方法签名
     */
    private static final String EXECUTE_METHOD_DESCRIPTOR = "(Ljava/lang/Runnable;)V";

    /**
     * 前置回调
     */
    private static final String BEFORE_EXECUTE_METHOD_NAME = "beforeExecute";
    /**
     * 前置回调的方法签名
     */
    private static final String BEFORE_EXECUTE_METHOD_DESCRIPTOR = "(Ljava/lang/Thread;Ljava/lang/Runnable;)V";


    /**
     * 后置回调
     */
    private static final String AFTER_EXECUTE_METHOD_NAME = "afterExecute";
    /**
     * 后置回调的方法签名
     */
    private static final String AFTER_EXECUTE_METHOD_DESCRIPTOR = "(Ljava/lang/Runnable;Ljava/lang/Throwable;)V";


    public ModifyThreadPoolExecutorClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM9, classVisitor);
    }

    /**
     * 该方法在访问一个类的方法时被调用，用于获取方法信息。
     *
     * @param access     方法的访问标志，使用 ACC_XXX 常量表示，例如 ACC_PUBLIC、ACC_FINAL 等。
     * @param name       方法的名称。
     * @param descriptor 方法的描述符，例如“(Ljava/lang/String;)V”。
     * @param signature  方法的泛型签名，如果方法没有使用泛型，则为 null。
     * @param exceptions 方法可能抛出的异常的类型名称数组，每个异常类型以“/”分隔，例如“java/lang/Exception”。
     * @return 方法的访问对象
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        try {
            if (CONSTRUCTOR_NAME.equals(name) && CONSTRUCTOR_DESCRIPTOR.equals(descriptor)) {
                LOGGER.info("modify ThreadPoolExecutor by method: {} param type: {}", name, descriptor);
                methodVisitor = new ThreadPoolExecutorConstructorVisitor(methodVisitor);
            } else if (EXECUTE_METHOD_NAME.equals(name) && EXECUTE_METHOD_DESCRIPTOR.equals(descriptor)) {
                LOGGER.info("modify ThreadPoolExecutor by method: {} param type: {}", name, descriptor);
                methodVisitor = new ModifyThreadPoolExecutorExecuteMethodVisitor(methodVisitor);
            } else if (BEFORE_EXECUTE_METHOD_NAME.equals(name) && BEFORE_EXECUTE_METHOD_DESCRIPTOR.equals(descriptor)) {
                LOGGER.info("modify ThreadPoolExecutor by method: {} param type: {}", name, descriptor);
                methodVisitor = new ModifyThreadPoolExecutorBeforeMethodVisitor(methodVisitor);
            } else if (AFTER_EXECUTE_METHOD_NAME.equals(name) && AFTER_EXECUTE_METHOD_DESCRIPTOR.equals(descriptor)) {
                LOGGER.info("modify ThreadPoolExecutor by method: {} param type: {}", name, descriptor);
                methodVisitor = new ModifyThreadPoolExecutorAfterMethodVisitor(methodVisitor);
            }
        } catch (Throwable w) {
            w.printStackTrace();
        }
        return methodVisitor;
    }


}
