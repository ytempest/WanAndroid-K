package com.ytempest.asm.entity;

import org.objectweb.asm.tree.AnnotationNode;

import java.util.List;

/**
 * @author qiduhe
 * @since 2025/5/25
 */
public class RetentionAnnotation {

    public String name;
    public String valueClass;
    public String valueContent;

    public static boolean isRetention(AnnotationNode node) {
        boolean isValidDesc = "Ljava/lang/annotation/Retention;".equals(node.desc);
        if (!isValidDesc) {
            return false;
        }
        List<Object> nodes = node.values;
        if (nodes == null || nodes.size() < 2) {
            return false;
        }
        try {
            String name = (String) nodes.get(0);
            String[] values = (String[]) nodes.get(1);
            return name != null && values != null;
        } catch (Exception e) {
        }
        return false;
    }


    public RetentionAnnotation(AnnotationNode annotation) {
        List<Object> nodes = annotation.values;
        try {
            name = (String) nodes.get(0);
            String[] values = (String[]) nodes.get(1);
            valueClass = values[0];
            valueContent = values[1];
        } catch (Exception e) {
        }
    }


    @Override
    public String toString() {
        return "RetentionAnnotation{" +
                "name='" + name + '\'' +
                ", valueClass='" + valueClass + '\'' +
                ", valueContent='" + valueContent + '\'' +
                '}';
    }
}
