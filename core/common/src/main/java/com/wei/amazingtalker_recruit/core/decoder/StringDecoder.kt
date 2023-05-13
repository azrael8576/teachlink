package com.wei.amazingtalker_recruit.core.decoder

/**
 * StringDecoder 是一個接口，定義了對字串進行解碼的操作。
 * 這個接口可以被任何需要進行字串解碼的類別實現。
 */
interface StringDecoder {
    /**
     * 對傳入的已編碼字串進行解碼。
     *
     * @param encodedString 要解碼的已編碼字串。
     * @return 解碼後的字串。
     */
    fun decodeString(encodedString: String): String
}
