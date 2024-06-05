package com.threadx.visit;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * ThreadPoolExecutor构造函数的拦截修改器
 *
 * @author huangfukexing
 * @date 2023/3/9 21:13
 */
public class ThreadPoolExecutorConstructorVisitor extends MethodVisitor {
    protected ThreadPoolExecutorConstructorVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM9, methodVisitor);
    }

    @Override
    public void visitInsn(int opcode) {

        if (opcode == Opcodes.RETURN) {
            // 在构造方法完成之前，调用拦截器 将 this 压入操作数栈
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/threadx/state/ThreadPoolExecutorState", "init", "(Ljava/util/concurrent/ThreadPoolExecutor;)V", false);
        }
        super.visitInsn(opcode);
    }
}
