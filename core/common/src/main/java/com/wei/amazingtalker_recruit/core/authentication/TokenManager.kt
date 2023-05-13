package com.wei.amazingtalker_recruit.core.authentication

object TokenManager {
    var isTokenValid: Boolean = false
        private set

    fun invalidateToken() {
        isTokenValid = false
    }

    fun validateToken() {
        isTokenValid = true
    }
}
