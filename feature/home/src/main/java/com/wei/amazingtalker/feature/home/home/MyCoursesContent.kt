package com.wei.amazingtalker.feature.home.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wei.amazingtalker.core.designsystem.component.ThemePreviews
import com.wei.amazingtalker.core.designsystem.icon.AtIcons
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.core.designsystem.theme.SPACING_EXTRA_SMALL
import com.wei.amazingtalker.core.designsystem.theme.SPACING_LARGE
import com.wei.amazingtalker.core.designsystem.theme.SPACING_SMALL
import com.wei.amazingtalker.core.designsystem.theme.shapes
import com.wei.amazingtalker.feature.home.R
import com.wei.amazingtalker.feature.home.home.ui.ContactCard
import com.wei.amazingtalker.feature.home.home.ui.SkillProgressCard
import com.wei.amazingtalker.feature.home.home.ui.StatusCard
import com.wei.amazingtalker.feature.home.home.utilities.CONTACT_HEAD_SHOT_SIZE
import com.wei.amazingtalker.feature.home.home.utilities.TEST_CLASS_NAME
import com.wei.amazingtalker.feature.home.home.utilities.TEST_SKILL_LEVEL
import com.wei.amazingtalker.feature.home.home.utilities.TEST_SKILL_LEVEL_PROGRESS
import com.wei.amazingtalker.feature.home.home.utilities.TEST_SKILL_NAME
import com.wei.amazingtalker.feature.home.home.utilities.TEST_TUTOR_NAME
import com.wei.amazingtalker.feature.home.home.utilities.TestContacts

@Composable
fun MyCoursesContent(
    modifier: Modifier = Modifier,
    uiStates: MyCoursesContentState,
    isPreview: Boolean,
    onCardClick: () -> Unit,
) {
    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(SPACING_LARGE.dp))
            Row(modifier = modifier) {
                CourseProgressCard(
                    modifier = Modifier.weight(1f),
                    courseProgress = uiStates.courseProgress,
                    courseCount = uiStates.courseCount,
                    onClick = onCardClick,
                )
                Spacer(modifier = Modifier.width(SPACING_SMALL.dp))
                PupilRatingCard(
                    modifier = Modifier.weight(1f),
                    pupilRating = uiStates.pupilRating,
                    onClick = onCardClick,
                )
            }
            Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
            TutorProfileCard(
                modifier = modifier,
                tutorName = uiStates.tutorName,
                className = uiStates.className,
                lessonsCountDisplay = uiStates.lessonsCountDisplay,
                ratingCount = uiStates.ratingCount,
                startedDate = uiStates.startedDate,
                onTutorClick = onCardClick,
                onClick = onCardClick,
            )
            Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
            Row(modifier = modifier) {
                val cardSize = calculateCardSize()

                ContactCard(
                    modifier = Modifier.size(cardSize.dp),
                    contacts = uiStates.contacts,
                    isPreview = isPreview,
                )
                Spacer(modifier = Modifier.width(SPACING_SMALL.dp))
                SkillProgressCard(
                    modifier = Modifier
                        .size(cardSize.dp)
                        .weight(1f),
                    skillName = uiStates.skillName,
                    skillLevel = uiStates.skillLevel,
                    progress = uiStates.skillLevelProgress,
                    onClick = onCardClick,
                )
            }
            Spacer(modifier = Modifier.height(SPACING_LARGE.dp))
        }
    }
}

@Composable
fun CourseProgressCard(
    modifier: Modifier = Modifier,
    courseProgress: Int,
    courseCount: Int,
    onClick: () -> Unit,
) {
    val completed = stringResource(id = R.string.completed)

    StatusCard(
        modifier = modifier.semantics {
            contentDescription = completed
        },
        onClick = onClick,
        content = {
            Row {
                Text(
                    text = completed,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = " ($courseProgress%)",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = "$courseCount",
                style = MaterialTheme.typography.headlineSmall,
            )
        },
    )
}

@Composable
fun PupilRatingCard(
    modifier: Modifier = Modifier,
    pupilRating: Double,
    onClick: () -> Unit,
) {
    val pupil = stringResource(id = R.string.pupil)
    val rating = stringResource(id = R.string.rating)

    StatusCard(
        modifier = modifier
            .semantics {
                contentDescription = pupil + rating
            },
        onClick = onClick,
        content = {
            Row {
                Text(
                    text = pupil,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = " $rating",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
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
                Spacer(modifier = Modifier.width(SPACING_EXTRA_SMALL.dp))
                Text(
                    text = "$pupilRating",
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
        },
    )
}

@Composable
fun TutorProfileCard(
    modifier: Modifier = Modifier,
    tutorName: String,
    className: String,
    lessonsCountDisplay: String,
    ratingCount: Double,
    startedDate: String,
    onTutorClick: () -> Unit,
    onClick: () -> Unit,
) {
    TutorCard(
        modifier = modifier,
        content = {
            TutorButton(
                tutorName = tutorName,
                onTutorClick = onTutorClick,
            )
            Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
            ClassName(className = className)
            Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
            ClassInfo(
                lessonsCountDisplay = lessonsCountDisplay,
                ratingCount = ratingCount,
                startedDate = startedDate,
            )
            Spacer(modifier = Modifier.height(SPACING_SMALL.dp))
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
        shape = shapes.extraLarge,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(SPACING_SMALL.dp),
            content = {
                content()
            },
        )
    }
}

@Composable
private fun ClassInfo(
    lessonsCountDisplay: String,
    ratingCount: Double,
    startedDate: String,
) {
    Row(
        modifier = Modifier.padding(horizontal = SPACING_SMALL.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            val lessons = stringResource(id = R.string.lessons)

            Text(
                text = lessons,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                text = " $lessonsCountDisplay",
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            val rating = stringResource(id = R.string.rating)

            Text(
                text = rating,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                text = " $ratingCount",
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            val started = stringResource(id = R.string.started)

            Text(
                text = started,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                text = " $startedDate",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun ClassName(className: String) {
    Row(modifier = Modifier.padding(horizontal = SPACING_SMALL.dp)) {
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
    val tutor = stringResource(id = R.string.tutor)

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
            text = tutor,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = " $tutorName",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

internal fun calculateCardSize(): Int {
    return (CONTACT_HEAD_SHOT_SIZE * 2) + (SPACING_SMALL * 2) + 4
}

@ThemePreviews
@Composable
fun MyCoursesContentPreview() {
    AtTheme {
        Surface {
            MyCoursesContent(
                modifier = Modifier.padding(horizontal = SPACING_LARGE.dp),
                uiStates = MyCoursesContentState(
                    courseProgress = 20,
                    courseCount = 30,
                    pupilRating = 9.9,
                    tutorName = TEST_TUTOR_NAME,
                    className = TEST_CLASS_NAME,
                    lessonsCountDisplay = "40+",
                    ratingCount = 4.9,
                    startedDate = "11.04",
                    contacts = TestContacts,
                    skillName = TEST_SKILL_NAME,
                    skillLevel = TEST_SKILL_LEVEL,
                    skillLevelProgress = TEST_SKILL_LEVEL_PROGRESS,
                ),
                isPreview = true,
                onCardClick = {},
            )
        }
    }
}
