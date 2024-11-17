package com.uvg.directhealth.data.network

import com.uvg.directhealth.data.network.dto.AppointmentDto
import com.uvg.directhealth.data.network.dto.LoginDto
import com.uvg.directhealth.data.network.dto.LoginResponseDto
import com.uvg.directhealth.data.network.dto.PrescriptionDto
import com.uvg.directhealth.data.network.dto.RegisterDto
import com.uvg.directhealth.data.network.dto.UserDto
import com.uvg.directhealth.domain.network.DirectHealthApi
import com.uvg.directhealth.util.NetworkError
import com.uvg.directhealth.util.Result
import com.uvg.directhealth.util.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
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

    override suspend fun register(registerDto: RegisterDto): Result<String, NetworkError> {
        return safeCall<String> {
            httpClient.post(
                "https://direct-health-d73893a26328.herokuapp.com/api/auth/register"
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    registerDto
                )
            }
        }
    }

    override suspend fun getAllUsers(): Result<List<UserDto>, NetworkError> {
        return safeCall {
            httpClient.get("https://direct-health-d73893a26328.herokuapp.com/api/user/all") {
                contentType(ContentType.Application.Json)
            }.body()
        }
    }

    override suspend fun getUserById(id: String): Result<UserDto, NetworkError> {
        return safeCall {
            httpClient.get("https://direct-health-d73893a26328.herokuapp.com/api/user/$id") {
                contentType(ContentType.Application.Json)
            }.body()
        }
    }

    override suspend fun updateUser(id: String, user: UserDto): Result<UserDto, NetworkError> {
        return safeCall {
            httpClient.put("https://direct-health-d73893a26328.herokuapp.com/api/user/$id") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }.body()
        }
    }

    override suspend fun getAllPrescriptions(userId: String?): Result<List<PrescriptionDto>, NetworkError> {
        return safeCall {
            val url = "https://direct-health-d73893a26328.herokuapp.com/api/prescription/all" +
                    (if (userId != null) "?userId=$userId" else "")
            httpClient.get(url).body()
        }
    }

    override suspend fun getPrescriptionById(id: String): Result<PrescriptionDto, NetworkError> {
        return safeCall {
            httpClient.get("https://direct-health-d73893a26328.herokuapp.com/api/prescription/$id").body()
        }
    }

    override suspend fun addPrescription(prescription: PrescriptionDto): Result<PrescriptionDto, NetworkError> {
        return safeCall {
            httpClient.post("https://direct-health-d73893a26328.herokuapp.com/api/prescription/add") {
                contentType(ContentType.Application.Json)
                setBody(prescription)
            }.body()
        }
    }

    override suspend fun getAllAppointments(userId: String?): Result<List<AppointmentDto>, NetworkError> {
        return safeCall {
            val url = "https://direct-health-d73893a26328.herokuapp.com/api/appointment/all" +
                    (if (userId != null) "?userId=$userId" else "")
            httpClient.get(url).body()
        }
    }

    override suspend fun getAppointmentById(id: String): Result<AppointmentDto, NetworkError> {
        return safeCall {
            httpClient.get("https://direct-health-d73893a26328.herokuapp.com/api/appointment/$id").body()
        }
    }

    override suspend fun addAppointment(appointment: AppointmentDto): Result<AppointmentDto, NetworkError> {
        return safeCall {
            httpClient.post("https://direct-health-d73893a26328.herokuapp.com/api/appointment/add") {
                contentType(ContentType.Application.Json)
                setBody(appointment)
            }.body()
        }
    }
}