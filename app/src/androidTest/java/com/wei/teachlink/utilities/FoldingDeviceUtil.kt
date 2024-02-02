package com.wei.teachlink.utilities

import android.graphics.Rect
import androidx.compose.ui.unit.DpSize
import androidx.window.layout.FoldingFeature

object FoldingDeviceUtil {
    fun getFoldBounds(dpSize: DpSize): Rect {
        val middleWidth = (dpSize.width / 2f).value.toInt()
        return Rect(
            middleWidth,
            0,
            middleWidth,
            dpSize.height.value.toInt(),
        )
    }

    fun getFoldingFeature(
        foldBounds: Rect,
        state: FoldingFeature.State,
    ): FoldingFeature {
        return object : FoldingFeature {
            override val bounds: Rect = foldBounds
            override val isSeparating: Boolean = true
            override val occlusionType: FoldingFeature.OcclusionType =
                FoldingFeature.OcclusionType.NONE
            override val orientation: FoldingFeature.Orientation =
                FoldingFeature.Orientation.VERTICAL
            override val state: FoldingFeature.State = state
        }
    }
}
