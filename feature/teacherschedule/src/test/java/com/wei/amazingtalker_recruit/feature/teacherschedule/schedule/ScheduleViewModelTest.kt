package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule

import com.google.common.truth.Truth
import com.wei.amazingtalker_recruit.core.domain.IntervalizeScheduleUseCase
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.core.extensions.state.setState
import com.wei.amazingtalker_recruit.core.manager.SnackbarManager
import com.wei.amazingtalker_recruit.core.manager.SnackbarState
import com.wei.amazingtalker_recruit.core.model.data.DuringDayType
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.core.testing.repository.TestTeacherScheduleRepository
import com.wei.amazingtalker_recruit.core.testing.util.MainDispatcherRule
import com.wei.amazingtalker_recruit.core.utils.UiText
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import com.wei.amazingtalker_recruit.feature.teacherschedule.domain.GetTeacherScheduleUseCase
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.WeekDataHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Unit tests for [ScheduleViewModel].
 * 在此測試只關注 ScheduleViewModelTest 的職責。
 * 不需要模擬 GetTeacherScheduleUseCase, IntervalizeScheduleUseCase, WeekDataHelper 的行為，
 * 因為已經對它進行了充分的測試 [GetTeacherScheduleUseCaseTest], [IntervalizeScheduleUseCaseTest], [WeekDataHelperTest]
 *
 * 遵循此模型，安排、操作、斷言：
 * {Arrange}{Act}{Assert}
 */
class ScheduleViewModelTest {

    private lateinit var getTeacherScheduleUseCase: GetTeacherScheduleUseCase
    private lateinit var testTeacherScheduleRepo: TestTeacherScheduleRepository
    private lateinit var intervalizeScheduleUseCase: IntervalizeScheduleUseCase
    private lateinit var weekDataHelper: WeekDataHelper
    private lateinit var snackbarManager: SnackbarManager
    private lateinit var viewModel: ScheduleViewModel

    // UI State for test assert
    private lateinit var expectedState: MutableStateFlow<ScheduleViewState>

    // mock currentTime
    private val fixedClock = Clock.fixed(Instant.parse(testCurrentTime), ZoneId.systemDefault())
    private val fixedClockUtc = Clock.fixed(Instant.parse(testCurrentTime), ZoneOffset.UTC)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        testTeacherScheduleRepo = TestTeacherScheduleRepository()
        intervalizeScheduleUseCase = IntervalizeScheduleUseCase()
        getTeacherScheduleUseCase = GetTeacherScheduleUseCase(
            teacherScheduleRepository = testTeacherScheduleRepo,
            intervalizeScheduleUseCase = intervalizeScheduleUseCase,
        )
        weekDataHelper = WeekDataHelper()
        snackbarManager = SnackbarManager()

        viewModel = ScheduleViewModel(
            clock = fixedClock,
            clockUtc = fixedClockUtc,
            getTeacherScheduleUseCase = getTeacherScheduleUseCase,
            weekDataHelper = weekDataHelper,
            snackbarManager = snackbarManager
        )

