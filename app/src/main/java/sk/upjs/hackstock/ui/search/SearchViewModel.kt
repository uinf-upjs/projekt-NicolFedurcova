package sk.upjs.hackstock.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sk.upjs.hackstock.repositories.AppRepository


class SearchViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is search Fragment"
    }
    val text: LiveData<String> = _text

    class SearchViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                return SearchViewModel(appRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}