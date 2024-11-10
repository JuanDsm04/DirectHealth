package com.uvg.directhealth.layouts.mainFlow.user.directory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.uvg.directhealth.data.model.Role
import com.uvg.directhealth.data.source.UserDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserDirectoryViewModel(savedStateHandle: SavedStateHandle): ViewModel() {
    private val userList = savedStateHandle.toRoute<UserDirectoryDestination>()
    private val _state = MutableStateFlow(UserDirectoryState())
    val state = _state.asStateFlow()

    private val userDb = UserDb()

    fun onEvent(event: UserDirectoryEvent) {
        when (event) {
            UserDirectoryEvent.PopulateData -> getData()
        }
    }

    private fun getData() {
        val loggedUser = userDb.getUserById(userList.userId)

        _state.update { state ->
            if (loggedUser.role == Role.DOCTOR) {
                state.copy(
                    userName = loggedUser.name,
                    userRole = loggedUser.role,
                    userList = userDb.getAllPatients()
                )
            } else {
                state.copy(
                    userName = loggedUser.name,
                    userRole = loggedUser.role,
                    userList = userDb.getAllDoctors()
                )
            }
        }
    }
}