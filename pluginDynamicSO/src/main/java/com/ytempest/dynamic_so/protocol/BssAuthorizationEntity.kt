package com.ytempest.dynamic_so.protocol

class BssAuthorizationEntity {
    var status = 0
    var errorCode = 0
    var authorization: String? = null
    var exceptionMsg: String? = null

    override fun toString(): String {
        return "BssAuthorizationEntity(status=$status, errorCode=$errorCode, authorization=$authorization, exceptionMsg=$exceptionMsg)"
    }
}