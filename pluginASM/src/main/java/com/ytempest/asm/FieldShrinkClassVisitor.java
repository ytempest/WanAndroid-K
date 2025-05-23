package com.ytempest.asm;

import org.objectweb.asm.Opcodes;

import java.util.ArrayList;

/**
 * @author qiduhe
 * @since 2025/5/23
 */
class FieldShrinkClassVisitor extends BaseClassVisitor {

    private static final String TAG = "FieldShrinkClassVisitor";
    private static final ArrayList<Integer> deleteOpcodesList = new ArrayList<>();
    private static final ArrayList<String> deleteTypeList = new ArrayList<>();

    static {
        deleteOpcodesList.add(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL);
        deleteOpcodesList.add(Opcodes.ACC_PROTECTED | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL);
        deleteOpcodesList.add(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL);
        deleteOpcodesList.add(Opcodes.ACC_STATIC | Opcodes.ACC_FINAL);


        deleteTypeList.add("C");
        deleteTypeList.add("B");
        deleteTypeList.add("S");
        deleteTypeList.add("I");
        deleteTypeList.add("F");
        deleteTypeList.add("J");
        deleteTypeList.add("D");
        deleteTypeList.add("Ljava/lang/String;");
    }

    private final String mClassName;

    public FieldShrinkClassVisitor(String className) {
        mClassName = className;
    }


    @Override
    public boolean visitFieldBefore(int access, String name, String desc, String signature, Object value) {
        if (deleteOpcodesList.contains(access) && deleteTypeList.contains(desc) && value != null) {
            System.out.println(TAG + " visitField className=" + mClassName + "  fieldName=" + name);
            return true;
        }
        return super.visitFieldBefore(access, name, desc, signature, value);
    }
}
