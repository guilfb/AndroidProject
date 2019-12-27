package com.example.tp2.adapter

import java.security.MessageDigest

class StringToHash {

    companion object StringToHash {
        fun stringToHash(string: String): String {
            var result = ""
            try {
                val md5 = MessageDigest.getInstance("MD5")
                val md5HashBytes = md5.digest(string.toByteArray())
                result = md5HashBytes.fold("", { str, it -> str + "%02x".format(it) })
            } catch(e: Exception) {
                result = "error: ${e.message}"
            }
            return result
        }
    }

}
