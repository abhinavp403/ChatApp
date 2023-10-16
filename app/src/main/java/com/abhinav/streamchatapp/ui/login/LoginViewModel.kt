package com.abhinav.streamchatapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhinav.streamchatapp.util.Constants.MIN_USERNAME_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: ChatClient
): ViewModel() {

    private val _loginEvent = MutableSharedFlow<LogInEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    private fun isValidUsername(username: String) =
        username.length >= MIN_USERNAME_LENGTH

    fun connectUser(username: String) {
        val trimmedUsername = username.trim()
        viewModelScope.launch {
            if(isValidUsername(trimmedUsername)) {
                val result = client.connectGuestUser(
                    userId = trimmedUsername,
                    username = trimmedUsername
                ).await()

                if(result.isError) {
                    _loginEvent.emit(LogInEvent.ErrorLogIn(result.error().message ?: "Unknown Error"))
                    return@launch
                }
                _loginEvent.emit(LogInEvent.Success)
            } else {
                _loginEvent.emit(LogInEvent.ErrorInputTooShort)
            }
        }
    }

    // distinguish between different type of login events which can occur and send the respective result to fragment
    sealed class LogInEvent {
        object ErrorInputTooShort : LogInEvent()  //isValidUsername() returns false
        data class ErrorLogIn(val error: String) : LogInEvent()  //server side login issue
        object Success : LogInEvent()  // isValidUsername() returns true
    }
}