package com.uvg.directhealth.layouts.mainFlow.appointment

sealed interface AppointmentListEvent {
    data object PopulateData: AppointmentListEvent
}