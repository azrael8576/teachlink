package com.wei.amazingtalker.feature.contactme.contactme.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
internal fun DecorativeBackgroundText(
    text: String,
    repetitions: Int,
    rotationZ: Float = 0f,
    scale: Float = 1f,
    textStyle: TextStyle = TextStyle(
        fontSize = MaterialTheme.typography.displayLarge.fontSize,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        textAlign = TextAlign.Center,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
    ),
) {
    Column(
        modifier = Modifier
            .graphicsLayer(rotationZ = rotationZ)
            .scale(scale),
    ) {
        repeat(repetitions) {
            Text(
                text = text,
                style = textStyle,
                modifier = Modifier.semantics { contentDescription = "" },
            )
        }
    }
}

@Composable
fun decorativeTextStyle(color: Color) = TextStyle(
    fontSize = MaterialTheme.typography.displayLarge.fontSize,
    color = color.copy(alpha = 0.8f),
    textAlign = TextAlign.Center,
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
)
