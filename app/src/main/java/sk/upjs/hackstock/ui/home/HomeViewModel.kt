package sk.upjs.hackstock.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import sk.upjs.hackstock.entities.User
import sk.upjs.hackstock.repositories.AppRepository

class HomeViewModel(private val appRepository: AppRepository) : ViewModel() {

    val text: LiveData<List<User>> = appRepository.allUsers.asLiveData()

    class HomeViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(appRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}