        expectedState = MutableStateFlow(viewModel.states.value)
    }

    @Test
    fun `dispatch showSnackBar action should set correct snackbar when message without resId`() =
        runTest {
            // Arrange
            val testMessage = "Test Message"
            val testUiText = UiText.DynamicString(testMessage)

            // Act
            val action = ScheduleViewAction.ShowSnackBar(message = listOf(testMessage))
            viewModel.dispatch(action)

            // Assert
            val lastMessage = snackbarManager.getLastMessage()
            Truth.assertThat(lastMessage?.state).isEqualTo(SnackbarState.Default)
            Truth.assertThat(lastMessage?.uiText).isEqualTo(testUiText)
        }

    @Test
    fun `dispatch showSnackBar action should set correct snackbar when message with single arg`() =
        runTest {
            // Arrange
            val resId = R.string.clickWeekDate
            val message = listOf(testTeacherName)
            val testUiText = UiText.StringResource(
                resId,
                message.map {
                    UiText.StringResource.Args.DynamicString(it)
                }.toList()
            )

            // Act
            val action = ScheduleViewAction.ShowSnackBar(
                resId,
                message = message
            )
            viewModel.dispatch(action)

            // Assert
            val lastMessage = snackbarManager.getLastMessage()
            Truth.assertThat(lastMessage?.state).isEqualTo(SnackbarState.Default)
            Truth.assertThat(lastMessage?.uiText).isEqualTo(testUiText)
        }

    @Test
    fun `dispatch showSnackBar action should set correct snackbar when message with multiple args`() =
        runTest {
            // Arrange
            val resId = R.string.inquirying_teacher_calendar
            val message = listOf(testTeacherName, testWeekDateText)
            val testUiText = UiText.StringResource(
                resId,
                message.map {
                    UiText.StringResource.Args.DynamicString(it)
                }.toList()
            )

            // Act
            val action = ScheduleViewAction.ShowSnackBar(
                resId,
                message = message
            )
            viewModel.dispatch(action)

            // Assert
            val lastMessage = snackbarManager.getLastMessage()
            Truth.assertThat(lastMessage?.state).isEqualTo(SnackbarState.Default)
            Truth.assertThat(lastMessage?.uiText).isEqualTo(testUiText)
        }

    @Test
    fun `dispatch updateWeek action should update correct _queryDateUtc and resetToStartOfDay when weekAction is PREVIOUS_WEEK and previousWeekMondayLocalDate is the same as or later than the current local time`() =
        runTest {
            // Arrange
            viewModel.updateState {
                copy(
                    _queryDateUtc = _queryDateUtc.plusWeeks(2),
                )
            }
            val previousWeekMondayLocalDate = viewModel.states.value.weekStart.minusWeeks(1)
            expectedState.setState {
                copy(
                    selectedIndex = 0,
                    _queryDateUtc = weekDataHelper.getQueryDateUtc(
                        queryDateLocal = previousWeekMondayLocalDate,
                        resetToStartOfDay = true
                    ),
                )
            }

            // Act
            viewModel.dispatch(ScheduleViewAction.UpdateWeek(WeekAction.PREVIOUS_WEEK))

            // Assert
            Truth.assertThat(viewModel.states.value._queryDateUtc).isEqualTo(expectedState.value._queryDateUtc)
            Truth.assertThat(viewModel.states.value.selectedIndex).isEqualTo(expectedState.value.selectedIndex)
        }

    @Test
    fun `dispatch updateWeek action update correct _queryDateUtc when weekAction is PREVIOUS_WEEK and previousWeekMondayLocalDate is earlier than the current time`() =
        runTest {
            // Arrange
            expectedState.setState {
                copy(
                    selectedIndex = 0,
                    _queryDateUtc = weekDataHelper.getQueryDateUtc(
                        queryDateLocal = OffsetDateTime.now(fixedClock).getLocalOffsetDateTime(),
                        resetToStartOfDay = false
                    ),
                )
            }

            // Act
            viewModel.dispatch(ScheduleViewAction.UpdateWeek(WeekAction.PREVIOUS_WEEK))

            // Assert
            Truth.assertThat(viewModel.states.value._queryDateUtc).isEqualTo(expectedState.value._queryDateUtc)
            Truth.assertThat(viewModel.states.value.selectedIndex).isEqualTo(expectedState.value.selectedIndex)
        }


    @Test
    fun `dispatch updateWeek action update correct _queryDateUtc when weekAction is NEXT_WEEK`() =
        runTest {
            // Arrange
            expectedState.setState {
                copy(
                    selectedIndex = 0,
                    _queryDateUtc = weekDataHelper.getQueryDateUtc(
                        queryDateLocal = viewModel.states.value.weekStart.plusWeeks(1),
                        resetToStartOfDay = true
                    ),
                )
            }

            // Act
            viewModel.dispatch(ScheduleViewAction.UpdateWeek(WeekAction.NEXT_WEEK))

            // Assert
            Truth.assertThat(viewModel.states.value._queryDateUtc).isEqualTo(expectedState.value._queryDateUtc)
            Truth.assertThat(viewModel.states.value.selectedIndex).isEqualTo(expectedState.value.selectedIndex)
        }

    @Test
    fun `dispatch updateWeek action update correct _queryDateUtc when weekAction is NEXT_WEEKs`() =
        runTest {
            // Arrange
            expectedState.setState {
                copy(
                    selectedIndex = 0,
                    _queryDateUtc = weekDataHelper.getQueryDateUtc(
                        queryDateLocal = viewModel.states.value.weekStart.plusWeeks(1),
                        resetToStartOfDay = true
                    ),
                )
            }

            // Act
            viewModel.dispatch(ScheduleViewAction.UpdateWeek(WeekAction.NEXT_WEEK))

            // Assert
            Truth.assertThat(viewModel.states.value._queryDateUtc).isEqualTo(expectedState.value._queryDateUtc)
            Truth.assertThat(viewModel.states.value.selectedIndex).isEqualTo(expectedState.value.selectedIndex)
        }

    @Test
    fun `filterTimeListByDate should update state with grouped time slots when result is Success`() =
        runTest {
            // Arrange
            val testResult = DataSourceResult.Success(testTimeSlotList)
            val testDate = OffsetDateTime.parse("2023-08-25T01:00+08:00")
            val expectedGroupedTimeSlots: Map<DuringDayType, List<IntervalScheduleTimeSlot>> =
                mapOf(
                    DuringDayType.Morning to listOf(
                        IntervalScheduleTimeSlot(
                            start = OffsetDateTime.parse("2023-08-25T01:00+08:00"),
                            end = OffsetDateTime.parse("2023-08-25T01:30+08:00"),
                            state = ScheduleState.AVAILABLE,
                            duringDayType = DuringDayType.Morning
                        ),
                        IntervalScheduleTimeSlot(
                            start = OffsetDateTime.parse("2023-08-25T01:30+08:00"),
                            end = OffsetDateTime.parse("2023-08-25T02:00+08:00"),
                            state = ScheduleState.AVAILABLE,
                            duringDayType = DuringDayType.Morning
                        )
                    )
                )

            // Act
            viewModel.filterTimeListByDate(testResult, testDate)

            // Assert
            Truth.assertThat(viewModel.states.value.timeListUiState).isEqualTo(TimeListUiState.Success(groupedTimeSlots = expectedGroupedTimeSlots))
        }

    @Test
    fun `filterTimeListByDate should update state to LoadFailed when result is Error`() =
        runTest {
            // Arrange
            val testResult = DataSourceResult.Error()
            val testDate = OffsetDateTime.parse("2023-08-25T01:00+08:00")

            // Act
            viewModel.filterTimeListByDate(testResult, testDate)

            // Assert
            Truth.assertThat(viewModel.states.value.timeListUiState).isEqualTo(TimeListUiState.LoadFailed)
        }

    @Test
    fun `filterTimeListByDate should update state to Loading when result is Loading`() =
        runTest {
            // Arrange
            val testResult = DataSourceResult.Loading
            val testDate = OffsetDateTime.parse("2023-08-25T01:00+08:00")

            // Act
            viewModel.filterTimeListByDate(testResult, testDate)

            // Assert
            Truth.assertThat(viewModel.states.value.timeListUiState).isEqualTo(TimeListUiState.Loading)
        }

}

