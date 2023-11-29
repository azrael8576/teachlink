package com.wei.amazingtalker.feature.teacherschedule.scheduledetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wei.amazingtalker.core.designsystem.icon.AtIcons
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.core.designsystem.theme.SPACING_LARGE
import com.wei.amazingtalker.core.designsystem.theme.SPACING_MEDIUM
import com.wei.amazingtalker.core.model.data.DuringDayType
import com.wei.amazingtalker.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker.core.model.data.ScheduleState
import com.wei.amazingtalker.feature.teacherschedule.R
import java.time.OffsetDateTime

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
internal fun ScheduleDetailRoute(
    teacherName: String,
    timeSlot: IntervalScheduleTimeSlot,
    navController: NavHostController,
    viewModel: ScheduleDetailViewModel = hiltViewModel(),
) {
    val uiStates by viewModel.states.collectAsStateWithLifecycle()
    viewModel.dispatch(
        ScheduleDetailViewAction.InitNavData(
            teacherName = teacherName,
            intervalScheduleTimeSlot = timeSlot,
        ),
    )

    ScheduleDetailScreen(
        uiStates = uiStates,
        onBackClick = navController::popBackStack,
    )
}

@Composable
internal fun ScheduleDetailScreen(
    uiStates: ScheduleDetailViewState,
    onBackClick: () -> Unit,
    withTopSpacer: Boolean = true,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            if (withTopSpacer) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
            ScheduleDetailToolbar(onBackClick = onBackClick)

            val teacherName = uiStates.teacherName.toString()
            val teacherNameDescription =
                stringResource(R.string.content_description_teacher_name).format(teacherName)
            Text(
                text = teacherName,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(horizontal = SPACING_LARGE.dp)
                    .testTag(stringResource(id = R.string.tag_teacher_name))
                    .semantics { contentDescription = teacherNameDescription },
            )

            val startTimeDescription =
                stringResource(R.string.content_description_start_time).format(uiStates.start.toString())
            Text(
                text = startTimeDescription,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = SPACING_LARGE.dp)
                    .padding(top = SPACING_MEDIUM.dp)
                    .testTag(stringResource(id = R.string.tag_start_time))
                    .semantics { contentDescription = startTimeDescription },
            )

            val endTimeDescription =
                stringResource(R.string.content_description_end_time).format(uiStates.end.toString())
            Text(
                text = endTimeDescription,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = SPACING_LARGE.dp)
                    .padding(top = SPACING_MEDIUM.dp)
                    .testTag(stringResource(id = R.string.tag_end_time))
                    .semantics { contentDescription = endTimeDescription },
            )

            val state = uiStates.state?.name.toString()
            val stateDescription = stringResource(R.string.content_description_state).format(state)
            Text(
                text = state,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = SPACING_LARGE.dp)
                    .padding(top = SPACING_MEDIUM.dp)
                    .testTag(stringResource(id = R.string.tag_state))
                    .semantics { contentDescription = stateDescription },
            )

            val duringDayType = uiStates.duringDayType?.name.toString()
            val duringDayTypeDescription =
                stringResource(R.string.content_description_during_day_type).format(duringDayType)
            Text(
                text = duringDayType,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = SPACING_LARGE.dp)
                    .padding(top = SPACING_MEDIUM.dp)
                    .testTag(stringResource(id = R.string.tag_during_day_type))
                    .semantics { contentDescription = duringDayTypeDescription },
            )
        }
    }
}

@Composable
private fun ScheduleDetailToolbar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        val back = stringResource(R.string.content_description_back)
        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier.semantics { contentDescription = back },
        ) {
            Icon(
                imageVector = AtIcons.ArrowBack,
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleDetailScreenPreview() {
    val nowTime = OffsetDateTime.now()

    AtTheme {
        ScheduleDetailScreen(
            uiStates = ScheduleDetailViewState(
                teacherName = "Teacher Name",
                start = nowTime,
                end = nowTime.plusMinutes(30),
                state = ScheduleState.BOOKED,
                duringDayType = DuringDayType.Morning,
            ),
            onBackClick = { },
        )
    }
}
