package com.uvg.directhealth.layouts.mainFlow.user.profile

import com.uvg.directhealth.domain.model.User
import java.util.Calendar

data class UserProfileState(
    val loggedUser: User? = null,
    val userProfile: User? = null,
    val selectedDate: String = "dd/mm/yyyy",
    val selectedTime: String = "00:00",
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val initialHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    val initialMinute: Int = Calendar.getInstance().get(Calendar.MINUTE),
    val is24Hour: Boolean = true
)