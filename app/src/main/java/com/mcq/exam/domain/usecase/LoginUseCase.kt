package com.mcq.exam.domain.usecase

import com.mcq.exam.domain.model.AuthResponse
import com.mcq.exam.domain.repository.IAuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthResponse> {
        return authRepository.login(email, password)
    }
}

class RegisterUseCase @Inject constructor(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<AuthResponse> {
        return authRepository.register(name, email, password)
    }
}

class GetCurrentAuthUserUseCase @Inject constructor(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(): Result<com.mcq.exam.domain.model.User?> {
        return authRepository.getCurrentUser()
    }
}

class LogoutUseCase @Inject constructor(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}
