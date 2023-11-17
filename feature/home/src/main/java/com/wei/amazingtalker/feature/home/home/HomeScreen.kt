package com.wei.amazingtalker.feature.home.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.wei.amazingtalker.core.designsystem.component.FunctionalityNotAvailablePopup
import com.wei.amazingtalker.core.designsystem.icon.AtIcons
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.feature.home.R
import com.wei.amazingtalker.feature.home.home.ui.CircularProgressCard
import com.wei.amazingtalker.feature.home.home.ui.StatusCard

/**
 *
 * UI 事件決策樹
 * 下圖顯示了一個決策樹，用於查找處理特定事件用例的最佳方法。
 *
 *                                                      ┌───────┐
 *                                                      │ Start │
 *                                                      └───┬───┘
 *                                                          ↓
 *                                       ┌───────────────────────────────────┐
 *                                       │ Where is event originated?        │
 *                                       └──────┬─────────────────────┬──────┘
 *                                              ↓                     ↓
 *                                              UI                  ViewModel
 *                                              │                     │
 *                           ┌─────────────────────────┐      ┌───────────────┐
 *                           │ When the event requires │      │ Update the UI │
 *                           │ ...                     │      │ State         │
 *                           └─┬─────────────────────┬─┘      └───────────────┘
 *                             ↓                     ↓
 *                        Business logic      UI behavior logic
 *                             │                     │
 *     ┌─────────────────────────────────┐   ┌──────────────────────────────────────┐
 *     │ Delegate the business logic to  │   │ Modify the UI element state in the   │
 *     │ the ViewModel                   │   │ UI directly                          │
 *     └─────────────────────────────────┘   └──────────────────────────────────────┘
 *
 *
 */
@Composable
internal fun HomeRoute(
    navController: NavController,
    tokenInvalidNavigate: () -> Unit,
) {
    HomeScreen()
}

