package com.ytempest.asm;

import com.ytempest.asm.entity.RetentionAnnotation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * @author qiduhe
 * @since 2025/5/25
 */
public class RetentionAnnotationDeleter {

    public static final String ANNOTATION_CLASS_NAME = "java/lang/annotation/Annotation";

    public static boolean isSourceAnnotation(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            return isSourceAnnotation(new ClassReader(stream));
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isSourceAnnotation(ClassReader classReader) {
        if (classReader == null) {
            return false;
        }
        String[] superInterfaces = classReader.getInterfaces();
        if (superInterfaces == null
                || superInterfaces.length != 1
                || !ANNOTATION_CLASS_NAME.equals(superInterfaces[0])) {
            return false;
        }

        ClassNode classNode = new ClassNode(Opcodes.ASM6);
        classReader.accept(classNode, ClassReader.SKIP_DEBUG);

        List<String> interfaces = classNode.interfaces;
        if (interfaces == null || interfaces.size() != 1) {
            return false;
        }
        String superInterface = interfaces.get(0);
        if (!ANNOTATION_CLASS_NAME.equals(superInterface)) {
            return false;
        }

        List<AnnotationNode> visibleAnnotations = classNode.visibleAnnotations;
        if (visibleAnnotations == null || visibleAnnotations.size() <= 0) {
            return false;
        }

        for (AnnotationNode node : visibleAnnotations) {
            if (RetentionAnnotation.isRetention(node)) {
                RetentionAnnotation retentionAnnotation = new RetentionAnnotation(node);
                if ("value".equals(retentionAnnotation.name)
                        && "Ljava/lang/annotation/RetentionPolicy;".equals(retentionAnnotation.valueClass)
                        && "SOURCE".equals(retentionAnnotation.valueContent)) {
                    LogUtils.d("RetentionAnnotationDeleter name=" + classReader.getClassName()
                            + "  annotation=" + retentionAnnotation);
                    return true;
                }
            }
        }
        return false;
    }
}