const val testTeacherName = TEST_DATA_TEACHER_NAME
const val testWeekDateText = "2023-08-14 - 08-20"
const val testCurrentTime = "2023-08-18T00:00:00.00Z"
private val testTimeSlotList = mutableListOf(
    IntervalScheduleTimeSlot(
        start = OffsetDateTime.parse("2023-08-19T16:00+08:00"),
        end = OffsetDateTime.parse("2023-08-19T16:30+08:00"),
        state = ScheduleState.AVAILABLE,
        duringDayType = DuringDayType.Afternoon
    ),
    IntervalScheduleTimeSlot(
        start = OffsetDateTime.parse("2023-08-20T13:30+08:00"),
        end = OffsetDateTime.parse("2023-08-20T14:00+08:00"),
        state = ScheduleState.AVAILABLE,
        duringDayType = DuringDayType.Afternoon
    ),
    IntervalScheduleTimeSlot(
        start = OffsetDateTime.parse("2023-08-20T14:00+08:00"),
        end = OffsetDateTime.parse("2023-08-20T14:30+08:00"),
        state = ScheduleState.AVAILABLE,
        duringDayType = DuringDayType.Afternoon
    ),
    IntervalScheduleTimeSlot(
        start = OffsetDateTime.parse("2023-08-25T01:00+08:00"),
        end = OffsetDateTime.parse("2023-08-25T01:30+08:00"),
        state = ScheduleState.AVAILABLE,
        duringDayType = DuringDayType.Morning
    ),
    IntervalScheduleTimeSlot(
        start = OffsetDateTime.parse("2023-08-25T01:30+08:00"),
        end = OffsetDateTime.parse("2023-08-25T02:00+08:00"),
        state = ScheduleState.AVAILABLE,
        duringDayType = DuringDayType.Morning
    )
)