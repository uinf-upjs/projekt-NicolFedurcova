package sk.upjs.hackstock.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sk.upjs.hackstock.repositories.AppRepository


class GameViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is game Fragment"
    }
    val text: LiveData<String> = _text

    class GameViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                return GameViewModel(appRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}