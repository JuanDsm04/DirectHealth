package com.uvg.directhealth.db

import java.time.LocalDateTime

data class Appointment(
    val id: String,
    val doctorId: String,
    val patientId: String,
    val date: LocalDateTime
)