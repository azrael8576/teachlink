package com.wei.teachlink.core.data.test

import com.wei.teachlink.core.data.utils.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AlwaysOnlineNetworkMonitor
@Inject
constructor() : NetworkMonitor {
    override val isOnline: Flow<Boolean> = flowOf(true)
}
