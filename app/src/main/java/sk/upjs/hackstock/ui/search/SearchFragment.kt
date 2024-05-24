

package sk.upjs.hackstock.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import sk.upjs.hackstock.R
import sk.upjs.hackstock.databinding.FragmentSearchBinding


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
        val searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

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