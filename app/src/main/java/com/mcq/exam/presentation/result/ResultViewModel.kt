package com.mcq.exam.presentation.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcq.exam.domain.repository.IQuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor() : ViewModel() {

    private val _result = MutableStateFlow<IQuestionRepository.SubmitResult?>(null)
    val result: StateFlow<IQuestionRepository.SubmitResult?> = _result.asStateFlow()

    fun setResult(result: IQuestionRepository.SubmitResult) {
        _result.value = result
    }
}
