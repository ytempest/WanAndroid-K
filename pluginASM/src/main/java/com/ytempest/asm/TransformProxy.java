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
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;


/**
 * @author heqidu
 * @since 2025/5/9
 */
public class TransformProxy extends Transform {

    public TransformProxy(Project p) {
        super();
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        final long start = System.currentTimeMillis();
        //输入，可以从中获取jar包和class文件夹路径。需要输出给下一个任务
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        //OutputProvider管理输出路径
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        //遍历目录
        for (TransformInput input : inputs) {
            // 遍历jar 第三方引入的 class
            for (JarInput jarInput : input.getJarInputs()) {
                handJarInput(jarInput, outputProvider);
            }
            // 遍历文件夹
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                handleDirectoryInput(directoryInput, outputProvider);
            }
        }
        final long passTime = System.currentTimeMillis() - start;
        LogUtils.d("TransformProxy run time:" + passTime);
    }

    //遍历jarInputs 得到对应的class 交给ASM处理
    private static void handJarInput(JarInput jarInput, TransformOutputProvider outputProvider) throws IOException {
        File jarFile = jarInput.getFile();
        if (!jarFile.getAbsolutePath().endsWith(".jar")) {
            return;
        }

        File output = outputProvider.getContentLocation(
                jarInput.getFile().getAbsolutePath(),
                jarInput.getContentTypes(),
                jarInput.getScopes(),
                Format.JAR);

        modifyJar(jarFile, output);
    }

    private static void modifyJar(File jarFile, File output) throws IOException {
        //重名名输出文件,因为可能同名,会覆盖
        String jarName = jarFile.getName();
        LogUtils.d("modifyJar jarName=" + jarName);
        File tmpFile = new File(jarFile.getParent() + File.separator + "classes_temp.jar");
        //避免上次的缓存被重复插入
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile));
        JarFile jar = new JarFile(jarFile);
        Enumeration<JarEntry> enumeration = jar.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = enumeration.nextElement();
            String entryName = jarEntry.getName();
            ZipEntry zipEntry = new ZipEntry(entryName);
            InputStream inputStream = jar.getInputStream(jarEntry);

            String fileName = new File(entryName).getName();
            LogUtils.d("modifyJar fileName: " + fileName);

            if (isValidClassFile(fileName)) {
                byte[] inputByte = IOUtils.toByteArray(inputStream);
                if (RetentionAnnotationDeleter.isSourceAnnotation(new ClassReader(inputByte))) {
                    // 编译时注解类移除，这里不写入到 jar

                } else {
                    //class文件处理
                    ClassReader classReader = new ClassReader(inputByte);
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
                    ClassVisitor cv = new ClassVisitorProxy(classWriter, entryName);
                    classReader.accept(cv, ClassReader.EXPAND_FRAMES);
                    jarOutputStream.putNextEntry(zipEntry);
                    jarOutputStream.write(classWriter.toByteArray());
                }

            } else {
                jarOutputStream.putNextEntry(zipEntry);
                jarOutputStream.write(IOUtils.toByteArray(inputStream));
            }
            jarOutputStream.closeEntry();
        }
        //结束
        jarOutputStream.close();
        jar.close();

        FileUtils.copyFile(tmpFile, output);
        tmpFile.delete();
    }

    private static void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) throws IOException {
        //是否是目录
        if (!directoryInput.getFile().isDirectory()) {
            return;
        }
        File dest = outputProvider.getContentLocation(directoryInput.getName(),
                directoryInput.getContentTypes(), directoryInput.getScopes(),
                Format.DIRECTORY);
        transformDir(directoryInput.getFile(), dest);
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
                        if (RetentionAnnotationDeleter.isSourceAnnotation(file)) {
                            // 移除编译时注解，这里就不用复制了
                        } else {
                            FileUtils.touch(destFile);
                            weave(file, destFile);
                        }
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
        return TransformProxy.class.getName();
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

}
