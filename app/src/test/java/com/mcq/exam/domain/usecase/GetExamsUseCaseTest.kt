package com.mcq.exam.domain.usecase

import com.mcq.exam.domain.model.Exam
import com.mcq.exam.domain.model.Subject
import com.mcq.exam.domain.repository.IExamRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetExamsUseCaseTest {

    @Mock
    private lateinit var examRepository: IExamRepository

    private lateinit var getExamsUseCase: GetExamsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getExamsUseCase = GetExamsUseCase(examRepository)
    }

    @Test
    fun `invoke should return exams from repository`() = runTest {
        // Arrange
        val expectedExams = listOf(
            Exam(
                id = "1",
                title = "Test Exam",
                description = "Description",
                subject = Subject("1", "Math"),
                durationMinutes = 30,
                totalQuestions = 10,
                passingPercentage = 70.0
            )
        )
        Mockito.`when`(examRepository.getExams(null))
            .thenReturn(Result.success(expectedExams))

        // Act
        val result = getExamsUseCase()

        // Assert
        assertEquals(Result.success(expectedExams), result)
    }
}
