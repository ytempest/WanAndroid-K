package com.ytempest.dynamic_so

import com.ytempest.dynamic_so.entity.SOVersionList
import com.ytempest.dynamic_so.properties.SOConfigProperties
import com.ytempest.dynamic_so.util.SLog
import com.ytempest.dynamic_so.util.Utils
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

/**
 * @author heqidu
 * @since 2023/9/9
 */
class SOClassGenerator(private val projectDir: File) {

    private val TAG = "SOClassGenerator"

    fun generateClass(soList: SOVersionList) {
        if (SOConfigProperties.soClassPath.isEmpty()) {
            throw IllegalArgumentException("please setup the soClassPath in " + SOConfigProperties.PROPERTIES_FILE_NAME)
        }
        val soPackageName = SOConfigProperties.soPackageName
        if (soPackageName.isEmpty()) {
            throw IllegalArgumentException("please setup the soPackageName in " + SOConfigProperties.PROPERTIES_FILE_NAME)
        }
        val soClassModuleDir = File(projectDir, SOConfigProperties.soClassPath)
        val soClassPackagePath = soPackageName.replace(".", "/")
        val soClassFullDir = File(soClassModuleDir, soClassPackagePath)

        SLog.d(TAG, "soClassModuleDir =" + soClassModuleDir)
        SLog.d(TAG, "soClassPackagePath =" + soClassPackagePath)
        SLog.d(TAG, "soClassFullPath =" + soClassFullDir)

        soClassFullDir.mkdirs()

        val soClassFullName = SOConfigProperties.SO_CLASS_FULL_NAME
        val soClassName = SOConfigProperties.SO_CLASS_NAME

        val soClassFile = File(soClassFullDir, soClassFullName)
        soClassFile.createNewFile()

        val str = StringBuilder()
            .add("package $soPackageName;")
            .add("\n")
            .add("// generate by DynamicSOPlugin")
            .add("public enum $soClassName {")

        soList.list.forEach { so ->
            val fieldName = Utils.humpToUnderline(
                so.name.replace("-", "_").replace(".", "")
            ).toUpperCase()
            str.add(
                "    ${fieldName}(" +
                        "${so.version}, " +
                        "\"${so.name}\", " +
                        "\"${so.arm64Hash}\", " +
                        "\"${so.armHash}\"," +
                        "\"${so.x86Hash}\"" +
                        "), "
            )
        }

        str.add("")
            .add("    ;")
            .add("    public final int version;")
            .add("    public final String name;")
            .add("    public final String arm64Hash;")
            .add("    public final String armHash;")
            .add("    public final String x86Hash;")
            .add("")
            .add("    YSO(int version, String name, String arm64Hash, String armHash, String x86Hash) {")
            .add("        this.version = version;")
            .add("        this.name = name;")
            .add("        this.arm64Hash = arm64Hash;")
            .add("        this.armHash = armHash;")
            .add("        this.x86Hash = x86Hash;")
            .add("    }")
            .add("}")

        writeString(str.toString(), soClassFile)
    }

    private fun writeString(text: String, file: File) {
        var output: FileOutputStream? = null
        var br: BufferedWriter? = null
        try {
            output = FileOutputStream(file, false)
            br = BufferedWriter(OutputStreamWriter(output))
            br.write(text)
            br.flush()

        } catch (t: Throwable) {
            SLog.d(TAG, "writeString:err,$t")
        }
    }
}

private fun StringBuilder.add(text: String): StringBuilder {
    this.append(text).append("\n")
    return this
}
