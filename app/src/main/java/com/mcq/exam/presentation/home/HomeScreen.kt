package com.mcq.exam.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mcq.exam.domain.model.Exam

@Composable
fun HomeScreen(
    onExamClick: (String) -> Unit,
    onHistoryClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadExams()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MCQ Exam App") },
                actions = {
                    IconButton(onClick = onHistoryClick) {
                        Text("History")
                    }
                }
            )
        }
    ) { padding ->
        when (val currentState = state) {
            is HomeState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is HomeState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "Available Exams",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    items(currentState.exams) { exam ->
                        ExamCard(
                            exam = exam,
                            onClick = { onExamClick(exam.id) }
                        )
                    }
                }
            }
            is HomeState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = currentState.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadExams() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExamCard(
    exam: Exam,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = exam.title,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = exam.subject.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "${exam.totalQuestions} questions",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${exam.durationMinutes} min",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Pass: ${exam.passingPercentage}%",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
