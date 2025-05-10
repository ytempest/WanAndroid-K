package com.ytempest.dynamic_so;

import com.android.build.gradle.AppExtension;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;

import java.io.File;

class DynamicPlugin implements Plugin<Project> {
    Project project;

    private static final String TASK_NAME = "dynamicSOTask";
    private static final String PLUGIN_MODULE_NAME = "pluginDynamicSO";
    private static final String BUILD_SO_DIR_PATH = "/build/intermediates/merged_native_libs/debug/out/lib";
    private static final String CONFIG_DIR_NAME = "config";

    private static void log(String msg) {
        System.out.println(TASK_NAME + ", " + msg);
    }

    @Override
    public void apply(Project p) {
        project = p;
        AppExtension android = project.getExtensions().getByType(AppExtension.class);

        log("apply DynamicPlugin, project.name:" + project.getName());

        // 这个是初始化 -配置 -执行阶段中，配置阶段执行的任务之一，完成afterEvaluate就可以得到所有的tasks，从而可以在里面插入我们定制化的数据
        project.task(TASK_NAME).doLast(new Action<Task>() {
            @Override
            public void execute(Task task) {
                log("dynamicSo insert!!!!");
                //projectDir 在哪个project下面，projectDir就是哪个路径
                log("getRootProject:" + DefaultGroovyMethods.findAll(project.getRootProject()));

                File projectDir = project.getProjectDir();
                String soLibDirPath = projectDir.getAbsolutePath() + BUILD_SO_DIR_PATH;
                File soLibDir = new File(soLibDirPath);
                log("soLibDir=" + soLibDir);

                File pluginModuleDir = new File(project.getRootDir().getAbsolutePath(), PLUGIN_MODULE_NAME);
                File configDir = new File(pluginModuleDir, CONFIG_DIR_NAME);

                SOTaskProxy.execute(project, soLibDir, pluginModuleDir, configDir);
            }
        });
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                TaskContainer tasks = project.getTasks();
                log("dynamicSo task start");
                Task customer = tasks.findByName(TASK_NAME);
                Task merge = tasks.findByName("mergeDebugNativeLibs");
                Task strip = tasks.findByName("stripDebugDebugSymbols");
                if (merge != null && strip != null) {
                    customer.mustRunAfter(merge);
                    strip.dependsOn(customer);
                }
            }
        });
    }
}


