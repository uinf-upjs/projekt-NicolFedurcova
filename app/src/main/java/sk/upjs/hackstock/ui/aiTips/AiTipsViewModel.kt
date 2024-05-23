package sk.upjs.hackstock.ui.aiTips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sk.upjs.hackstock.repositories.AppRepository


class AiTipsViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is ai tips Fragment"
    }
    val text: LiveData<String> = _text

    class AiTipsViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AiTipsViewModel::class.java)) {
                return AiTipsViewModel(appRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }


}