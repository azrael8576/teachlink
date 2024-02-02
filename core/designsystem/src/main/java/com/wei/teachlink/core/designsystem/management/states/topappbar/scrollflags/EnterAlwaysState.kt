package com.wei.teachlink.core.designsystem.management.states.topappbar.scrollflags

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import com.wei.teachlink.core.designsystem.management.states.topappbar.ScrollFlagState

class EnterAlwaysState(
    heightRange: IntRange,
    scrollOffset: Float = 0f,
) : ScrollFlagState(heightRange) {
    override var mScrollOffset by mutableStateOf(
        value = scrollOffset.coerceIn(0f, maxHeight.toFloat()),
        policy = structuralEqualityPolicy(),
    )

    override val offset: Float
        get() = -(scrollOffset - rangeDifference).coerceIn(0f, minHeight.toFloat())

    override var scrollOffset: Float
        get() = mScrollOffset
        set(value) {
            val oldOffset = mScrollOffset
            mScrollOffset = value.coerceIn(0f, maxHeight.toFloat())
            mConsumed = oldOffset - mScrollOffset
        }

    companion object {
        val Saver =
            run {

                val minHeightKey = "MinHeight"
                val maxHeightKey = "MaxHeight"
                val scrollOffsetKey = "ScrollOffset"

                mapSaver(
                    save = {
                        mapOf(
                            minHeightKey to it.minHeight,
                            maxHeightKey to it.maxHeight,
                            scrollOffsetKey to it.scrollOffset,
                        )
                    },
                    restore = {
                        EnterAlwaysState(
                            heightRange = (it[minHeightKey] as Int)..(it[maxHeightKey] as Int),
                            scrollOffset = it[scrollOffsetKey] as Float,
                        )
                    },
                )
            }
    }
}
