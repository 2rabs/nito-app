package club.nito.feature.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import club.nito.core.domain.GetRecentScheduleUseCase
import club.nito.core.model.FetchSingleResult
import club.nito.core.model.Schedule
import club.nito.core.ui.buildUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopViewModel @Inject constructor(
    getRecentScheduleUseCase: GetRecentScheduleUseCase,
) : ViewModel() {
    private val showConfirmParticipateSchedule = MutableStateFlow<Schedule?>(null)

    private val recentSchedule = getRecentScheduleUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FetchSingleResult.Loading,
    )

    val uiState: StateFlow<TopScreenUiState> = buildUiState(
        showConfirmParticipateSchedule,
        recentSchedule,
    ) { showConfirmParticipateSchedule, recentSchedule ->
        TopScreenUiState(
            recentSchedule = recentSchedule,
            confirmParticipateDialog = showConfirmParticipateSchedule
                ?.let(ConfirmParticipateDialogUiState::Show)
                ?: ConfirmParticipateDialogUiState.Hide,
        )
    }

    private val _events = MutableStateFlow<List<TopEvent>>(emptyList())
    val event: Flow<TopEvent?> = _events.map { it.firstOrNull() }

    fun dispatch(intent: TopIntent) {
        viewModelScope.launch {
            when (intent) {
                is TopIntent.ClickShowConfirmParticipateDialog -> showConfirmParticipateSchedule.emit(intent.schedule)
                TopIntent.ClickDismissConfirmParticipateDialog -> showConfirmParticipateSchedule.emit(null)
                TopIntent.ClickScheduleList -> _events.emit(_events.value + TopEvent.NavigateToScheduleList)
                TopIntent.ClickSettings -> _events.emit(_events.value + TopEvent.NavigateToSettings)
            }
        }
    }

    fun consume(event: TopEvent) {
        viewModelScope.launch {
            _events.emit(_events.value.filterNot { it == event })
        }
    }
}
