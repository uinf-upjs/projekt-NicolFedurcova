

package sk.upjs.hackstock.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController

import sk.upjs.hackstock.MainApplication

import androidx.recyclerview.widget.LinearLayoutManager

import sk.upjs.hackstock.R
import sk.upjs.hackstock.databinding.FragmentSearchBinding
import sk.upjs.hackstock.ui.home.HomeViewModel
import sk.upjs.hackstock.ui.home.OnItemClickListener


class SearchFragment : Fragment(), OnItemClickListener {

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
        super.onCreate(savedInstanceState)
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


        searchResultsAdapter = SearchResultsAdapter(listOf(), this)
        binding.searchResultsRecyclerView?.layoutManager = LinearLayoutManager(context)
        binding.searchResultsRecyclerView?.adapter = searchResultsAdapter

        searchViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            searchResultsAdapter.updateResults(results)
        }
        return root
    }

    override fun onItemClick(position: Int) {
        // Navigate to new fragment based on item click
        val share = searchResultsAdapter.getCurrentShare(position)
        Log.e("SearchFragment", "Clicked on item: ${share.name}")
        val navController = findNavController()
        val bundle = Bundle().apply {
            putString("sourceFragment", "SearchFragment")
            putString("company", share.name)
            putString("symbol", share.symbol)

        }
        navController.navigate(R.id.action_search_to_detail, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}