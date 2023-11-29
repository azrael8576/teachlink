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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wei.amazingtalker.core.designsystem.component.ThemePreviews
import com.wei.amazingtalker.core.designsystem.component.coilImagePainter
import com.wei.amazingtalker.core.designsystem.icon.AtIcons
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.core.designsystem.theme.SPACING_LARGE
import com.wei.amazingtalker.core.designsystem.theme.SPACING_SMALL
import com.wei.amazingtalker.feature.home.R

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    userName: String,
    avatarId: Int,
    isPreview: Boolean,
    onUserProfileImageClick: () -> Unit,
    onAddUserClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    Column(modifier = modifier.padding(top = SPACING_LARGE.dp)) {
        Row {
            Box {
                AddUserButton(
                    modifier = Modifier.offset(x = (48 - 12).dp),
                    onAddUserClick = onAddUserClick,
                )
                UserAvatar(
                    userName = userName,
                    avatarId = avatarId,
                    isPreview = isPreview,
                    onUserProfileImageClick = onUserProfileImageClick,
                )
            }
            Spacer(modifier = modifier.weight(1f))
            MenuButton(onMenuClick = onMenuClick)
        }
        Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
        val helloUserName = stringResource(R.string.hello, userName)

        Text(
            text = helloUserName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.semantics { contentDescription = helloUserName },
        )
        Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
    }
}

@Composable
private fun AddUserButton(
    modifier: Modifier = Modifier,
    onAddUserClick: () -> Unit,
) {
    Box(modifier = modifier) {
        val addUser = stringResource(R.string.add_user)

        IconButton(
            onClick = {
                onAddUserClick()
            },
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .semantics { contentDescription = addUser },
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
internal fun UserAvatar(
    modifier: Modifier = Modifier,
    userName: String,
    avatarId: Int,
    isPreview: Boolean,
    onUserProfileImageClick: () -> Unit,
) {
    val painter = coilImagePainter(avatarId, isPreview)
    val profilePictureDescription = stringResource(R.string.profile_picture).format(userName)

    IconButton(
        onClick = onUserProfileImageClick,
        modifier = modifier
            .size(48.dp)
            .semantics {
                contentDescription = profilePictureDescription
            },
    ) {
        Image(
            painter = painter,
            contentDescription = null,
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

@ThemePreviews
@Composable
fun HomeTopBarPreview() {
    AtTheme {
        Surface {
            HomeTopBar(
                modifier = Modifier.padding(horizontal = SPACING_LARGE.dp),
                userName = "TEST_NAME",
                avatarId = R.drawable.he_wei,
                isPreview = true,
                onUserProfileImageClick = {},
                onAddUserClick = {},
                onMenuClick = {},
            )
        }
    }
}
