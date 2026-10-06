package com.mcq.exam.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcq.exam.domain.model.Exam
import com.mcq.exam.domain.usecase.GetExamsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getExamsUseCase: GetExamsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun loadExams() {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            getExamsUseCase().fold(
                onSuccess = { exams ->
                    _state.value = HomeState.Success(exams)
                },
                onFailure = { error ->
                    _state.value = HomeState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
}

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val exams: List<Exam>) : HomeState()
    data class Error(val message: String) : HomeState()
}
