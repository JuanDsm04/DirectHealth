package com.uvg.directhealth.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val result: String,
    val id: String,
    val role: String
)