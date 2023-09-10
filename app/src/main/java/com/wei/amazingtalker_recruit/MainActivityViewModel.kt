package com.wei.amazingtalker_recruit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wei.amazingtalker_recruit.MainActivityUiState.Loading
import com.wei.amazingtalker_recruit.MainActivityUiState.Success
import com.wei.amazingtalker_recruit.core.data.repository.UserDataRepository
import com.wei.amazingtalker_recruit.core.model.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData.map {
        Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}
