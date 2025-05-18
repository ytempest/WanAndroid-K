package com.ytempest.dynamic_so;

import java.io.File;

/**
 * @author qiduhe
 * @since 2023/9/6
 */
public class UploadSO {
    public final String cpuType;
    public final File soFile;
    public final String soName;
    public final String soHash;

    public UploadSO(String cpuType, File soFile) {
        this.cpuType = cpuType;
        this.soFile = soFile;
        this.soName = FileUtil.getSOSimpleName(soFile);
        this.soHash = FileUtil.getFileMd5(soFile);
    }
}
