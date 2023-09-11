package com.wei.amazingtalker_recruit.core.authentication

/**
 * Responsible for managing the state of user's token.
 * The `isTokenValid` property indicates the validity of the current token.
 */
object TokenManager {
    var tokenState: TokenState = TokenState.Loading
        private set

    /**
     * Mark the current token as invalid.
     */
    private fun invalidateToken() {
        tokenState = TokenState.Invalid
    }

    /**
     * Validate and set the state of the token.
     */
    fun validateToken(token: String) {
        if (token.isNotEmpty()) {
            tokenState = TokenState.Valid(token)
        } else {
            invalidateToken()
        }
    }
}

sealed interface TokenState {
    data class Valid(val token: String) : TokenState
    object Invalid : TokenState
    object Loading : TokenState
}
