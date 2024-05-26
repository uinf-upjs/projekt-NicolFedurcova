package sk.upjs.hackstock.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import sk.upjs.hackstock.MainApplication
import sk.upjs.hackstock.entities.Activity
import sk.upjs.hackstock.entities.Question
import sk.upjs.hackstock.entities.Share
import sk.upjs.hackstock.entities.User
import sk.upjs.hackstock.repositories.AppRepository

class HomeViewModel(private val appRepository: AppRepository, private val context: Context) : ViewModel() {

    val userId = context.applicationContext.getSharedPreferences(MainApplication.PREFS_NAME, Context.MODE_PRIVATE)
        .getLong(MainApplication.USER_ID_KEY, 0)

    //val text: String = userId.toString()
    val text: String = "My Shares " //+ userId

    val shares: LiveData<List<Share>> = appRepository.sharesOfUser(userId).asLiveData()



    class HomeViewModelFactory(private val appRepository: AppRepository, private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(appRepository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}