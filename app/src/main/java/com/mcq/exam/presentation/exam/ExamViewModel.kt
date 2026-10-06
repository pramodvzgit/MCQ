package com.mcq.exam.presentation.exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcq.exam.domain.model.ExamSession
import com.mcq.exam.domain.usecase.StartExamUseCase
import com.mcq.exam.domain.usecase.SubmitExamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val startExamUseCase: StartExamUseCase,
    private val submitExamUseCase: SubmitExamUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ExamState>(ExamState.Loading)
    val state: StateFlow<ExamState> = _state.asStateFlow()

    private val _answers = MutableStateFlow<Map<String, String>>(emptyMap())
    val answers: StateFlow<Map<String, String>> = _answers.asStateFlow()

    private var examSession: ExamSession? = null
    private var timerJob: kotlinx.coroutines.Job? = null

    fun loadExamSession(attemptId: String) {
        viewModelScope.launch {
            _state.value = ExamState.Loading
            // Note: In a real implementation, you would load the session from a repository
            // using the attemptId. For this demo, we assume the session is passed separately
            // via loadExamSessionFromSession method
            _state.value = ExamState.Error("Use loadExamSessionFromSession instead")
        }
    }

    fun loadExamSessionFromSession(session: ExamSession) {
        examSession = session
        _state.value = ExamState.Success(
            questions = session.questions,
            currentQuestionIndex = 0,
            remainingSeconds = session.durationMinutes * 60
        )
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val currentState = _state.value
                if (currentState is ExamState.Success) {
                    val newRemaining = currentState.remainingSeconds - 1
                    if (newRemaining <= 0) {
                        _state.value = currentState.copy(remainingSeconds = 0)
                        submitExam()
                        break
                    } else {
                        _state.value = currentState.copy(remainingSeconds = newRemaining)
                    }
                }
            }
        }
    }

    fun setCurrentQuestion(index: Int) {
        val currentState = _state.value
        if (currentState is ExamState.Success && index in currentState.questions.indices) {
            _state.value = currentState.copy(currentQuestionIndex = index)
        }
    }

    fun nextQuestion() {
        val currentState = _state.value
        if (currentState is ExamState.Success) {
            val nextIndex = currentState.currentQuestionIndex + 1
            if (nextIndex < currentState.questions.size) {
                _state.value = currentState.copy(currentQuestionIndex = nextIndex)
            }
        }
    }

    fun previousQuestion() {
        val currentState = _state.value
        if (currentState is ExamState.Success) {
            val prevIndex = currentState.currentQuestionIndex - 1
            if (prevIndex >= 0) {
                _state.value = currentState.copy(currentQuestionIndex = prevIndex)
            }
        }
    }

    fun selectAnswer(questionId: String, optionId: String) {
        _answers.value = _answers.value.toMutableMap().apply {
            put(questionId, optionId)
        }
    }

    fun submitExam() {
        timerJob?.cancel()
        viewModelScope.launch {
            val session = examSession ?: return@launch
            val currentState = _state.value
            if (currentState is ExamState.Success) {
                _state.value = ExamState.Submitting
                submitExamUseCase(session.attemptId, _answers.value).fold(
                    onSuccess = { result ->
                        _state.value = ExamState.Submitted(result)
                    },
                    onFailure = { error ->
                        _state.value = ExamState.Error(error.message ?: "Submission failed")
                    }
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

sealed class ExamState {
    object Loading : ExamState()
    data class Success(
        val questions: List<com.mcq.exam.domain.model.Question>,
        val currentQuestionIndex: Int,
        val remainingSeconds: Int
    ) : ExamState()
    object Submitting : ExamState()
    data class Submitted(val result: com.mcq.exam.domain.repository.IQuestionRepository.SubmitResult) : ExamState()
    data class Error(val message: String) : ExamState()
}
