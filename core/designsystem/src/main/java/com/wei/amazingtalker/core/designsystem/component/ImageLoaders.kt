package com.wei.amazingtalker.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest

/**
 * Returns a Painter for an image resource. Uses Coil for image loading, with support for SVG.
 * @param resId The resource ID of the image to load.
 * @param isPreview Whether to use a preview (simple resource loading) or full Coil loading.
 * @return A Painter object for the image.
 */
@Composable
fun coilImagePainter(resId: Int, isPreview: Boolean = false): Painter {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    val request = ImageRequest.Builder(context)
        .data(resId)
        .build()

    return if (isPreview) {
        painterResource(id = resId)
    } else {
        rememberAsyncImagePainter(request, imageLoader)
    }
}
