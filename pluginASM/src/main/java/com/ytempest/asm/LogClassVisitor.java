package com.ytempest.asm;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;


/**
 * @author heqidu
 * @since 2025/5/9
 */
public class LogClassVisitor extends BaseClassVisitor {

    private static final String TAG = "LogClassVisitor";

    @Override
    public MethodVisitor visitMethodAfter(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethodAfter(mv, access, name, desc, signature, exceptions);
        // 取出所有需要修改的方法一个一个创建visitor去进行访问
        if (methodVisitor != null) {
            methodVisitor = new LogMethodVisitor(host.getApi(), mv, access, name, desc);
        }
        return methodVisitor;
    }

    private static class LogMethodVisitor extends AdviceAdapter {

        private final String mName;
        private final String mDesc;

        protected LogMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
            mName = name;
            mDesc = desc;
        }

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter();

            // 不处理构造方法
            if ("<init>".equals(mName) || "<clinit>".equals(mName)) {
                return;
            }

            LogUtils.d(TAG + " onMethodEnter mName=" + mName + "  mDesc=" + mDesc);

            //方法执行之前打印
            mv.visitLdcInsn("ytempest-method-log");
            mv.visitLdcInsn(mName);
            mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            mv.visitInsn(POP);
        }
    }
}
