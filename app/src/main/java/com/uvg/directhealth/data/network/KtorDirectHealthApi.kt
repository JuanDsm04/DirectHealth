package com.uvg.directhealth.data.network

import com.uvg.directhealth.data.network.dto.LoginDto
import com.uvg.directhealth.data.network.dto.LoginResponseDto
import com.uvg.directhealth.domain.network.DirectHealthApi
import com.uvg.directhealth.util.NetworkError
import com.uvg.directhealth.util.Result
import com.uvg.directhealth.util.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class KtorDirectHealthApi(private val httpClient: HttpClient): DirectHealthApi {
    override suspend fun login(email: String, password: String): Result<LoginResponseDto, NetworkError> {
        return safeCall<LoginResponseDto> {
            httpClient.post(
                "https://direct-health-d73893a26328.herokuapp.com/api/auth/login"
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    LoginDto(
                        email = email,
                        password = password
                    )
                )
            }
        }
    }
}