package com.ytempest.common.so;


// generate by DynamicSOPlugin
public enum YSO {
    SHINE(1, "shine", "23421ca25d4b3b824c807291f197f2f6", "23421ca25d4b3b824c807291f197f2f6",""), 
    X264(1, "x264", "3d1a6480c8838017505ef6dbefb0869f", "3d1a6480c8838017505ef6dbefb0869f",""), 

    ;
    public final int version;
    public final String name;
    public final String arm64Hash;
    public final String armHash;
    public final String x86Hash;

    YSO(int version, String name, String arm64Hash, String armHash, String x86Hash) {
        this.version = version;
        this.name = name;
        this.arm64Hash = arm64Hash;
        this.armHash = armHash;
        this.x86Hash = x86Hash;
    }
}
