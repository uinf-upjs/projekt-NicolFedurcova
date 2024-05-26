package sk.upjs.hackstock.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sk.upjs.hackstock.repositories.AppRepository


class DetailViewModel (private val appRepository: AppRepository) : ViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = "This is ai DETAIL Fragment"
    }
    val text: LiveData<String> = _text

    class DetailViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(appRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}