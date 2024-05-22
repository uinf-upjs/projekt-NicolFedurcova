package sk.upjs.hackstock.ui.aiTips

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import sk.upjs.hackstock.databinding.FragmentAiTipsBinding

class AiTipsFragment : Fragment() {

    private var _binding: FragmentAiTipsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val aiTipsViewModel =
            ViewModelProvider(this).get(AiTipsViewModel::class.java)

        _binding = FragmentAiTipsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAiTips
        aiTipsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}