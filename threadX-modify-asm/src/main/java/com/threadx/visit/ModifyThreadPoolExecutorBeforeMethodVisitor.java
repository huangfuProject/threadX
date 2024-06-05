package com.threadx.visit;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 修改的方法的访问
 *
 * @author huangfukexing
 * @date 2023/3/9 11:54
 */
public class ModifyThreadPoolExecutorBeforeMethodVisitor extends MethodVisitor {

    protected ModifyThreadPoolExecutorBeforeMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM9, methodVisitor);


    }

    /**
     * 开始方法方法
     */
    @Override
    public void visitCode() {
        try {
            //将参数压入操作数栈顶
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            //将 this压入操作数栈顶
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/threadx/state/ThreadPoolTaskState", "beforeTaskExecution", "(Ljava/lang/Thread;Ljava/lang/Runnable;)V", false);
        }catch (Throwable w) {
            w.printStackTrace();
        }
        super.visitCode();
    }
}
