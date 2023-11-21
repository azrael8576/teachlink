package com.wei.amazingtalker.feature.home.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wei.amazingtalker.feature.home.home.utilities.CARD_CORNER_SIZE
import com.wei.amazingtalker.feature.home.home.utilities.LARGE_SPACING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = CARD_CORNER_SIZE.dp),
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(LARGE_SPACING.dp)) {
            content()
        }
    }
}
