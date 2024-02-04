package com.wei.teachlink.feature.contactme.contactme.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.wei.teachlink.core.designsystem.component.baselineHeight
import com.wei.teachlink.core.designsystem.theme.SPACING_EXTRA_LARGE
import com.wei.teachlink.core.designsystem.theme.SPACING_MEDIUM

@Composable
fun ProfileProperty(
    label: String,
    value: String,
    isLink: Boolean = false,
) {
    Column(modifier = Modifier.padding(vertical = SPACING_MEDIUM.dp)) {
        Divider(color = MaterialTheme.colorScheme.outline)
        Text(
            text = label,
            modifier =
            Modifier
                .baselineHeight(SPACING_EXTRA_LARGE.dp)
                .semantics { contentDescription = label },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        val style =
            if (isLink) {
                MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
            } else {
                MaterialTheme.typography.bodyLarge
            }
        Text(
            text = value,
            modifier =
            Modifier
                .baselineHeight(SPACING_EXTRA_LARGE.dp)
                .semantics { contentDescription = value },
            style = style,
        )
    }
}
