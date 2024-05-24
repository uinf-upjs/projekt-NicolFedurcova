package sk.upjs.hackstock.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
<<<<<<< HEAD
import sk.upjs.hackstock.repositories.AppRepository


=======
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sk.upjs.hackstock.repositories.AppRepository

>>>>>>> search
class SearchViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is search Fragment"
    }
    val text: LiveData<String> = _text

<<<<<<< HEAD
=======
    private val _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> = _searchResults


    private val searchPage = SearchPage()

    fun searchStocks(term: String) {
        viewModelScope.launch {
            searchPage.searchTerm = term
            searchPage.handleSearch()
            _searchResults.value = searchPage.searchResults
        }
    }

>>>>>>> search
    class SearchViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                return SearchViewModel(appRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}