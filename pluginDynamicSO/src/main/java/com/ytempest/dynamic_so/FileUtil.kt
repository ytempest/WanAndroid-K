package com.ytempest.dynamic_so

import com.ytempest.dynamic_so.util.SLog
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.RandomAccessFile
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object FileUtil {

    private const val TAG = "FileUtil"

    @JvmStatic
    fun getFileSimpleName(file: File): String {
        val fileName = file.name
        return fileName.substring(0, fileName.length - 3)
    }

    @JvmStatic
    fun getSOSimpleName(file: File): String {
        val fileName = file.name
        return fileName.substring(3, fileName.length - 3)
    }

    @JvmStatic
    fun readData(filePath: String): ByteArray {
        var content: ByteArray? = null
        val file = File(filePath)
        if (file.exists() && file.isFile) {
            var inputStream: FileInputStream? = null
            try {
                content = ByteArray(file.length().toInt())
                inputStream = FileInputStream(file)
                val len = inputStream.read(content)
                if (len == 0) {
                    content = null
                }
            } catch (e: Exception) {
                content = null
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (_: Exception) {

                    }
                }
            }
        }
        if (content == null) {
            content = ByteArray(0)
        }
        return content
    }


    @JvmStatic
    fun isExist(file: File?): Boolean {
        return file != null && file.exists()
    }

    @JvmStatic
    fun delete(file: File?): Boolean {
        return file != null && file.delete()
    }


    @JvmStatic
    fun readJson(file: File): String {
        var input: FileInputStream? = null
        var br: BufferedReader? = null
        try {
            val builder = StringBuilder()
            input = FileInputStream(file)
            br = BufferedReader(InputStreamReader(input, "UTF-8"))
            var str: String?
            while (br.readLine().also { str = it } != null) {
                builder.append(str)
            }
            return builder.toString()

        } catch (t: Throwable) {
            SLog.d(TAG, "readJson:err,$t")
        }
        return ""
    }

    @JvmStatic
    fun writeJson(json: String, file: File) {
        var output: FileOutputStream? = null
        var br: BufferedWriter? = null
        try {
            output = FileOutputStream(file, false)
            br = BufferedWriter(OutputStreamWriter(output))
            br.write(json)
            br.flush()

        } catch (t: Throwable) {
            SLog.d(TAG, "writeJson:err,$t")
        }
    }


    fun getMd5AndSuffix(file: File?): Array<String?> {
        val result = arrayOfNulls<String>(2)
        result[0] = ""
        result[1] = ""
        if (file == null || !file.exists()) {
            return result
        }
        result[0] = getFileMd5(file)
        val name = file.name
        val index = name.lastIndexOf(".")
        if (index != -1) {
            result[1] = name.substring(index, name.length)
        }
        return result
    }

    /*
     * 获取文件md5信息,使用RandomAccessFile，避免oom
     */
    @JvmStatic
    @Synchronized
    fun getFileMd5(file: File?): String? {
        val messageDigest: MessageDigest
        var randomAccessFile: RandomAccessFile? = null
        try {
            messageDigest = MessageDigest.getInstance("MD5")
            if (file == null) {
                return ""
            }
            if (!file.exists()) {
                return ""
            }
            randomAccessFile = RandomAccessFile(file, "r")
            val bytes = ByteArray(1024 * 1024 * 10)
            var len = 0
            while (randomAccessFile.read(bytes).also { len = it } != -1) {
                messageDigest.update(bytes, 0, len)
            }
            val bigInt = BigInteger(1, messageDigest.digest())
            var md5 = bigInt.toString(16)
            while (md5.length < 32) {
                md5 = "0$md5"
            }
            return md5
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close()
                    randomAccessFile = null
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }

}