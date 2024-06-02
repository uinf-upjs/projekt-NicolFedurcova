package sk.upjs.hackstock.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sk.upjs.hackstock.MainApplication
import sk.upjs.hackstock.R
import sk.upjs.hackstock.databinding.FragmentDetailBinding
import sk.upjs.hackstock.entities.Share
import sk.upjs.hackstock.repositories.AppRepository
import sk.upjs.hackstock.ui.search.SearchResult

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private lateinit var share: Share
    private lateinit var sourceFragment: String
    private lateinit var searchResult: SearchResult
    private lateinit var factory: DetailViewModel.DetailViewModelFactory
    private lateinit var textButton: String
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sourceFragment = it.getString("sourceFragment") ?: throw IllegalStateException("Source fragment is missing")
            if (sourceFragment == "HomeFragment") {
                share = it.getParcelable("share") ?: throw IllegalStateException("Share is missing")
            } else if (sourceFragment == "SearchFragment") {
                searchResult = SearchResult(it.getString("company") ?: throw IllegalStateException("Company is missing"), it.getString("symbol") ?: throw IllegalStateException("Symbol is missing"))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // THIS ADDED TO COMMUNICATE WITH DB
        val application = requireNotNull(this.activity).application
        val appRepository = (application as MainApplication).repository
        // val factory = DetailViewModel.DetailViewModelFactory(appRepository, share)
        if (sourceFragment == "HomeFragment") {
            textButton = "SELL"
            factory = DetailViewModel.DetailViewModelFactory(
                appRepository,
                share.company,
                share.shortname,
                "HomeFragment",
                share.amount,
                share.price
            )
        } else if (sourceFragment == "SearchFragment") {
            textButton = "BUY"
            factory = DetailViewModel.DetailViewModelFactory(
                appRepository,
                searchResult.name,
                searchResult.symbol,
                "SearchFragment", null, null)
        }
        // TO HERE
        val userName = MainApplication.prefs.getString("user_email", null)

        val detailViewModel =
            ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDetail
        // textView.text = share.company
        textView.text = detailViewModel.name

        val btnSell: Button = binding.btnSell
        btnSell.text = textButton

        detailViewModel.info.observe(viewLifecycleOwner) {
            binding.textInfo.text = detailViewModel.info.value
        }

        detailViewModel.text.observe(viewLifecycleOwner) {
            Log.e("DATE", "Observed data: $it")

            if (it.isNotEmpty()) {
                binding.lineChart.apply {
                    gradientFillColors = intArrayOf(
                        Color.parseColor("#16B30B"),
                        Color.TRANSPARENT
                    )
                    animation.duration = 1000L
                    onDataPointTouchListener = { index, _, _ ->
                        binding.tvChartData.text = it[index].second.toString()
                    }
                    animate(it)
                }
            }
        }

        btnSell.setOnClickListener {
            if (sourceFragment == "HomeFragment") {
                showAmountDialog(userName,"sell", detailViewModel, appRepository, ::sellShares)
            } else {
                showAmountDialog(userName, "buy",detailViewModel, appRepository, ::buyShares)
            }
        }

        return root
    }

    private fun showAmountDialog(
        userName: String?,
        info:String,
        detailViewModel: DetailViewModel,
        appRepository: AppRepository,
        action: (String?, DetailViewModel, AppRepository, Double) -> Unit
    ) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_amount_input, null)
        val editTextAmount = dialogView.findViewById<EditText>(R.id.editTextAmount)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Enter Amount")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val amount = editTextAmount.text.toString().toDoubleOrNull()
                if (amount != null) {
                    if(amount <= 0) {
                        Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    if(info=="sell" && amount > share.amount){
                        Toast.makeText(context, "You don't have enough shares to sell", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    action(userName, detailViewModel, appRepository, amount)
                } else {
                    Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun sellShares(userName: String?, detailViewModel: DetailViewModel, appRepository: AppRepository, amountToSell: Double) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val stockPrice = detailViewModel.stockPriceOfCurrentShare
                if (userName != null) {
                    appRepository.sellShare(userName, stockPrice.value ?: 0.0, share, amountToSell)
                    Toast.makeText(context, "Share successfully sold", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.navigation_home)
                } else {
                    Log.e("TU", "User is null while updating account money")
                }
            } catch (e: Exception) {
                Log.e("TU", "Error selling shares", e)
            }
        }
    }

    private fun buyShares(userName: String?, detailViewModel: DetailViewModel, appRepository: AppRepository, amountToBuy: Double) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val stockPrice = detailViewModel.stockPriceOfCurrentShare
                if (userName != null) {
                    val success = appRepository.buyShare(userName, stockPrice.value ?: 0.0, amountToBuy, searchResult)
                    if (!success) {
                        Toast.makeText(context, "You don't have enough money to buy this share!", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.navigation_search)
                    } else {
                        Toast.makeText(context, "Share successfully bought", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.navigation_home)
                    }
                } else {
                    Toast.makeText(context, "Error, try again later", Toast.LENGTH_LONG).show()
                    Log.e("TU", "User is null while updating account money")
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error try again later", Toast.LENGTH_LONG).show()
                Log.e("TU", "Error buying shares", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
