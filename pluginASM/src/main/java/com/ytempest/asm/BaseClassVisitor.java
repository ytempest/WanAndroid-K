package com.ytempest.asm;

import org.objectweb.asm.MethodVisitor;

/**
 * @author qiduhe
 * @since 2025/5/23
 */
public abstract class BaseClassVisitor implements IClassVisitor {

    protected ClassVisitorProxy host;

    @Override
    public void attach(ClassVisitorProxy host) {
        this.host = host;
    }

    @Override
    public boolean visitMethodBefore(int access, String name, String desc, String signature, String[] exceptions) {
        return false;
    }

    @Override
    public MethodVisitor visitMethodAfter(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions) {
        return mv;
    }

    @Override
    public boolean visitFieldBefore(int access, String name, String desc, String signature, Object value) {
        return false;
    }
}
