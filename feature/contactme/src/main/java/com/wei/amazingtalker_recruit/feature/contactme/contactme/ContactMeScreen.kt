package com.wei.amazingtalker_recruit.feature.contactme.contactme

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.wei.amazingtalker_recruit.core.designsystem.component.ThemePreviews
import com.wei.amazingtalker_recruit.core.designsystem.theme.AtTheme
import com.wei.amazingtalker_recruit.feature.contactme.R
import com.wei.amazingtalker_recruit.feature.contactme.utilities.NAME_ENG

/**
 *
 * UI 事件決策樹
 * 下圖顯示了一個決策樹，用於查找處理特定事件用例的最佳方法。
 *
 *                                                      ┌───────┐
 *                                                      │ Start │
 *                                                      └───┬───┘
 *                                                          ↓
 *                                       ┌───────────────────────────────────┐
 *                                       │ Where is event originated?        │
 *                                       └──────┬─────────────────────┬──────┘
 *                                              ↓                     ↓
 *                                              UI                  ViewModel
 *                                              │                     │
 *                           ┌─────────────────────────┐      ┌───────────────┐
 *                           │ When the event requires │      │ Update the UI │
 *                           │ ...                     │      │ State         │
 *                           └─┬─────────────────────┬─┘      └───────────────┘
 *                             ↓                     ↓
 *                        Business logic      UI behavior logic
 *                             │                     │
 *     ┌─────────────────────────────────┐   ┌──────────────────────────────────────┐
 *     │ Delegate the business logic to  │   │ Modify the UI element state in the   │
 *     │ the ViewModel                   │   │ UI directly                          │
 *     └─────────────────────────────────┘   └──────────────────────────────────────┘
 *
 *
 */
@Composable
internal fun ContactMeRoute(
    navController: NavController
) {
    ContactMeScreen()
}

@Composable
internal fun ContactMeScreen(
    isPreview: Boolean = true
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            DecorativeBackgroundText(text = NAME_ENG, repetitions = 3)
            DisplayHeadShot(isPreview = isPreview)
        }
    }
}

@Composable
internal fun DecorativeBackgroundText(
    text: String,
    repetitions: Int
) {
    val textStyle = TextStyle(
        fontSize = MaterialTheme.typography.displayLarge.fontSize,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        textAlign = TextAlign.Center,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    )

    Column(modifier = Modifier.graphicsLayer(rotationZ = -25f).scale(2.5f)) {
        repeat(repetitions) {
            Text(text = text, style = textStyle)
        }
    }
}

@Composable
internal fun DisplayHeadShot(
    isPreview: Boolean,
    modifier: Modifier = Modifier
) {
    val resId = R.drawable.he_wei
    val painter = loadImageUsingCoil(resId, isPreview)

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .size(300.dp)
            .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
    )
}

@Composable
fun loadImageUsingCoil(resId: Int, isPreview: Boolean): Painter {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    val request = ImageRequest.Builder(LocalContext.current)
        .data(resId)
        .build()
    return if (isPreview) {
        painterResource(id = resId)
    } else {
        rememberAsyncImagePainter(request, imageLoader)
    }
}

@ThemePreviews
@Composable
fun ContactMeScreenPreview() {
    AtTheme {
        ContactMeScreen()
    }
}