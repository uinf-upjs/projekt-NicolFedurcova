package sk.upjs.hackstock.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sk.upjs.hackstock.repositories.AppRepository


class QuizViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is quiz Fragment"
    }
    val text: LiveData<String> = _text

    class QuizViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
                return QuizViewModel(appRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}