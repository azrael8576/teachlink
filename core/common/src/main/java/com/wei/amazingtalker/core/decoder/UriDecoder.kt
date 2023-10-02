package com.wei.amazingtalker.core.decoder

import android.net.Uri
import javax.inject.Inject

/**
 * UriDecoder 是一個實現了 StringDecoder 接口的類別，它用來對 URI 進行解碼。
 */
class UriDecoder @Inject constructor() : StringDecoder {
    /**
     * 使用 Uri.decode 方法對傳入的已編碼字符串進行解碼。
     *
     * @param encodedString 要解碼的已編碼字符串。
     * @return 解碼後的字符串。
     */
    override fun decodeString(encodedString: String): String = Uri.decode(encodedString)
}
