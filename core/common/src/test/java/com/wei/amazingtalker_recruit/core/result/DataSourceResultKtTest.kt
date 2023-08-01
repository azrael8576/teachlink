package com.wei.amazingtalker_recruit.core.result

import app.cash.turbine.test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [DataSourceResult.kt].
 * 此測試案例透過一個 flow 來模擬數據源的行為。首先，它發出一個 Loading 狀態，然後發出一個成功狀態，最後觸發一個異常以模擬錯誤狀態。
 *
 * 測試過程如下：
 * 1. 首先驗證是否接收到 Loading 狀態。
 * 2. 驗證成功狀態是否包含正確的數值。
 * 3. 驗證錯誤狀態是否包含正確的異常訊息。
 * 4. 完成測試並確保上述步驟的流程完整。
 *
 * 如果接收到的任何項目不符合預期，則會拋出異常，並使測試案例失敗。
 */
class DataSourceResultKtTest {

    @Test
    fun `dataSourceResult catches errors`() = runTest {
        flow {
            emit(1)
            throw Exception("Test Done")
        }
            .asDataSourceResult()
            .test {
                assertEquals(DataSourceResult.Loading, awaitItem())
                assertEquals(DataSourceResult.Success(1), awaitItem())

                when (val errorResult = awaitItem()) {
                    is DataSourceResult.Error -> assertEquals(
                        "Test Done",
                        errorResult.exception?.message,
                    )
                    DataSourceResult.Loading,
                    is DataSourceResult.Success,
                    -> throw IllegalStateException(
                        "The flow should have emitted an Error Result",
                    )
                }

                awaitComplete()
            }
    }
}
