package com.mcq.exam.di

import com.mcq.exam.data.repository.AttemptRepository
import com.mcq.exam.data.repository.AuthRepository
import com.mcq.exam.data.repository.ExamRepository
import com.mcq.exam.data.repository.QuestionRepository
import com.mcq.exam.domain.repository.IAttemptRepository
import com.mcq.exam.domain.repository.IAuthRepository
import com.mcq.exam.domain.repository.IExamRepository
import com.mcq.exam.domain.repository.IQuestionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExamRepository(examRepository: ExamRepository): IExamRepository

    @Binds
    @Singleton
    abstract fun bindQuestionRepository(questionRepository: QuestionRepository): IQuestionRepository

    @Binds
    @Singleton
    abstract fun bindAttemptRepository(attemptRepository: AttemptRepository): IAttemptRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepository: AuthRepository): IAuthRepository
}
