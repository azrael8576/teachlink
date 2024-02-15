package com.wei.teachlink.feature.home.home.ui

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wei.teachlink.core.designsystem.component.ThemePreviews
import com.wei.teachlink.core.designsystem.component.coilImagePainter
import com.wei.teachlink.core.designsystem.theme.SPACING_EXTRA_SMALL
import com.wei.teachlink.core.designsystem.theme.SPACING_MEDIUM
import com.wei.teachlink.core.designsystem.theme.TlTheme
import com.wei.teachlink.core.designsystem.theme.shapes
import com.wei.teachlink.feature.home.R
import com.wei.teachlink.feature.home.home.Contact
import com.wei.teachlink.feature.home.home.OnlineStatus
import com.wei.teachlink.feature.home.home.utilities.BusyColor
import com.wei.teachlink.feature.home.home.utilities.CONTACT_HEAD_SHOT_SIZE
import com.wei.teachlink.feature.home.home.utilities.FreeColor
import com.wei.teachlink.feature.home.home.utilities.OfflineColor
import com.wei.teachlink.feature.home.home.utilities.TestContacts

@Composable
fun ContactCard(
    modifier: Modifier = Modifier,
    contacts: List<Contact>,
    isPreview: Boolean,
) {
    val maxDisplayContacts = 4
    val contactsPerRow = 2
    val spacingSize = SPACING_EXTRA_SMALL.dp
    val contactCard = stringResource(R.string.feature_home_contact_card)

    Card(
        modifier =
            modifier
                .semantics {
                    contentDescription = contactCard
                },
        shape = shapes.extraLarge,
        colors =
            CardDefaults.cardColors(
                contentColor = MaterialTheme.colorScheme.onSecondary,
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
    ) {
        Column(
            modifier = Modifier.padding(SPACING_MEDIUM.dp),
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
                                isPreview = isPreview,
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
    isPreview: Boolean,
) {
    val painter = coilImagePainter(avatarId, true)
    val profilePictureDescription = stringResource(R.string.feature_home_profile_picture).format(name)

    Box(
        modifier =
            modifier
                .size(CONTACT_HEAD_SHOT_SIZE.dp)
                .statusIndicator(status),
    ) {
        Image(
            painter = painter,
            contentDescription = profilePictureDescription,
            modifier =
                Modifier
                    .matchParentSize()
                    .clip(CircleShape),
        )
    }
}

fun Modifier.statusIndicator(
    status: OnlineStatus,
    canvasSize: Dp = 16.dp,
    statusIndicatorOffset: Dp = 3.dp,
    offlineStatusIndicatorOffset: Dp = 6.dp,
): Modifier =
    composed {
        val canvasBackground = MaterialTheme.colorScheme.secondary

        this.then(
            drawWithContent {
                drawContent()

                val circleColor =
                    when (status) {
                        OnlineStatus.FREE -> FreeColor
                        OnlineStatus.BUSY -> BusyColor
                        OnlineStatus.OFFLINE -> OfflineColor
                    }

                val radius = size.minDimension.coerceAtMost(canvasSize.toPx()) / 2
                val center = Offset(size.width - radius, size.height - radius)

                drawCircle(
                    color = canvasBackground,
                    radius = radius,
                    center = center,
                )

                drawCircle(
                    color = circleColor,
                    radius = radius - statusIndicatorOffset.toPx(),
                    center = center,
                )

                if (status == OnlineStatus.OFFLINE) {
                    drawCircle(
                        color = canvasBackground,
                        radius = radius - offlineStatusIndicatorOffset.toPx(),
                        center = center,
                    )
                }
            },
        )
    }

@Composable
fun PlaceholderAvatar(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.secondary)
                .size(CONTACT_HEAD_SHOT_SIZE.dp),
    )
}

@ThemePreviews
@Composable
fun ContactCardPreview() {
    val cardSize = (CONTACT_HEAD_SHOT_SIZE * 2) + (SPACING_MEDIUM * 2) + 4

    TlTheme {
        ContactCard(
            modifier = Modifier.size(cardSize.dp),
            contacts = TestContacts,
            isPreview = true,
        )
    }
}
