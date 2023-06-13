package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AppTheme
import com.wei.amazingtalker_recruit.core.extensions.state.observeState
import com.wei.amazingtalker_recruit.core.navigation.DeepLinks
import com.wei.amazingtalker_recruit.core.navigation.navigate
import com.wei.amazingtalker_recruit.feature.teacherschedule.R
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.FragmentScheduleBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ErrorMessage
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewState
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.WeekAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ScheduleFragment
    : BaseFragment<
        FragmentScheduleBinding,
        ScheduleViewModel,
        ScheduleViewAction,
        ScheduleViewState
        >() {

    override val viewModel: ScheduleViewModel by viewModels()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScheduleBinding
        get() = FragmentScheduleBinding::inflate

    override fun FragmentScheduleBinding.setupViews() {
        buttonLastWeek.setActiveColorFilter()
        buttonNextWeek.setActiveColorFilter()

        // ComposeView
        composeView.apply {
            // Dispose the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                AppTheme {
                    ScheduleScreen()
                }
            }
        }
    }

    private fun ImageButton.setActiveColorFilter() {
        setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.amazingtalker_green_900
            )
        )
    }

    override fun FragmentScheduleBinding.addOnClickListener() {
        buttonLastWeek.setOnClickListener {
            viewModel.dispatch(
                ScheduleViewAction.UpdateWeek(
                    WeekAction.PREVIOUS_WEEK
                )
            )
        }
        buttonNextWeek.setOnClickListener {
            viewModel.dispatch(
                ScheduleViewAction.UpdateWeek(
                    WeekAction.NEXT_WEEK
                )
            )
        }
    }

    override fun FragmentScheduleBinding.handleState(
        viewLifecycleOwner: LifecycleOwner,
        states: StateFlow<ScheduleViewState>
    ) {
        states.let {
            states.observeState(viewLifecycleOwner, ScheduleViewState::weekStart) {
                setPreviousWeekButtonState(it)
            }

            states.observeState(viewLifecycleOwner, ScheduleViewState::weekDateText) {
                setWeekDateTextState(it)

            }
            states.observeState(viewLifecycleOwner, ScheduleViewState::dateTabs) { dates ->
                setDateTabsState(dates)

            }

            states.observeState(
                viewLifecycleOwner,
                ScheduleViewState::isTokenValid
            ) { isTokenValid ->
                if (isTokenValid) return@observeState

                findNavController().popBackStack(R.id.scheduleFragment, true)
                findNavController().navigate(DeepLinks.LOGIN)
            }

            states.observeState(
                viewLifecycleOwner,
                ScheduleViewState::errorMessages
            ) { errorMessages ->
                if (errorMessages.isEmpty()) return@observeState

                handleErrorMessage(errorMessages.first())
                // UI 事件消費後應該通知 ViewModel 重置初始值
                viewModel.dispatch(ScheduleViewAction.SnackBarShown)
            }

            states.observeState(
                viewLifecycleOwner,
                ScheduleViewState::clickTimeSlots
            ) { clickTimeSlots ->
                if (clickTimeSlots.isEmpty()) return@observeState

                val action =
                    ScheduleFragmentDirections.actionScheduleFragmentToScheduleDetailFragment(
                        clickTimeSlots.first()
                    )
                findNavController().navigate(action)
                // UI 事件消費後應該通知 ViewModel 重置初始值
                viewModel.dispatch(ScheduleViewAction.TimeSlotClicked)
            }
        }
    }

    private fun FragmentScheduleBinding.setPreviousWeekButtonState(date: OffsetDateTime) {
        with(buttonLastWeek) {
            if (date < OffsetDateTime.now(ZoneId.systemDefault())) {
                colorFilter = null
                setOnClickListener(null)
            } else {
                setActiveColorFilter()
                setOnClickListener {
                    viewModel.dispatch(ScheduleViewAction.UpdateWeek(WeekAction.PREVIOUS_WEEK))
                }
            }
        }
    }

    private fun FragmentScheduleBinding.setWeekDateTextState(weekDate: String) {
        textWeek.text = weekDate
        textWeek.setOnClickListener {
            //TODO 開啟日曆選單
            viewModel.dispatch(
                ScheduleViewAction.ShowSnackBar(
                    message = "開啟日曆選單: $weekDate",
                )
            )
        }
    }

    private fun FragmentScheduleBinding.setDateTabsState(dates: MutableList<OffsetDateTime>) {
        tablayout.doOnLayout {
            setupTabs(dates)
            // 延遲選擇tab
            tablayout.post {
                // 恢复所選的tab
                tablayout.getTabAt(viewModel.states.value.selectedIndex)?.select()
            }
        }
    }

    private fun FragmentScheduleBinding.setupTabs(
        options: MutableList<OffsetDateTime>
    ) {
        with(tablayout) {
            val tabWidth = width / 3
            val tabHeight = height
            // 移除所有 Tabs
            removeAllTabs()
            options.forEachIndexed { _, element ->
                newTab().run {
                    setCustomView(R.layout.custom_tab)
                    customView?.minimumWidth = tabWidth
                    customView?.minimumHeight = tabHeight
                    tag = element
                    val dateFormatter = DateTimeFormatter.ofPattern("E, MMM dd")
                    text = dateFormatter.format(element)
                    addTab(this)
                }
            }
            // 重新添加 TabSelectedListener
            addTabSelectedListener(tablayout)
        }
    }

    private fun addTabSelectedListener(tabLayout: TabLayout) {
        tabLayout.clearOnTabSelectedListeners()
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Timber.d("======onTabSelected====${tab.tag}")

                val date = tab.tag as? OffsetDateTime
                if (date != null) {
                    viewModel.dispatch(ScheduleViewAction.SelectedTab(date, tab.position))
                } else {
                    Timber.e("Invalid tab tag, expected OffsetDateTime but got ${tab.tag?.javaClass?.name}")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun FragmentScheduleBinding.handleErrorMessage(errorMessage: ErrorMessage) {
        val snackBar = createSnackBar(errorMessage)
        configureSnackBar(snackBar, errorMessage)
        snackBar.show()
    }

    private fun createSnackBar(errorMessage: ErrorMessage): Snackbar {
        val message = getErrorMessage(errorMessage)
        return Snackbar.make(binding.root, message, errorMessage.duration)
    }

    private fun getErrorMessage(errorMessage: ErrorMessage): String {
        return if (errorMessage.resId != 0) getString(
            R.string.inquirying_teacher_calendar,
            errorMessage.message
        )
        else errorMessage.message
    }

    private fun configureSnackBar(snackBar: Snackbar, errorMessage: ErrorMessage) {
        snackBar.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.amazingtalker_green_700
            )
        )
        val snackTextView =
            snackBar.view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        snackTextView.maxLines = errorMessage.maxLines
    }

}