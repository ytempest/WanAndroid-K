package com.ytempest.dynamic_so.protocol

class BssUploadResult {
    var status = 0
    var errorCode = 0
    var bucket: String? = null
    var filename: String? = null
    var hash: String? = null
    var tag: String? = null

    override fun toString(): String {
        return "BssUploadResult(status=$status, errorCode=$errorCode, bucket=$bucket, filename=$filename, hash=$hash, tag=$tag)"
    }
}