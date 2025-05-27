package com.ytempest.asm;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class ASMPlugin implements Plugin<Project> {

    private static final String TASK_NAME = "ASMPlugin";

    @Override
    public void apply(Project p) {
        AppExtension appExtension = p.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(new TransformProxy(p));
    }
}


