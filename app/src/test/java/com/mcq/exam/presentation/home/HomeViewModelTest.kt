package com.mcq.exam.presentation.home

import com.mcq.exam.domain.model.Exam
import com.mcq.exam.domain.model.Subject
import com.mcq.exam.domain.usecase.GetExamsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @Mock
    private lateinit var getExamsUseCase: GetExamsUseCase

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(getExamsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadExams should update state to Success when repository returns exams`() = runTest {
        // Arrange
        val exams = listOf(
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
        Mockito.`when`(getExamsUseCase()).thenReturn(Result.success(exams))

        // Act
        viewModel.loadExams()

        // Assert
        val state = viewModel.state.value
        assert(state is HomeState.Success)
        assertEquals(exams, (state as HomeState.Success).exams)
    }

    @Test
    fun `loadExams should update state to Error when repository fails`() = runTest {
        // Arrange
        val error = Exception("Network error")
        Mockito.`when`(getExamsUseCase()).thenReturn(Result.failure(error))

        // Act
        viewModel.loadExams()

        // Assert
        val state = viewModel.state.value
        assert(state is HomeState.Error)
        assertEquals("Network error", (state as HomeState.Error).message)
    }
}
