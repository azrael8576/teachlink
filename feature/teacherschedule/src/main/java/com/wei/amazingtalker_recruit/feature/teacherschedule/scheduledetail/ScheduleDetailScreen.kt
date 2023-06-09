package com.wei.amazingtalker_recruit.feature.teacherschedule.scheduledetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wei.amazingtalker_recruit.core.ui.theme.AppTheme
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleDetailViewModel

@Composable
internal fun ScheduleDetailScreen(
    viewModel: ScheduleDetailViewModel = hiltViewModel()
) {
    val states by viewModel.states.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopStart)
        ) {
            Text(text = states.teacherName.toString(), style = MaterialTheme.typography.bodyMedium)
            Text(
                text = states.start.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = states.end.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = states.state?.name.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = states.duringDayType?.name.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = { viewModel.dispatch(ScheduleDetailViewAction.ClickBack) },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 16.dp),
        ) {
            Text("Back")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScheduleDetailScreenPreview() {
    AppTheme {
        ScheduleDetailScreen()
    }
}