package com.mcq.exam.presentation.exams

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mcq.exam.domain.model.Exam

@Composable
fun ExamDetailScreen(
    examId: String,
    onStartExam: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: ExamDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(examId) {
        viewModel.loadExam(examId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exam Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        when (val currentState = state) {
            is ExamDetailState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ExamDetailState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = currentState.exam.title,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    if (currentState.exam.description != null) {
                        Text(
                            text = currentState.exam.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InfoRow("Subject", currentState.exam.subject.name)
                            InfoRow("Questions", currentState.exam.totalQuestions.toString())
                            InfoRow("Duration", "${currentState.exam.durationMinutes} minutes")
                            InfoRow("Passing Score", "${currentState.exam.passingPercentage}%")
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Instructions",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "• Read each question carefully",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "• Select the best answer for each question",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "• You can navigate between questions",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "• Submit your exam before time runs out",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Button(
                        onClick = { onStartExam(currentState.exam.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Start Exam")
                    }
                }
            }
            is ExamDetailState.Error -> {
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
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.loadExam(examId) }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}
