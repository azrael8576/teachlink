package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AppTheme
import com.wei.amazingtalker_recruit.core.model.data.DuringDayType
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewState
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
internal fun TimeListHeader() {
    val currentTimezone = ZoneId.systemDefault()
    val offsetFormatter = DateTimeFormatter.ofPattern("xxx")

    Text(
        text = String.format(
            stringResource(R.string.your_local_time_zone),
            currentTimezone,
            offsetFormatter.format(OffsetDateTime.now(currentTimezone).offset)
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = MaterialTheme.typography.bodySmall.fontSize,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
internal fun TimeItemHeader(duringDayType: DuringDayType) {
    Text(
        text = when (duringDayType) {
            DuringDayType.Morning -> stringResource(R.string.morning)
            DuringDayType.Afternoon -> stringResource(R.string.afternoon)
            DuringDayType.Evening -> stringResource(R.string.evening)
            else -> stringResource(R.string.morning)
        },
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
internal fun TimeListItem(
    timeSlot: IntervalScheduleTimeSlot,
    dispatch: (ScheduleViewAction) -> Unit
) {
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

@Preview(showBackground = true)
@Composable
fun TimeListHeaderPreview() {
    AppTheme {
        TimeListHeader()
    }
}

@Preview(showBackground = true)
@Composable
fun TimeItemHeaderPreview() {
    AppTheme {
        TimeItemHeader(DuringDayType.Afternoon)
    }
}

@Preview(showBackground = true)
@Composable
fun AvailableTimeSlotPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            AvailableTimeSlot(
                timeSlot = IntervalScheduleTimeSlot(
                    start = OffsetDateTime.now(),
                    end = OffsetDateTime.now(),
                    state = ScheduleState.AVAILABLE,
                    duringDayType = DuringDayType.Morning
                ),
                dispatch = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnavailableTimeSlotPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            UnavailableTimeSlot(
                timeSlot = IntervalScheduleTimeSlot(
                    start = OffsetDateTime.now(),
                    end = OffsetDateTime.now(),
                    state = ScheduleState.BOOKED,
                    duringDayType = DuringDayType.Morning
                ),
            )
        }
    }
}