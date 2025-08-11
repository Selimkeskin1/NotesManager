package com.notesmanager.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LoginViewModel : ViewModel() {
    var loginState = mutableStateOf(LoginState())
        private set


    @OptIn(ExperimentalTime::class)
    fun onUiEvent(loginUiEvent: LoginUiEvent) {
        when (loginUiEvent) {
            // Email/Mobile changed
            is LoginUiEvent.EmailOrMobileChanged -> {
                loginState.value = loginState.value.copy(
                    emailOrMobile = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(
                        emailOrMobileErrorState = if (loginUiEvent.inputValue.trim().isNotEmpty())
                            ErrorState()
                        else
                            emailOrMobileEmptyErrorState
                    )
                )
            }

            // Password changed
            is LoginUiEvent.PasswordChanged -> {
                loginState.value = loginState.value.copy(
                    password = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(
                        passwordErrorState = if (loginUiEvent.inputValue.trim().isNotEmpty())
                            ErrorState()
                        else
                            passwordEmptyErrorState
                    )
                )
            }

            // Submit Login
            is LoginUiEvent.Submit -> {
                val inputsValidated = validateInputs()
                if (inputsValidated) {
                    // TODO Trigger login in authentication flow

                    if(loginState.value.emailOrMobile != "selimkeskin1@gmail.com"){
                        loginState.value = loginState.value.copy(
                            errorState =  loginState.value.errorState.copy( wrongCridentials) )


                    }else{


                        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                        val hour = now.hour.toString().padStart(2,'0')
                        val minute = now.minute.toString().padStart(2,'0')
                        val day =  now.day.toString().padStart(2,'0')

                         val pwd = hour + minute  + day

                        if ( loginState.value.password == ( pwd  ) ){
                             loginState.value = loginState.value.copy(isLoginSuccessful = true)
                         }else{
                             loginState.value = loginState.value.copy(
                                 errorState =  loginState.value.errorState.copy( wrongCridentials) )
                         }
                    }


                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val emailOrMobileString = loginState.value.emailOrMobile.trim()
        val passwordString = loginState.value.password
        return when {

            // Email/Mobile empty
            emailOrMobileString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState(
                        emailOrMobileErrorState = emailOrMobileEmptyErrorState
                    )
                )
                false
            }

            //Password Empty
            passwordString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState(
                        passwordErrorState = passwordEmptyErrorState
                    )
                )
                false
            }

            // No errors
            else -> {
                // Set default error state
                loginState.value = loginState.value.copy(errorState = LoginErrorState())
                true
            }
        }
    }
}
