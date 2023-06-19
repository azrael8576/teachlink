package com.wei.amazingtalker_recruit.feature.teacherschedule.scheduledetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleDetailViewModel

@Composable
internal fun ScheduleDetailScreen(
    timeSlot: IntervalScheduleTimeSlot,
    navController: NavHostController,
    viewModel: ScheduleDetailViewModel = hiltViewModel(),
) {
    val uiStates by viewModel.states.collectAsStateWithLifecycle()
    viewModel.dispatch(ScheduleDetailViewAction.InitNavData(timeSlot))

    LaunchedEffect(uiStates.isBackClick) {
        if (uiStates.isBackClick) {
            navController.popBackStack()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(text = uiStates.teacherName.toString(), style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = uiStates.start.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = uiStates.end.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = uiStates.state?.name.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = uiStates.duringDayType?.name.toString(),
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
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ScheduleDetailScreenPreview() {
//    AtTheme {
//        ScheduleDetailScreen()
//    }
//}