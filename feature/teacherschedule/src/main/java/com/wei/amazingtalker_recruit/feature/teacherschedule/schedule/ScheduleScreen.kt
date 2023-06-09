package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.models.DuringDayType
import com.wei.amazingtalker_recruit.core.ui.theme.AppTheme
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewState
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleViewModel
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.TimeListUiState
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@VisibleForTesting
@Composable
internal fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val uiStates: ScheduleViewState by viewModel.states.collectAsStateWithLifecycle()
    TimeListView(uiStates = uiStates, dispatch = viewModel::dispatch)
}

@Composable
internal fun TimeListView(
    uiStates: ScheduleViewState,
    dispatch: (ScheduleViewAction) -> Unit,
) {
    val nestedScrollInterop = rememberNestedScrollInteropConnection()
    val listState = rememberLazyListState()
    val timeListUiState = uiStates.timeListUiState

    // 當 timeListUiState 發生改變時，將會啟動一個新的協程執行，
    // 滾動到列表的第 0 個位置。
    LaunchedEffect(timeListUiState) {
        listState.scrollToItem(index = 0)
    }

    // Add the nested scroll connection to your top level @Composable element
    // using the nestedScroll modifier.
    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .nestedScroll(nestedScrollInterop),
    ) {
        when (timeListUiState) {
            is TimeListUiState.Success -> {
                timeListHeader()
                val groupedTimeSlots = timeListUiState.timeSlotList.groupBy { it.duringDayType }
                groupedTimeSlots.forEach { (duringDayType, timeSlots) ->
                    timeItemHeader(duringDayType)
                    timeList(timeSlots, dispatch)
                }
            }

            is TimeListUiState.Loading -> item {
            }

            is TimeListUiState.Error -> item {
            }

        }
    }
}

fun LazyListScope.timeListHeader() = item {
    val currentTimezone = ZoneId.systemDefault()
    val offsetFormatter = DateTimeFormatter.ofPattern("xxx")

    Text(
        text = String.format(
            stringResource(R.string.your_local_time_zone),
            currentTimezone,
            offsetFormatter.format(OffsetDateTime.now(currentTimezone).offset)
        ),
        fontSize = MaterialTheme.typography.bodySmall.fontSize,
        modifier = Modifier.padding(top = 16.dp)
    )
}

fun LazyListScope.timeItemHeader(duringDayType: DuringDayType) = item {
    Text(
        text = when (duringDayType) {
            DuringDayType.Morning -> stringResource(R.string.morning)
            DuringDayType.Afternoon -> stringResource(R.string.afternoon)
            DuringDayType.Evening -> stringResource(R.string.evening)
            else -> stringResource(R.string.morning)
        },
        fontSize = MaterialTheme.typography.bodySmall.fontSize,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

fun LazyListScope.timeList(
    timeSlots: List<IntervalScheduleTimeSlot>,
    dispatch: (ScheduleViewAction) -> Unit
) = itemsIndexed(timeSlots) { index, timeSlot ->
    if (timeSlot.state == ScheduleState.AVAILABLE) {
        AvailableTimeSlot(
            timeSlot = timeSlot,
            dispatch = dispatch
        )
    } else {
        UnavailableTimeSlot(
            timeSlot = timeSlot
        )
    }
}

@Composable
private fun AvailableTimeSlot(
    timeSlot: IntervalScheduleTimeSlot,
    dispatch: (ScheduleViewAction) -> Unit
) {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")

    Button(
        onClick = { dispatch(ScheduleViewAction.ClickTimeSlot(timeSlot)) },
        modifier = Modifier
            .padding(
                bottom = 12.dp
            )
            .fillMaxWidth(0.5f),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(
            text = dateTimeFormatter.format(timeSlot.start),
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun UnavailableTimeSlot(
    timeSlot: IntervalScheduleTimeSlot
) {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")

    OutlinedButton(
        onClick = {},
        modifier = Modifier
            .padding(
                bottom = 12.dp
            )
            .fillMaxWidth(0.5f),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            text = dateTimeFormatter.format(timeSlot.start),
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScheduleScreenPreview() {
    AppTheme {
        ScheduleScreen()
    }
}