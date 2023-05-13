package com.wei.amazingtalker_recruit.feature.teacherschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.models.ShowSnackBarEvent
import com.wei.amazingtalker_recruit.core.models.ShowToastEvent
import com.wei.amazingtalker_recruit.core.navigation.DeepLinks
import com.wei.amazingtalker_recruit.core.navigation.navigate
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.feature.teacherschedule.adapters.OnItemClickListener
import com.wei.amazingtalker_recruit.feature.teacherschedule.adapters.ScheduleTimeListAdapter
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.FragmentScheduleBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleViewModel
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.WeekAction
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(), OnItemClickListener {

    @Inject
    lateinit var adapter: ScheduleTimeListAdapter
    override val viewModel: ScheduleViewModel by viewModels()
    private var isUpdatingWeek = false

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScheduleBinding
        get() = FragmentScheduleBinding::inflate

    override fun LifecycleCoroutineScope.setupObservers() {
        launchWhenStarted { observeWeekStart() }
        launchWhenStarted { observeWeekEnd() }
        launchWhenStarted { observeWeekDateText() }
        launchWhenStarted { observeDateTabs() }
        launchWhenStarted { observeFilteredTimeList() }
    }

    override fun FragmentScheduleBinding.addOnClickListener() {
    }

    override fun FragmentScheduleBinding.setupViews() {
        adapter.setOnClickListener(this@ScheduleFragment)
        scheduleTimeRecyclerview.adapter = adapter

        viewModel.showSnackBar(
            Snackbar.make(
                root,
                getString(
                    R.string.inquirying_teacher_calendar,
                    viewModel.currentTeacherName.value
                ),
                Snackbar.LENGTH_LONG
            )
        )
    }

    override fun initData() {
        if (!TokenManager.isTokenValid) {
            findNavController().popBackStack(R.id.scheduleFragment, true)
            findNavController().navigate(DeepLinks.LOGIN)
        }
    }

    private suspend fun LifecycleCoroutineScope.observeWeekStart() {
        viewModel.weekStart.collect {
            setPreviousWeekButtonState(it)
        }
    }

    private suspend fun LifecycleCoroutineScope.observeWeekEnd() {
        viewModel.weekEnd.collect {
            setNextWeekButtonState(it)
        }
    }

    private suspend fun LifecycleCoroutineScope.observeWeekDateText() {
        viewModel.weekDateText.collect {
            binding.textWeek.text = it
            binding.textWeek.setOnClickListener { view ->
                //TODO 開啟日曆選單
                viewModel.showSnackBar(
                    Snackbar.make(
                        view,
                        "TODO 開啟日曆選單, 當前星期為: ${it}",
                        Snackbar.LENGTH_LONG
                    )
                )
            }
        }
    }

    private suspend fun LifecycleCoroutineScope.observeDateTabs() {
        viewModel.dateTabs.collect { dates ->
            binding.tablayout.doOnLayout {
                setupTabs(dates)
                // 延遲選擇tab
                binding.tablayout.post {
                    // 恢复所選的tab
                    binding.tablayout.getTabAt(viewModel.selectedIndex.value)?.select()
                }
            }
        }
    }

    private suspend fun LifecycleCoroutineScope.observeFilteredTimeList() {
        viewModel.filteredTimeList.collect { result ->
            handleTimeListUpdate(result)
        }
    }

    private fun handleTimeListUpdate(result: DataSourceResult<List<IntervalScheduleTimeSlot>>) {
        if (!isUpdatingWeek) {
            when (result) {
                is DataSourceResult.Success -> {
                    result.data.let {
                        adapter.addHeaderAndSubmitList(
                            it
                        )
                    }
                    binding.scheduleTimeRecyclerview.isVisible = true
                    binding.scheduleTimeRecyclerview.scrollToPosition(0)

                    Timber.d("API Success")
                }

                is DataSourceResult.Error -> {
                    viewModel.showSnackBar(
                        Snackbar.make(
                            binding.root,
                            "Api Failed ${result.exception}",
                            Snackbar.LENGTH_LONG
                        ),
                        maxLines = 4
                    )
                    binding.scheduleTimeRecyclerview.isVisible = false

                    Timber.d("API Failed ${result.exception}")
                }

                is DataSourceResult.Loading -> {
                    Timber.d("API Loading")
                }
            }
        }

        isUpdatingWeek = false
    }


    override fun handleEvent(event: Event) {
        when (event) {
            is NavigateEvent.ByDirections -> {
                findNavController().navigate(event.directions)
            }

            is ShowToastEvent -> {
                event.toast.show()
            }

            is ShowSnackBarEvent -> {
                val snackBar = event.snackBar.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.amazingtalker_green_700
                    )
                )

                val snackBarView = snackBar.view
                val snackTextView =
                    snackBarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
                snackTextView.maxLines = event.maxLines
                snackBar.show()
            }
            // ...handle other events
        }
    }

    private fun setPreviousWeekButtonState(date: OffsetDateTime) {
        with(binding.buttonLastWeek) {
            if (date < OffsetDateTime.now(ZoneId.systemDefault())) {
                colorFilter = null
                setOnClickListener(null)
            } else {
                setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.amazingtalker_green_900
                    )
                )
                setOnClickListener { viewModel.updateWeek(WeekAction.PREVIOUS_WEEK) }
            }
        }
    }

    private fun setNextWeekButtonState(date: OffsetDateTime) {
        with(binding.buttonNextWeek) {
            setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.amazingtalker_green_900
                )
            )
            setOnClickListener { viewModel.updateWeek(WeekAction.NEXT_WEEK) }
        }
    }

    private fun setupTabs(
        options: MutableList<OffsetDateTime>
    ) {
        with(binding.tablayout) {
            val tabWidth = width / 3
            val tabHeight = height
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
            addTabSelectedListener(this)
        }

    }


    private fun addTabSelectedListener(tabLayout: TabLayout) {
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Timber.e("======onTabSelected====${tab.tag}")

                val date = tab.tag as? OffsetDateTime
                if (date != null) {
                    viewModel.onTabSelected(date, position = tab.position)
                } else {
                    Timber.e("Invalid tab tag, expected OffsetDateTime but got ${tab.tag?.javaClass?.name}")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onItemClick(item: IntervalScheduleTimeSlot) {
        viewModel.navigateToScheduleDetail(item)
    }
}