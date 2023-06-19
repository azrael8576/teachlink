package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.defaultDateUtc
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.weekDataHelper
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
internal fun DateTabLayout(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    tabs: MutableList<OffsetDateTime>,
    dispatch: (ScheduleViewAction) -> Unit
) {
    val tabWidth = remember { mutableStateOf(0.dp) } // 創建一個共享的寬度數據

    BoxWithConstraints {
        tabWidth.value = maxWidth / 3 // 計算並設置Tab的寬度
    }

    // Use by rememberUpdatedState to remember view model state
    val selectedTabIndex by rememberUpdatedState(selectedIndex)

    // TabLayout
    ScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = MaterialTheme.colorScheme.primary,
                height = 2.dp
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, tab ->
            DateTab(
                selected = selectedTabIndex == index,
                tab = tab,
                index = index,
                onTabClick = dispatch,
                tabWidth = tabWidth.value
            )
        }
    }
}

@Composable
private fun DateTab(
    selected: Boolean,
    tab: OffsetDateTime,
    index: Int,
    onTabClick: (ScheduleViewAction) -> Unit,
    tabWidth: Dp
) {
    Tab(
        selected = selected,
        onClick = {
            onTabClick(ScheduleViewAction.SelectedTab(tab, index))
        },
        modifier = Modifier
            .height(70.dp)
            .width(tabWidth)
    ) {
        val dateFormatter = DateTimeFormatter.ofPattern("E, MMM dd")
        val textColor =
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground

        Text(
            text = dateFormatter.format(tab),
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DateTabLayoutPreview() {
    val TestHeight = 70.dp
    AtTheme {
        Box(modifier = Modifier.height(TestHeight)) {
            DateTabLayout(
                modifier = Modifier.fillMaxSize(),
                selectedIndex = 0,
                tabs = weekDataHelper.setDateTabs(defaultDateUtc.getLocalOffsetDateTime()),
                dispatch = {}
            )
        }
    }
}