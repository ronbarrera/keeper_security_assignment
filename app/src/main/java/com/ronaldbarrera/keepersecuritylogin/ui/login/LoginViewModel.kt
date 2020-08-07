package com.ronaldbarrera.keepersecuritylogin.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ronaldbarrera.keepersecuritylogin.R
import com.ronaldbarrera.keepersecuritylogin.data.LoginRepository
import com.ronaldbarrera.keepersecuritylogin.data.Result

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    init {
        updateIsloggedIn()
    }

    fun updateIsloggedIn() {
        _isLoggedIn.value = loginRepository.isLoggedIn
    }

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)
        if (result is Result.Success) {
            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
        updateIsloggedIn()
    }

    fun logout() {
        loginRepository.logout()
        _loginResult.value = LoginResult()
        _loginForm.value = LoginFormState()
        updateIsloggedIn()
    }

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

/**
 * Data validation state of the login form.
 */
data class LoginFormState(val emailError: Int? = null,
                          val passwordError: Int? = null,
                          val isDataValid: Boolean = false)

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String
)