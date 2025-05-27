package com.ytempest.asm;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;


/**
 * @author heqidu
 * @since 2025/5/9
 */
public class ClassVisitorProxy extends ClassVisitor {

    private static final String TAG = "ClassVisitorProxy";

    public final List<IClassVisitor> mClassVisitors = new ArrayList<>();

    public ClassVisitorProxy(ClassWriter writer, String className) {
        super(Opcodes.ASM6, writer);
        assembleClassVisitors(className);
        for (IClassVisitor visitor : mClassVisitors) {
            visitor.attach(this);
        }
    }

    private void assembleClassVisitors(String className) {
//        mClassVisitors.add(new LogClassVisitor());
        mClassVisitors.add(new FieldShrinkClassVisitor(className));
    }

    public int getApi() {
        return api;
    }

    public ClassVisitor getClassVisitor() {
        return cv;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        boolean removeMethod = false;
        for (IClassVisitor classVisitor : mClassVisitors) {
            removeMethod |= classVisitor.visitMethodBefore(access, name, desc, signature, exceptions);
        }
        if (removeMethod) {
            return null;
        }
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        for (IClassVisitor classVisitor : mClassVisitors) {
            mv = classVisitor.visitMethodAfter(mv, access, name, desc, signature, exceptions);
        }
        return mv;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        boolean removeField = false;
        for (IClassVisitor visitor : mClassVisitors) {
            removeField |= visitor.visitFieldBefore(access, name, desc, signature, value);
        }
        if (removeField) {
            // 返回null表示将这个属性从class文件中移除
            return null;
        }

        return super.visitField(access, name, desc, signature, value);
    }

}
