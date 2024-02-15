package com.wei.teachlink.feature.home.home.ui

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wei.teachlink.core.designsystem.component.ThemePreviews
import com.wei.teachlink.core.designsystem.component.coilImagePainter
import com.wei.teachlink.core.designsystem.icon.TlIcons
import com.wei.teachlink.core.designsystem.theme.SPACING_LARGE
import com.wei.teachlink.core.designsystem.theme.SPACING_SMALL
import com.wei.teachlink.core.designsystem.theme.TlTheme
import com.wei.teachlink.feature.home.R

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    userName: String,
    avatarId: Int,
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
                    onUserProfileImageClick = onUserProfileImageClick,
                )
            }
            Spacer(modifier = modifier.weight(1f))
            MenuButton(onMenuClick = onMenuClick)
        }
        Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
        val helloUserName = stringResource(R.string.feature_home_hello, userName)

        Text(
            text = helloUserName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Normal,
            modifier =
                Modifier
                    .testTag(stringResource(R.string.feature_home_tag_hello_user_name_text))
                    .semantics { contentDescription = helloUserName },
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
        val addUser = stringResource(R.string.feature_home_add_user)

        IconButton(
            onClick = {
                onAddUserClick()
            },
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .semantics { contentDescription = addUser },
        ) {
            Icon(
                imageVector = TlIcons.Add,
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
    onUserProfileImageClick: () -> Unit,
) {
    val painter = coilImagePainter(avatarId, true)
    val profilePictureDescription = stringResource(R.string.feature_home_profile_picture).format(userName)

    IconButton(
        onClick = onUserProfileImageClick,
        modifier =
            modifier
                .size(48.dp)
                .testTag(stringResource(R.string.feature_home_tag_user_avatar))
                .semantics {
                    contentDescription = profilePictureDescription
                },
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier =
                modifier
                    .clip(CircleShape)
                    .fillMaxSize(),
        )
    }
}

@Composable
private fun MenuButton(onMenuClick: () -> Unit) {
    val menu = stringResource(R.string.feature_home_menu)

    IconButton(
        onClick = onMenuClick,
        modifier =
            Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .semantics { contentDescription = menu },
    ) {
        Icon(
            imageVector = TlIcons.Menu,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
        )
    }
}

@ThemePreviews
@Composable
fun HomeTopBarPreview() {
    TlTheme {
        Surface {
            HomeTopBar(
                modifier = Modifier.padding(horizontal = SPACING_LARGE.dp),
                userName = "TEST_NAME",
                avatarId = R.drawable.feature_home_he_wei,
                onUserProfileImageClick = {},
                onAddUserClick = {},
                onMenuClick = {},
            )
        }
    }
}
