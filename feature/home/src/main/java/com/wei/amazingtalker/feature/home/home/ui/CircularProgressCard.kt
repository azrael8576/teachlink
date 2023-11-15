package com.wei.amazingtalker.feature.home.home.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wei.amazingtalker.core.designsystem.theme.AtTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircularProgressCard(
    modifier: Modifier,
    progress: Int,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = 24.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = {
                Text(
                    text = "Business English",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Advanced level",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgress(modifier = Modifier.weight(1f), progress = progress)
            },
        )
    }
}

@Composable
private fun CircularProgress(modifier: Modifier = Modifier, progress: Int) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        val diameter = minOf(maxWidth, maxHeight)
        val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
        val outlineVariantColor =
            MaterialTheme.colorScheme.outlineVariant

        Canvas(modifier = Modifier.size(diameter)) {
            val canvasWidth = size.width
            drawCircle(
                color = outlineVariantColor,
                radius = canvasWidth / 2,
                style = Stroke(width = 4.dp.toPx()),
            )
            drawArc(
                color = onPrimaryColor,
                startAngle = -90f,
                sweepAngle = (360 * (64 / 100.0)).toFloat(),
                useCenter = false,
                style = Stroke(width = 4.dp.toPx()),
            )
        }
        Text(
            text = "$progress%",
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AtTheme {
        CircularProgressCard(
            modifier = Modifier
                .width(200.dp)
                .height(152.dp),
            progress = 64,
            onClick = {},
        )
    }
}
