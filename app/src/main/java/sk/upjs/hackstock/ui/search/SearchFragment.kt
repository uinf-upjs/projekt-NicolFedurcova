

package sk.upjs.hackstock.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
<<<<<<< HEAD
import sk.upjs.hackstock.MainApplication
=======
import androidx.recyclerview.widget.LinearLayoutManager
>>>>>>> search
import sk.upjs.hackstock.R
import sk.upjs.hackstock.databinding.FragmentSearchBinding
import sk.upjs.hackstock.ui.home.HomeViewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchResultsAdapter: SearchResultsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //THIS ADDED TO COMMUNICATE WITH DB
        val application = requireNotNull(this.activity).application
        val usersRepository = (application as MainApplication).repository
        val factory = SearchViewModel.SearchViewModelFactory(usersRepository)
        //TO HERE
        val searchViewModel =
            ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Assuming you have a search button and a search input field in your layout
        binding.searchButton?.setOnClickListener {
            val searchTerm = binding.searchInput?.text.toString()
            searchViewModel.searchStocks(searchTerm)
        }


        searchResultsAdapter = SearchResultsAdapter(listOf())
        binding.searchResultsRecyclerView?.layoutManager = LinearLayoutManager(context)
        binding.searchResultsRecyclerView?.adapter = searchResultsAdapter

        searchViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            searchResultsAdapter.updateResults(results)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}