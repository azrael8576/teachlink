package com.wei.amazingtalker.feature.home.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wei.amazingtalker.core.designsystem.theme.shapes
import com.wei.amazingtalker.core.designsystem.theme.spacing_large

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shapes.extraLarge,
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(spacing_large.dp)) {
            content()
        }
    }
}
