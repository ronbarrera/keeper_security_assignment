package com.ronaldbarrera.keepersecuritylogin.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ronaldbarrera.keepersecuritylogin.R

class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    fun loginDataChanged(email: String, password: String) {
        if (email.isBlank() && password.isBlank()) {
            _loginForm.value = LoginFormState(isDataValid = false)
        } else if(!isEmailValid(email) && email.isNotBlank() && !isPasswordValid(password) && password.isNotBlank()) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email, passwordError = R.string.invalid_password)
        } else if (email.isNotBlank() && !isEmailValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (password.isNotBlank() && !isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else if (email.isBlank() || password.isBlank()) {
            _loginForm.value = LoginFormState(isDataValid = false)
        } else  {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}

data class LoginFormState(val emailError: Int? = null,
                          val passwordError: Int? = null,
                          val isDataValid: Boolean = false)