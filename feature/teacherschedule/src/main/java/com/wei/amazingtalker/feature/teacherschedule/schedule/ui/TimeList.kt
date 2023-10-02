package com.wei.amazingtalker.feature.teacherschedule.schedule.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.core.model.data.DuringDayType
import com.wei.amazingtalker.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker.core.model.data.ScheduleState
import com.wei.amazingtalker.feature.teacherschedule.R
import java.time.Clock
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

val yourLocalTimeZoneFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("xxx")
val timeSlotFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")

@Composable
internal fun YourLocalTimeZoneText(clock: Clock = Clock.systemDefaultZone()) {
    val yourLocalTimeZone = String.format(
        stringResource(R.string.your_local_time_zone),
        clock.zone,
        yourLocalTimeZoneFormatter.format(OffsetDateTime.now(clock).offset)
    )

    Text(
        text = yourLocalTimeZone,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
            .semantics { contentDescription = yourLocalTimeZone }
    )
}

@Composable
internal fun DuringDay(duringDayType: DuringDayType) {
    val duringDay = when (duringDayType) {
        DuringDayType.Morning -> stringResource(R.string.morning)
        DuringDayType.Afternoon -> stringResource(R.string.afternoon)
        DuringDayType.Evening -> stringResource(R.string.evening)
        else -> stringResource(R.string.morning)
    }

    Text(
        text = duringDay,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
            .semantics { contentDescription = duringDay }
    )
}

@Composable
internal fun TimeSlot(
    timeSlot: IntervalScheduleTimeSlot,
    onTimeSlotClick: (IntervalScheduleTimeSlot) -> Unit
) {
    if (timeSlot.state == ScheduleState.AVAILABLE) {
        AvailableTimeSlot(
            timeSlot = timeSlot,
            onTimeSlotClick = { onTimeSlotClick(timeSlot) },
        )
    } else {
        UnavailableTimeSlot(
            timeSlot = timeSlot
        )
    }
}

@Composable
private fun AvailableTimeSlot(
    modifier: Modifier = Modifier,
    timeSlot: IntervalScheduleTimeSlot,
    onTimeSlotClick: () -> Unit
) {
    val startTimeText = timeSlotFormatter.format(timeSlot.start)
    val availableDescription =
        String.format(stringResource(R.string.content_description_available_time_slot), startTimeText)

    Button(
        onClick = { onTimeSlotClick() },
        modifier = modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(0.5f)
            .semantics { contentDescription = availableDescription },
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(
            text = startTimeText,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun UnavailableTimeSlot(
    modifier: Modifier = Modifier,
    timeSlot: IntervalScheduleTimeSlot
) {
    val startTimeText = timeSlotFormatter.format(timeSlot.start)
    val unavailableDescription =
        String.format(stringResource(R.string.content_description_unavailable_time_slot), startTimeText)

    OutlinedButton(
        onClick = {},
        enabled = false,
        modifier = modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(0.5f)
            .semantics { contentDescription = unavailableDescription },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            text = startTimeText,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun YourLocalTimeZoneTextPreview() {
    AtTheme {
        YourLocalTimeZoneText()
    }
}

@Preview(showBackground = true)
@Composable
fun DuringDayPreview() {
    AtTheme {
        DuringDay(DuringDayType.Afternoon)
    }
}

@Preview(showBackground = true)
@Composable
fun AvailableTimeSlotPreview() {
    AtTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            AvailableTimeSlot(
                timeSlot = IntervalScheduleTimeSlot(
                    start = OffsetDateTime.now(),
                    end = OffsetDateTime.now(),
                    state = ScheduleState.AVAILABLE,
                    duringDayType = DuringDayType.Morning
                ),
                onTimeSlotClick = { },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnavailableTimeSlotPreview() {
    AtTheme {
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