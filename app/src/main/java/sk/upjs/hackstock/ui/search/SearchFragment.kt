package sk.upjs.hackstock.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import sk.upjs.hackstock.MainApplication
import sk.upjs.hackstock.R
import sk.upjs.hackstock.databinding.FragmentSearchBinding
import sk.upjs.hackstock.ui.home.HomeViewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


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

        val textView: TextView = binding.textSearch
        searchViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}