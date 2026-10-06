package com.mcq.exam.presentation.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mcq.exam.domain.model.QuestionWithAnswer

@Composable
fun ReviewScreen(
    questions: List<QuestionWithAnswer>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Answer Review") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(questions) { questionWithAnswer ->
                ReviewQuestionCard(questionWithAnswer)
            }
        }
    }
}

@Composable
fun ReviewQuestionCard(questionWithAnswer: QuestionWithAnswer) {
    val question = questionWithAnswer.question
    val userAnswer = questionWithAnswer.userAnswer

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Question
            Text(
                text = question.questionText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            // Options
            question.options.forEach { option ->
                val isUserSelected = userAnswer?.optionId == option.id
                val isCorrect = option.isCorrect == true
                val backgroundColor = when {
                    isUserSelected && isCorrect -> Color(0xFF4CAF50) // Green
                    isUserSelected && !isCorrect -> Color(0xFFF44336) // Red
                    isCorrect -> Color(0xFF81C784) // Light green
                    else -> MaterialTheme.colorScheme.surface
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = backgroundColor
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${option.optionOrder}. ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isUserSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        Text(
                            text = option.optionText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isUserSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        if (isCorrect) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text("✓", color = Color.White)
                        }
                    }
                }
            }

            // Explanation
            if (question.explanation != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Explanation:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // User's result
            if (userAnswer != null) {
                val resultText = when {
                    userAnswer.isCorrect == true -> "Correct (+${userAnswer.marksAwarded})"
                    userAnswer.isCorrect == false -> "Incorrect (${userAnswer.marksAwarded})"
                    else -> "Not answered"
                }
                val resultColor = when {
                    userAnswer.isCorrect == true -> Color(0xFF4CAF50)
                    userAnswer.isCorrect == false -> Color(0xFFF44336)
                    else -> Color.Gray
                }

                Text(
                    text = "Your answer: $resultText",
                    style = MaterialTheme.typography.bodyMedium,
                    color = resultColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
