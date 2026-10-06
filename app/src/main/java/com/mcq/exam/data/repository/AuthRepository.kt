package com.mcq.exam.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mcq.exam.data.remote.api.AuthApi
import com.mcq.exam.data.remote.api.AuthResponseDto
import com.mcq.exam.data.remote.api.LoginRequest
import com.mcq.exam.data.remote.api.RegisterRequest
import com.mcq.exam.data.remote.mapper.DtoMapper
import com.mcq.exam.domain.model.AuthResponse
import com.mcq.exam.domain.model.User
import com.mcq.exam.domain.repository.IAuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

private val TOKEN_KEY = stringPreferencesKey("auth_token")
private val USER_ID_KEY = stringPreferencesKey("user_id")
private val USER_NAME_KEY = stringPreferencesKey("user_name")
private val USER_EMAIL_KEY = stringPreferencesKey("user_email")

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authApi: AuthApi
) : IAuthRepository {

    override suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val request = RegisterRequest(name, email, password)
            val response = authApi.register(request)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                saveAuthData(authResponse)
                Result.success(
                    AuthResponse(
                        user = DtoMapper.toDomain(authResponse.user),
                        token = authResponse.token
                    )
                )
            } else {
                Result.failure(Exception("Registration failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val request = LoginRequest(email, password)
            val response = authApi.login(request)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                saveAuthData(authResponse)
                Result.success(
                    AuthResponse(
                        user = DtoMapper.toDomain(authResponse.user),
                        token = authResponse.token
                    )
                )
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            val user = context.dataStore.data.map { preferences ->
                val userId = preferences[USER_ID_KEY]
                val userName = preferences[USER_NAME_KEY]
                val userEmail = preferences[USER_EMAIL_KEY]

                if (userId != null && userName != null && userEmail != null) {
                    User(userId, userName, userEmail)
                } else {
                    null
                }
            }.first()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.first()
    }

    private suspend fun saveAuthData(authResponse: AuthResponseDto) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = authResponse.token
            preferences[USER_ID_KEY] = authResponse.user.id
            preferences[USER_NAME_KEY] = authResponse.user.name
            preferences[USER_EMAIL_KEY] = authResponse.user.email
        }
    }
}
