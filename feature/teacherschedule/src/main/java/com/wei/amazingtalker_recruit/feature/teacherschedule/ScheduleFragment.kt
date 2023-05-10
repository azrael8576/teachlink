package com.wei.amazingtalker_recruit.feature.teacherschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.wei.amazingtalker_recruit.core.extensions.observeEvent
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.models.ShowSnackBarEvent
import com.wei.amazingtalker_recruit.core.models.ShowToastEvent
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
class ScheduleFragment : Fragment(), OnItemClickListener {

    @Inject
    lateinit var adapter: ScheduleTimeListAdapter
    private val viewModel: ScheduleViewModel by viewModels()
    private var binding: FragmentScheduleBinding? = null
    private var isUpdateWeek = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentScheduleBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            adapter.setOnClickListener(this@ScheduleFragment)
            scheduleTimeRecyclerview.adapter = adapter
            subscribeUi(this, adapter)
            addOnTabSelectedListener(this)

            viewModel.showSnackBar(
                Snackbar.make(
                    root,
                    String.format(
                        requireContext().getString(
                            R.string.inquirying_teacher_calendar,
                            viewModel.currentTeacherName.value
                        )
                    ),
                    Snackbar.LENGTH_LONG
                )
            )
        }
    }

    private fun addOnTabSelectedListener(
        binding: FragmentScheduleBinding
    ) {
        binding.tablayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Timber.d("======onTabSelected====${tab.tag}")

                viewModel.onTabSelected(tab.tag.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun subscribeUi(binding: FragmentScheduleBinding, adapter: ScheduleTimeListAdapter) {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weekStart.collect {
                setButtonLastWeekBehavior(binding, it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weekEnd.collect {
                setButtonNextWeekBehavior(binding, it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weekDateText.collect {
                binding.textWeek.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.dateTabs.collect { dateTabOffsetDateTimeOptions ->
                binding.tablayout.doOnLayout {
                    putTabToTablayoutByOptions(binding, dateTabOffsetDateTimeOptions)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.filteredTimeList.collect { result ->
                if (!isUpdateWeek) {
                    when (result) {
                        is DataSourceResult.Success -> {
                            result.data.let {
                                adapter.addHeaderAndSubmitList(
                                    it
                                )
                            }
                            binding.scheduleTimeRecyclerview.isVisible = true
                            binding.scheduleTimeRecyclerview?.scrollToPosition(0)

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

                if (isUpdateWeek) {
                    isUpdateWeek = false
                }
            }

        }

        // Observe events
        viewModel.events.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is NavigateEvent -> {
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

    }

    private fun setButtonLastWeekBehavior(binding: FragmentScheduleBinding, it: OffsetDateTime) {
        if (it < OffsetDateTime.now(ZoneId.systemDefault())) {
            binding.buttonLastWeek.colorFilter = null
            binding.buttonLastWeek.setOnClickListener(null)
        } else {
            binding.buttonLastWeek.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.amazingtalker_green_900
                )
            )
            binding.buttonLastWeek.setOnClickListener(View.OnClickListener {
                isUpdateWeek = true
                viewModel.updateWeek(WeekAction.ACTION_LAST_WEEK)
            })
        }
    }

    private fun setButtonNextWeekBehavior(binding: FragmentScheduleBinding, it: OffsetDateTime) {
        binding.buttonNextWeek.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.amazingtalker_green_900
            )
        )
        binding.buttonNextWeek.setOnClickListener(View.OnClickListener {
            isUpdateWeek = true
            viewModel.updateWeek(WeekAction.ACTION_NEXT_WEEK)
        })
    }

    private fun putTabToTablayoutByOptions(
        binding: FragmentScheduleBinding,
        options: MutableList<OffsetDateTime>
    ) {
        val tabWidth = binding.tablayout.width / 3
        val tabHeight = binding.tablayout.height
        binding.tablayout.removeAllTabs()
        options.forEachIndexed { _, element ->
            binding.tablayout.newTab().run {
                setCustomView(R.layout.custom_tab)
                customView?.minimumWidth = tabWidth
                customView?.minimumHeight = tabHeight
                tag = element
                val dateFormatter = DateTimeFormatter.ofPattern("E, MMM dd")
                text = dateFormatter.format(element)
                binding.tablayout.addTab(this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onItemClick(item: IntervalScheduleTimeSlot) {
        viewModel.navigateToScheduleDetail(item)
    }
}