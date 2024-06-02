package sk.upjs.hackstock.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sk.upjs.hackstock.MainApplication
import sk.upjs.hackstock.R
import sk.upjs.hackstock.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: ShareAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //THIS ADDED TO COMMUNICATE WITH DB
        val application = requireNotNull(this.activity).application
        val appRepository = (application as MainApplication).repository
        val factory = HomeViewModel.HomeViewModelFactory(appRepository, requireContext().applicationContext)
        //TO HERE
        val homeViewModel =
            ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        adapter = ShareAdapter(listOf(), this)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        recyclerView.adapter = adapter

        binding.text.text = homeViewModel.text

        // Observe LiveData in ViewModel
        homeViewModel.shares.observe(viewLifecycleOwner) { shares ->
            adapter.updateResults(shares)
        }


        return root
    }
    override fun onItemClick(position: Int) {
        // Navigate to new fragment based on item click
        val share = adapter.getCurrentShare(position)
        Log.e("HomeFragment", "Clicked on item: ${share.company}")
        val navController = findNavController()
        val bundle = Bundle().apply {
            putString("sourceFragment", "HomeFragment")
            putParcelable("share", share)

        }
        navController.navigate(R.id.action_home_to_detail, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}