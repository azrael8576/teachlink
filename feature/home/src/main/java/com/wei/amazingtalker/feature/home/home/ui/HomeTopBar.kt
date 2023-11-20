package com.wei.amazingtalker.feature.home.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wei.amazingtalker.core.designsystem.icon.AtIcons
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.feature.home.R
import com.wei.amazingtalker.feature.home.home.loadImageUsingCoil

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    onUserProfileImageClick: () -> Unit,
    onAddUserClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    Column(modifier = modifier.padding(top = 16.dp)) {
        Row {
            Box {
                AddUserButton(
                    modifier = Modifier.offset(x = (48 - 12).dp),
                    onAddUserClick = onAddUserClick,
                )
                UserProfileImage(
                    name = "He, Xuan-Wei",
                    isPreview = true,
                    onUserProfileImageClick = onUserProfileImageClick,
                )
            }
            Spacer(modifier = modifier.weight(1f))
            MenuButton(onMenuClick = onMenuClick)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Hello, Wei",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { contentDescription = "" },
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun AddUserButton(
    modifier: Modifier = Modifier,
    onAddUserClick: () -> Unit,
) {
    Box(modifier = modifier) {
        IconButton(
            onClick = {
                onAddUserClick()
            },
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .semantics { contentDescription = "" },
        ) {
            Icon(
                imageVector = AtIcons.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
internal fun UserProfileImage(
    modifier: Modifier = Modifier,
    name: String,
    isPreview: Boolean,
    onUserProfileImageClick: () -> Unit,
) {
    val resId = R.drawable.he_wei
    val painter = loadImageUsingCoil(resId, isPreview)
    val profilePictureDescription = stringResource(R.string.profile_picture).format(name)

    IconButton(
        onClick = onUserProfileImageClick,
        modifier = modifier.size(48.dp),
    ) {
        Image(
            painter = painter,
            contentDescription = profilePictureDescription,
            modifier = modifier
                .clip(CircleShape)
                .fillMaxSize(),
        )
    }
}

@Composable
private fun MenuButton(
    onMenuClick: () -> Unit,
) {
    val menu = stringResource(R.string.menu)

    IconButton(
        onClick = onMenuClick,
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .semantics { contentDescription = menu },
    ) {
        Icon(
            imageVector = AtIcons.Menu,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarPreview() {
    AtTheme {
        HomeTopBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            onUserProfileImageClick = {},
            onAddUserClick = {},
            onMenuClick = {},
        )
    }
}
