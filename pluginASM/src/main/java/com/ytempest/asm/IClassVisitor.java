package com.ytempest.asm;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * @author qiduhe
 * @since 2025/5/23
 */
public interface IClassVisitor {
    void attach(ClassVisitorProxy host);

    boolean visitMethodBefore(int access, String name, String desc, String signature, String[] exceptions);

    MethodVisitor visitMethodAfter(MethodVisitor mv, int access, String name, String desc, String signature, String[] exceptions);

    boolean visitFieldBefore(int access, String name, String desc, String signature, Object value);
}
