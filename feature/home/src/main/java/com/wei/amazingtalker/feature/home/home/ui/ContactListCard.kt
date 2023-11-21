package com.wei.amazingtalker.feature.home.home.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wei.amazingtalker.core.designsystem.component.ThemePreviews
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.feature.home.R
import com.wei.amazingtalker.feature.home.home.Contact
import com.wei.amazingtalker.feature.home.home.OnlineStatus
import com.wei.amazingtalker.feature.home.home.loadImageUsingCoil
import com.wei.amazingtalker.feature.home.home.utilities.BusyColor
import com.wei.amazingtalker.feature.home.home.utilities.CARD_CORNER_SIZE
import com.wei.amazingtalker.feature.home.home.utilities.ContactHeadShotSize
import com.wei.amazingtalker.feature.home.home.utilities.DEFAULT_SPACING
import com.wei.amazingtalker.feature.home.home.utilities.FreeColor
import com.wei.amazingtalker.feature.home.home.utilities.OfflineColor
import com.wei.amazingtalker.feature.home.home.utilities.TestContacts

@Composable
fun ContactCard(
    modifier: Modifier = Modifier,
    contacts: List<Contact>,
) {
    val maxDisplayContacts = 4
    val contactsPerRow = 2
    val spacingSize = 4.dp

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = CARD_CORNER_SIZE.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSecondary,
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
    ) {
        Column(
            modifier = Modifier.padding(DEFAULT_SPACING.dp),
        ) {
            val displayContacts = contacts.take(maxDisplayContacts)
            for (i in 0 until maxDisplayContacts / contactsPerRow) {
                Row {
                    for (j in 0 until contactsPerRow) {
                        val contactIndex = i * contactsPerRow + j
                        displayContacts.getOrNull(contactIndex)?.let {
                            ContactAvatar(
                                name = it.name,
                                avatarId = it.avatarId,
                                status = it.status,
                            )
                        } ?: PlaceholderAvatar()

                        if (j < contactsPerRow - 1) {
                            Spacer(modifier = Modifier.width(spacingSize))
                        }
                    }
                }
                if (i < maxDisplayContacts / contactsPerRow - 1) {
                    Spacer(modifier = Modifier.height(spacingSize))
                }
            }
        }
    }
}

@Composable
internal fun ContactAvatar(
    modifier: Modifier = Modifier,
    name: String,
    avatarId: Int,
    status: OnlineStatus,
) {
    val painter = loadImageUsingCoil(avatarId, true)
    val profilePictureDescription = stringResource(R.string.profile_picture).format(name)
    val canvasSize = 16.dp
    val statusIndicatorOffset = 3.dp
    val offlineStatusIndicatorOffset = statusIndicatorOffset * 2

    Box(
        modifier = modifier
            .size(ContactHeadShotSize.dp),
    ) {
        Image(
            painter = painter,
            contentDescription = profilePictureDescription,
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape),
        )

        val canvasBackground = MaterialTheme.colorScheme.secondary

        Canvas(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(canvasSize),
        ) {
            val circleColor = when (status) {
                OnlineStatus.FREE -> FreeColor
                OnlineStatus.BUSY -> BusyColor
                OnlineStatus.OFFLINE -> OfflineColor
            }

            drawCircle(
                color = canvasBackground,
                radius = size.minDimension / 2,
                center = Offset(size.width / 2, size.height / 2),
            )

            drawCircle(
                color = circleColor,
                radius = size.minDimension / 2 - statusIndicatorOffset.toPx(),
                center = Offset(size.width / 2, size.height / 2),
            )

            if (status == OnlineStatus.OFFLINE) {
                drawCircle(
                    color = canvasBackground,
                    radius = size.minDimension / 2 - offlineStatusIndicatorOffset.toPx(),
                    center = Offset(size.width / 2, size.height / 2),
                )
            }
        }
    }
}

@Composable
fun PlaceholderAvatar(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .size(ContactHeadShotSize.dp),
    )
}

@ThemePreviews
@Composable
fun ContactCardPreview() {
    val cardSize = calculateCardSize()

    AtTheme {
        ContactCard(
            modifier = Modifier.size(cardSize.dp),
            contacts = TestContacts,
        )
    }
}
