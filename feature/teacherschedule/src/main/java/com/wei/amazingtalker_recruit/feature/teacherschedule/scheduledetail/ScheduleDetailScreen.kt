package com.wei.amazingtalker_recruit.feature.teacherschedule.scheduledetail

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wei.amazingtalker_recruit.core.designsystem.icon.AtIcons
import com.wei.amazingtalker_recruit.core.designsystem.theme.AtTheme
import com.wei.amazingtalker_recruit.core.model.data.DuringDayType
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
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
    timeSlot: IntervalScheduleTimeSlot,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ScheduleDetailViewModel = hiltViewModel(),
) {
    val uiStates by viewModel.states.collectAsStateWithLifecycle()
    viewModel.dispatch(ScheduleDetailViewAction.InitNavData(timeSlot))

    ScheduleDetailScreen(
        uiStates = uiStates,
        onBackClick = navController::popBackStack,
    )
}

@Composable
internal fun ScheduleDetailScreen(
    uiStates: ScheduleDetailViewState,
    onBackClick: () -> Unit,
    withTopSpacer: Boolean = true
) {

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column{
            if (withTopSpacer) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            }
            ScheduleDetailToolbar(onBackClick = onBackClick)

            val teacherName = stringResource(R.string.teacher_name)
            Text(
                text = uiStates.teacherName.toString(),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .semantics { contentDescription = teacherName },
            )
            val startTime = stringResource(R.string.start_time)
            Text(
                text = uiStates.start.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .semantics { contentDescription = startTime },
            )
            val endTime = stringResource(R.string.end_time)
            Text(
                text = uiStates.end.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .semantics { contentDescription = endTime },
            )
            val state = stringResource(R.string.state)
            Text(
                text = uiStates.state?.name.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .semantics { contentDescription = state },
            )
            val duringDayType = stringResource(R.string.during_day_type)
            Text(
                text = uiStates.duringDayType?.name.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .semantics { contentDescription = duringDayType },
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
        val back = stringResource(R.string.back)
        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier.semantics { contentDescription = back }
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