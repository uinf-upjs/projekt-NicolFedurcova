package sk.upjs.hackstock.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sk.upjs.hackstock.MainApplication
import sk.upjs.hackstock.entities.User
import sk.upjs.hackstock.repositories.AppRepository


class LoginViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = appRepository.getUserByEmailAndPassword(username, password)
            _currentUser.postValue(user)
        }
    }

    fun logout() {
        _currentUser.postValue(null)
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