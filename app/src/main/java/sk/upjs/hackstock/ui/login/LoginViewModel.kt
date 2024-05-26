package sk.upjs.hackstock.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sk.upjs.hackstock.MainApplication
import sk.upjs.hackstock.entities.User
import sk.upjs.hackstock.repositories.AppRepository


class LoginViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser
    private var loginAttempts = 0
    private val _warningMessage = MutableLiveData<String>()
    val warningMessage: LiveData<String> = _warningMessage

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = appRepository.getUserByEmailAndPassword(username, password)
            _currentUser.postValue(user)
            if (user != null) {
                // Login successful, reset login attempts
                loginAttempts = 0
                // Save user session data to SharedPreferences
                MainApplication.prefs.edit().apply {
                    putString("user_email", user.email)
                    putLong("user_id", user.userId)
                    apply()
                }
            } else{
                loginAttempts++
                // Update warning message based on attempts
                updateWarningMessage()
                // Disable login button after 3 attempts
                if (loginAttempts >= 3) {
                    loginAttempts=0
                }
            }
        }
    }

    private fun updateWarningMessage() {
        when (loginAttempts) {
            1 -> _warningMessage.value = "Invalid username or password. Please try again."
            2 -> _warningMessage.value = "Incorrect credentials again. Please try again."
            3 -> _warningMessage.value = "Too many failed login attempts. Give your memory a rest and please try again later :)"
        }
    }

    private fun disableLoginButtonFor20Seconds() {
        // Disable login button
        // Start a coroutine to enable it after 20 seconds
        viewModelScope.launch {
            delay(20000) // 20 seconds
            // Enable login button
        }
    }

    fun logout() {
        _currentUser.postValue(null)
        viewModelScope.launch {
            // Clear user session data
            appRepository.clearUserSession()
        }
    }

    class LoginViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(appRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}