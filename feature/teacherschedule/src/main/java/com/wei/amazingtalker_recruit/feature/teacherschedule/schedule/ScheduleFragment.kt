package com.wei.amazingtalker_recruit.feature.teacherschedule.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow

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

    override fun FragmentScheduleBinding.addOnClickListener() {
    }

    override fun FragmentScheduleBinding.handleState(
        viewLifecycleOwner: LifecycleOwner,
        states: StateFlow<ScheduleViewState>
    ) {
        states.let {

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