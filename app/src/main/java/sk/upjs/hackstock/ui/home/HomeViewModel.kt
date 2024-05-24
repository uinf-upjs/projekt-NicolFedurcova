package sk.upjs.hackstock.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import sk.upjs.hackstock.entities.Activity
import sk.upjs.hackstock.entities.Question
import sk.upjs.hackstock.entities.Share
import sk.upjs.hackstock.entities.User
import sk.upjs.hackstock.repositories.AppRepository

class HomeViewModel(private val appRepository: AppRepository) : ViewModel() {

    val text: LiveData<List<Activity>> = appRepository.activitiesOfUser(1).asLiveData()
    val text2: LiveData<List<Share>> = appRepository.sharesOfUser(2).asLiveData()

    class HomeViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(appRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}