package com.ytempest.asm;


import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;


/**
 * @author heqidu
 * @since 2025/5/9
 */
public class LogTransform extends Transform {

    public LogTransform(Project p) {
        super();
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        //输入，可以从中获取jar包和class文件夹路径。需要输出给下一个任务
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        //OutputProvider管理输出路径
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        //遍历目录
        for (TransformInput input : inputs) {
            // 遍历jar 第三方引入的 class
            for (JarInput jarInput : input.getJarInputs()) {
                File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                //不处理jar文件，直接copy
                FileUtils.copyFile(jarInput.getFile(), dest);
            }
            //
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                handleDirectoryInput(directoryInput, outputProvider);
            }
        }
    }


    private static void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) throws IOException {
        //是否是目录
        if (directoryInput.getFile().isDirectory()) {
            File dest = outputProvider.getContentLocation(directoryInput.getName(),
                    directoryInput.getContentTypes(), directoryInput.getScopes(),
                    Format.DIRECTORY);
            transformDir(directoryInput.getFile(), dest);
        }
    }


    private static void transformDir(File input, File dest) throws IOException {
        if (dest.exists()) {
            FileUtils.forceDelete(dest);
        }
        FileUtils.forceMkdir(dest);
        String srcDirPath = input.getAbsolutePath();
        String destDirPath = dest.getAbsolutePath();
        for (File file : Objects.requireNonNull(input.listFiles())) {
            String destFilePath = file.getAbsolutePath().replace(srcDirPath, destDirPath);
            File destFile = new File(destFilePath);
            if (file.isDirectory()) {
                transformDir(file, destFile);
            } else if (file.isFile()) {
                final String fileName = file.getName();
                if (isClassFile(fileName)) {
                    if (isValidClassFile(fileName)) {
                        FileUtils.touch(destFile);
                        weave(file, destFile);
                    } else {
                        FileUtils.copyFile(file, destFile);
                    }
                }
            }
        }
    }


    private static void weave(File srcFile, File destFile) {
        try {
            FileInputStream is = new FileInputStream(srcFile);
            ClassReader cr = new ClassReader(is);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            // srcFile.getName()获取示例：PreferencesName$Companion.class
            ClassVisitorProxy adapter = new ClassVisitorProxy(cw, srcFile.getName());
            cr.accept(adapter, ClassReader.EXPAND_FRAMES);
            FileOutputStream fos = new FileOutputStream(destFile);
            fos.write(cw.toByteArray());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean isClassFile(String name) {
        return name != null && name.endsWith(".class");
    }

    /**
     * 检查class文件是否需要处理
     */
    static boolean isValidClassFile(String name) {
        return (name != null
                && name.endsWith(".class")
                && !name.startsWith("R$")
                && !"R.class".equals(name)
                && !"BuildConfig.class".equals(name));
    }

    @Override
    public String getName() {
        return LogTransform.class.getName();
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.PROJECT_ONLY;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

}
