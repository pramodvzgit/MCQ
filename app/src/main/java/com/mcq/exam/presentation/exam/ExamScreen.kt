package com.mcq.exam.presentation.exam

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mcq.exam.domain.model.Question
import com.mcq.exam.domain.model.Option

@Composable
fun ExamScreen(
    attemptId: String,
    onSubmit: (Map<String, String>) -> Unit,
    onBack: () -> Unit,
    viewModel: ExamViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val answers by viewModel.answers.collectAsState()

    LaunchedEffect(attemptId) {
        viewModel.loadExamSession(attemptId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exam") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                },
                actions = {
                    when (val timerState = state) {
                        is ExamState.Success -> {
                            TimerDisplay(
                                remainingSeconds = timerState.remainingSeconds,
                                onTimeUp = { viewModel.submitExam() }
                            )
                        }
                        else -> {}
                    }
                }
            )
        }
    ) { padding ->
        when (val currentState = state) {
            is ExamState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ExamState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    // Question navigation palette
                    QuestionPalette(
                        questions = currentState.questions,
                        currentQuestionIndex = currentState.currentQuestionIndex,
                        answers = answers,
                        onQuestionClick = { index -> viewModel.setCurrentQuestion(index) }
                    )

                    // Current question
                    QuestionCard(
                        question = currentState.questions[currentState.currentQuestionIndex],
                        selectedOptionId = answers[currentState.questions[currentState.currentQuestionIndex].id],
                        onOptionSelect = { optionId ->
                            viewModel.selectAnswer(
                                currentState.questions[currentState.currentQuestionIndex].id,
                                optionId
                            )
                        }
                    )

                    // Navigation buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { viewModel.previousQuestion() },
                            enabled = currentState.currentQuestionIndex > 0
                        ) {
                            Text("Previous")
                        }

                        Button(
                            onClick = { viewModel.nextQuestion() },
                            enabled = currentState.currentQuestionIndex < currentState.questions.size - 1
                        ) {
                            Text("Next")
                        }
                    }

                    // Submit button
                    Button(
                        onClick = { viewModel.submitExam() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Submit Exam")
                    }
                }
            }
            is ExamState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentState.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    LaunchedEffect(state) {
        if (state is ExamState.Submitted) {
            onSubmit(answers)
        }
    }
}

@Composable
fun QuestionPalette(
    questions: List<Question>,
    currentQuestionIndex: Int,
    answers: Map<String, String>,
    onQuestionClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Question Navigation",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.height(100.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(questions.size) { index ->
                val question = questions[index]
                val isAnswered = answers.containsKey(question.id)
                val isCurrent = index == currentQuestionIndex

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> MaterialTheme.colorScheme.primary
                                isAnswered -> MaterialTheme.colorScheme.secondary
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                        .border(
                            width = if (isCurrent) 2.dp else 1.dp,
                            color = if (isCurrent) MaterialTheme.colorScheme.primary else Color.Gray,
                            shape = CircleShape
                        )
                        .clickable { onQuestionClick(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = if (isCurrent || isAnswered) Color.White else Color.Black,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionCard(
    question: Question,
    selectedOptionId: String?,
    onOptionSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = question.questionText,
                style = MaterialTheme.typography.bodyLarge
            )

            question.options.forEach { option ->
                OptionButton(
                    option = option,
                    isSelected = selectedOptionId == option.id,
                    onClick = { onOptionSelect(option.id) }
                )
            }
        }
    }
}

@Composable
fun OptionButton(
    option: Option,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = option.optionText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TimerDisplay(
    remainingSeconds: Int,
    onTimeUp: () -> Unit
) {
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds <= 0) {
            onTimeUp()
        }
    }

    Text(
        text = String.format("%02d:%02d", minutes, seconds),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = if (remainingSeconds < 60) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
    )
}
