package com.mcq.exam.data.remote.mapper

import com.mcq.exam.data.remote.dto.ExamDto
import com.mcq.exam.data.remote.dto.SubjectDto
import com.mcq.exam.domain.model.Exam
import com.mcq.exam.domain.model.Subject
import org.junit.Assert.assertEquals
import org.junit.Test

class DtoMapperTest {

    @Test
    fun `toDomain should map ExamDto to Exam`() {
        // Arrange
        val examDto = ExamDto(
            id = "123",
            title = "Test Exam",
            description = "Test Description",
            subject = SubjectDto("456", "Mathematics"),
            durationMinutes = 30,
            totalQuestions = 10,
            passingPercentage = 70.0
        )

        // Act
        val exam = DtoMapper.toDomain(examDto)

        // Assert
        assertEquals("123", exam.id)
        assertEquals("Test Exam", exam.title)
        assertEquals("Test Description", exam.description)
        assertEquals("456", exam.subject.id)
        assertEquals("Mathematics", exam.subject.name)
        assertEquals(30, exam.durationMinutes)
        assertEquals(10, exam.totalQuestions)
        assertEquals(70.0, exam.passingPercentage)
    }
}
