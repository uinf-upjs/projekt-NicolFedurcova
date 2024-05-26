package sk.upjs.hackstock.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sk.upjs.hackstock.MainApplication
import sk.upjs.hackstock.databinding.FragmentDetailBinding
import sk.upjs.hackstock.entities.Share
import java.util.Map

class DetailFragment: Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private lateinit var share: Share

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            share = it.getParcelable("share") ?: throw IllegalStateException("Share is missing")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //THIS ADDED TO COMMUNICATE WITH DB
        val application = requireNotNull(this.activity).application
        val usersRepository = (application as MainApplication).repository
        val factory = DetailViewModel.DetailViewModelFactory(usersRepository, share)
        //TO HERE
        val detailViewModel =
            ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDetail
        //textView.text = share.company
        textView.text = detailViewModel.name


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



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}