package com.wei.teachlink.core.designsystem.management.states.topappbar

abstract class FixedScrollFlagState(heightRange: IntRange) : ScrollFlagState(heightRange) {
    final override val offset: Float = 0f
}
