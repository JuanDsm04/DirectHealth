package com.uvg.directhealth.layouts.mainFlow.user.directory

sealed interface UserDirectoryEvent {
    data object PopulateData: UserDirectoryEvent
}