@Composable
internal fun HomeScreen(
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
) {
    val showPopup = remember { mutableStateOf(false) }

    if (showPopup.value) {
        FunctionalityNotAvailablePopup(
            onDismiss = {
                showPopup.value = false
            },
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (withTopSpacer) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
            val horizontalBasePadding = Modifier.padding(horizontal = 16.dp)
            HomeTopBar(
                modifier = horizontalBasePadding,
                onAddUserClick = {
                    /*TODO*/
                    showPopup.value = true
                },
                onMyHeadShotClick = {
                    /*TODO*/
                    showPopup.value = true
                },
                onMenuClick = {
                    /*TODO*/
                    showPopup.value = true
                },
            )
            HomeTabRow(
                selectedTabIndex = 0,
                onTabSelected = { },
            )

            LazyColumn {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = horizontalBasePadding) {
                        CourseProgressCard(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                showPopup.value = true
                            },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        PupilRatingCard(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                showPopup.value = true
                            },
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TutorProfileCard(
                        modifier = horizontalBasePadding,
                        onTutorClick = {
                            showPopup.value = true
                        },
                        onClick = {
                            showPopup.value = true
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = horizontalBasePadding) {
                        val cardSize = (64 * 2) + (8 * 2) + 4
                        val cardSizeModifier = Modifier.size(cardSize.dp)
                        ContactListCard(modifier = cardSizeModifier)
                        Spacer(modifier = Modifier.width(8.dp))

                        val progress = 64
                        CircularProgressCard(
                            modifier = cardSizeModifier.weight(1f),
                            progress = progress,
                            onClick = {
                                showPopup.value = true
                            },
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            if (withBottomSpacer) {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    onMyHeadShotClick: () -> Unit,
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
                MyHeadShot(
                    name = "He, Xuan-Wei",
                    isPreview = true,
                    onMyHeadShotClick = onMyHeadShotClick,
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
internal fun MyHeadShot(
    modifier: Modifier = Modifier,
    name: String,
    isPreview: Boolean,
    onMyHeadShotClick: () -> Unit,
) {
    val resId = R.drawable.he_wei
    val painter = loadImageUsingCoil(resId, isPreview)
    val profilePictureDescription = stringResource(R.string.profile_picture).format(name)

    IconButton(
        onClick = onMyHeadShotClick,
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
internal fun ContactHeadShot(
    modifier: Modifier = Modifier,
    avatarId: Int,
    name: String,
    isPreview: Boolean,
) {
    val painter = loadImageUsingCoil(avatarId, isPreview)

    val profilePictureDescription = stringResource(R.string.profile_picture).format(name)
    Image(
        painter = painter,
        contentDescription = profilePictureDescription,
        modifier = modifier
            .clip(CircleShape)
            .size(64.dp),
    )
}

@Composable
fun loadImageUsingCoil(resId: Int, isPreview: Boolean): Painter {
    val imageLoader = ImageLoader.Builder(LocalContext.current).components {
        add(SvgDecoder.Factory())
    }.build()
    val request = ImageRequest.Builder(LocalContext.current).data(resId).build()
    return if (isPreview) {
        painterResource(id = resId)
    } else {
        rememberAsyncImagePainter(request, imageLoader)
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

@Composable
fun HomeTabRow(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 16.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        val showPopup = remember { mutableStateOf(false) }

        if (showPopup.value) {
            FunctionalityNotAvailablePopup(
                onDismiss = {
                    showPopup.value = false
                },
            )
        }

        Tab(selected = true, onClick = { /*TODO*/ }) {
            Text(
                text = "My Courses",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp),
            )
        }
        Tab(
            selected = false,
            onClick = {
                /*TODO*/
                showPopup.value = true
            },
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Chats",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(color = MaterialTheme.colorScheme.primary),
                ) {
                    Text(
                        text = "10",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                    )
                }
            }
        }
        Tab(
            selected = false,
            onClick = {
                /*TODO*/
                showPopup.value = true
            },
        ) {
            Text(
                text = "Tutors",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
fun CourseProgressCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    StatusCard(
        modifier = modifier,
        onClick = onClick,
        content = {
            Row {
                Text(
                    text = "Completed",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = " (20%)",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = "14",
                style = MaterialTheme.typography.headlineSmall,
            )
        },
    )
}

@Composable
fun PupilRatingCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    StatusCard(
        modifier = modifier,
        onClick = onClick,
        content = {
            Row {
                Text(
                    text = "Pupil",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = " Rating",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = AtIcons.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "9.9",
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
        },
    )
}

@Composable
fun TutorProfileCard(
    modifier: Modifier = Modifier,
    onTutorClick: () -> Unit,
    onClick: () -> Unit,
) {
    TutorCard(
        modifier = modifier,
        content = {
            TutorButton(
                tutorName = "jamie-coleman",
                onTutorClick = onTutorClick,
            )
            Spacer(modifier = Modifier.height(8.dp))
            ClassName(className = "English Grammar")
            Spacer(modifier = Modifier.height(8.dp))
            ClassInfo(
                lessons = "30+",
                rating = "4.9",
                started = "11.04",
            )
            Spacer(modifier = Modifier.height(8.dp))
        },
        onClick = onClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = 24.dp),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            content = {
                content()
            },
        )
    }
}

@Composable
private fun ClassInfo(
    lessons: String,
    rating: String,
    started: String,
) {
    Row(
        modifier = Modifier.padding(horizontal = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "Lessons ",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                text = lessons,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "Rating ",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                text = rating,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "Started ",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                text = started,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun ClassName(className: String) {
    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            text = className,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.weight(1.5f),
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun TutorButton(
    tutorName: String,
    onTutorClick: () -> Unit,
) {
    Button(
        onClick = onTutorClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        contentPadding = ButtonDefaults.TextButtonContentPadding,
    ) {
        Icon(
            imageVector = AtIcons.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
        Text(
            text = "Tutor ",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = tutorName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun ContactListCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = 24.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSecondary,
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            content = {
                Row {
                    ContactHeadShot(
                        name = "jamie-coleman",
                        avatarId = R.drawable.jamie_coleman,
                        isPreview = true,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    ContactHeadShot(
                        name = "contact tutor 1",
                        avatarId = R.drawable.img_face_01,
                        isPreview = true,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    ContactHeadShot(
                        name = "contact tutor 2",
                        avatarId = R.drawable.img_face_02,
                        isPreview = true,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    ContactHeadShot(
                        name = "contact tutor 3",
                        avatarId = R.drawable.img_face_03,
                        isPreview = true,
                    )
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AtTheme {
        HomeScreen()
    }
}
