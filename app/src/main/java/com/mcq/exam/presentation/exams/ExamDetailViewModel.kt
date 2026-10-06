package com.mcq.exam.presentation.exams

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
class ExamDetailViewModel @Inject constructor(
    private val getExamsUseCase: GetExamsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ExamDetailState>(ExamDetailState.Loading)
    val state: StateFlow<ExamDetailState> = _state.asStateFlow()

    fun loadExam(examId: String) {
        viewModelScope.launch {
            _state.value = ExamDetailState.Loading
            getExamsUseCase().fold(
                onSuccess = { exams ->
                    val exam = exams.find { it.id == examId }
                    if (exam != null) {
                        _state.value = ExamDetailState.Success(exam)
                    } else {
                        _state.value = ExamDetailState.Error("Exam not found")
                    }
                },
                onFailure = { error ->
                    _state.value = ExamDetailState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
}

sealed class ExamDetailState {
    object Loading : ExamDetailState()
    data class Success(val exam: Exam) : ExamDetailState()
    data class Error(val message: String) : ExamDetailState()
}
