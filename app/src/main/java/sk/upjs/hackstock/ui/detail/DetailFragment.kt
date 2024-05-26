package sk.upjs.hackstock.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import sk.upjs.hackstock.MainApplication
import sk.upjs.hackstock.databinding.FragmentDetailBinding
import sk.upjs.hackstock.entities.Share

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
        val factory = DetailViewModel.DetailViewModelFactory(usersRepository)
        //TO HERE
        val detailViewModel =
            ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDetail
        textView.text = share.company
//        aiTipsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}