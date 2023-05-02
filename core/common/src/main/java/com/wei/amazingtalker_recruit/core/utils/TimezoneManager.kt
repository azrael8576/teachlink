package com.wei.amazingtalker_recruit.core.utils

import java.time.ZoneId

object TimezoneManager {

    // 獲取系統當前的默認時區
    fun getCurrentTimezone(): ZoneId {
        return ZoneId.systemDefault()
    }

    // 更新系统的默認時區
    fun updateTimezone(zoneId: ZoneId) {
        // 如果您需要更新系統的默認時區，可以在此處添加邏輯。
        // 但請注意，這通常不建議在應用程序中做，因為它可能影響到其他部分的代碼。
        // 作為替代方案，您可以將新時區存儲在應用程序的配置中，並在需要時使用它。
    }
}