package com.wei.amazingtalker_recruit.core.authentication

/**
 * 用於管理用戶的 token 狀態。
 * 其中的 isTokenValid 屬性代表當前 token 是否有效。
 */
object TokenManager {
    var isTokenValid: Boolean = false
        private set

    /**
     * 將當前的 token 標記為無效。
     */
    fun invalidateToken() {
        isTokenValid = false
    }

    /**
     * 將當前的 token 標記為有效。
     */
    fun validateToken() {
        isTokenValid = true
    }
}
