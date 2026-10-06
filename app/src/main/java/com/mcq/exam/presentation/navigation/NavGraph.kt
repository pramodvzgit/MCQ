package com.mcq.exam.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mcq.exam.domain.usecase.GetCurrentAuthUserUseCase
import com.mcq.exam.presentation.exam.ExamScreen
import com.mcq.exam.presentation.exams.ExamDetailScreen
import com.mcq.exam.presentation.home.HomeScreen
import com.mcq.exam.presentation.result.ResultScreen
import com.mcq.exam.presentation.review.ReviewScreen
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Login.route) {
            // LoginScreen(navController = navController)
            // For simplicity, skip login and go to home
            LaunchedEffect(Unit) {
                delay(1000)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onExamClick = { examId ->
                    navController.navigate(Screen.ExamDetail.createRoute(examId))
                },
                onHistoryClick = {
                    navController.navigate(Screen.History.route)
                }
            )
        }

        composable(Screen.ExamList.route) {
            // Reuse HomeScreen for exam list
            HomeScreen(
                onExamClick = { examId ->
                    navController.navigate(Screen.ExamDetail.createRoute(examId))
                },
                onHistoryClick = {
                    navController.navigate(Screen.History.route)
                }
            )
        }

        composable(
            route = Screen.ExamDetail.route,
            arguments = listOf(navArgument("examId") { type = NavType.StringType })
        ) { backStackEntry ->
            val examId = backStackEntry.arguments?.getString("examId") ?: return@composable
            ExamDetailScreen(
                examId = examId,
                onStartExam = { examId ->
                    // For simplicity, navigate to exam screen with examId
                    // In real app, you would call startExam API first
                    navController.navigate(Screen.Exam.createRoute(examId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Exam.route,
            arguments = listOf(navArgument("attemptId") { type = NavType.StringType })
        ) { backStackEntry ->
            val attemptId = backStackEntry.arguments?.getString("attemptId") ?: return@composable
            ExamScreen(
                attemptId = attemptId,
                onSubmit = { answers ->
                    // Navigate to result screen
                    navController.navigate(Screen.Result.createRoute(attemptId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(navArgument("attemptId") { type = NavType.StringType })
        ) { backStackEntry ->
            val attemptId = backStackEntry.arguments?.getString("attemptId") ?: return@composable
            // For simplicity, show a placeholder result
            // In real app, you would load the actual result from API
            ResultScreen(
                result = com.mcq.exam.domain.repository.IQuestionRepository.SubmitResult(
                    attemptId = attemptId,
                    score = 15.0,
                    percentage = 75.0,
                    correctAnswers = 15,
                    incorrectAnswers = 5,
                    unansweredQuestions = 0,
                    passed = true,
                    questions = emptyList()
                ),
                onReview = {
                    navController.navigate(Screen.Review.createRoute(attemptId))
                },
                onRetake = {
                    navController.popBackStack(Screen.ExamDetail.route, inclusive = false)
                },
                onHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Review.route,
            arguments = listOf(navArgument("attemptId") { type = NavType.StringType })
        ) { backStackEntry ->
            val attemptId = backStackEntry.arguments?.getString("attemptId") ?: return@composable
            // For simplicity, show placeholder review
            ReviewScreen(
                questions = emptyList(),
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.History.route) {
            // Placeholder for history screen
            androidx.compose.material3.Scaffold { padding ->
                androidx.compose.foundation.layout.Box(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    androidx.compose.material3.Text("History Screen - Coming Soon")
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    androidx.compose.material3.Surface {
        androidx.compose.foundation.layout.Box(
            modifier = androidx.compose.ui.Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.material3.Text(
                text = "MCQ Exam App",
                style = androidx.compose.material3.MaterialTheme.typography.headlineLarge
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }
}
