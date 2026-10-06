package com.mcq.exam.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object ExamList : Screen("exam_list")
    object ExamDetail : Screen("exam_detail/{examId}") {
        fun createRoute(examId: String) = "exam_detail/$examId"
    }
    object Exam : Screen("exam/{attemptId}") {
        fun createRoute(attemptId: String) = "exam/$attemptId"
    }
    object Result : Screen("result/{attemptId}") {
        fun createRoute(attemptId: String) = "result/$attemptId"
    }
    object Review : Screen("review/{attemptId}") {
        fun createRoute(attemptId: String) = "review/$attemptId"
    }
    object History : Screen("history")
}
