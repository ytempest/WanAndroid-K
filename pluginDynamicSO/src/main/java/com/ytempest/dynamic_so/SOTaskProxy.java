package com.ytempest.dynamic_so;

import org.gradle.api.Project;

import java.io.File;

/**
 * @author heqidu
 * @since 2023/9/9
 */
public class SOTaskProxy {
    public static void execute(Project project, File soLibDir, File pluginModuleDir, File configDir) {
        new SOCollectTask(project, soLibDir, pluginModuleDir, configDir)
                .start();
    }
}
