package com.ytempest.dynamic_so;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;


/**
 * @author heqidu
 * @since 2025/5/9
 */
public class TestClassVisitor extends ClassVisitor {
    public TestClassVisitor(ClassWriter writer) {
        super(Opcodes.ASM6, writer);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        // 取出所有需要修改的方法一个一个创建visitor去进行访问
        if (mv != null) {
            mv = new TestMethodVisitor(api, mv, access, name, desc);
        }
        return mv;
    }

    private static class TestMethodVisitor extends AdviceAdapter {

        private final String mName;

        protected TestMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
            mName = name;
        }

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter();
            System.out.println("TestClassVisitor  onMethodEnter");

            if ("<init>".equals(mName)) {
                System.out.println("TestClassVisitor  onMethodEnter <init>");
                return;
            }

            //方法执行之前打印
            mv.visitLdcInsn("\u8fdb\u5165\u65b9\u6cd5");file://
            mv.visitLdcInsn(mName);
            mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            mv.visitInsn(POP);

            System.out.println("TestClassVisitor  onMethodEnter -- end");
        }
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        System.out.println("TestClassVisitor  visitEnd");
    }
}
