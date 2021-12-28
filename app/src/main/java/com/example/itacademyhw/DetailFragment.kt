package com.example.itacademyhw

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.itacademyhw.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = requireNotNull(_binding) {
        "View was destroyed"
    }

    private val beerName: String by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getString("beerName", "")
    }
    private val description: String by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getString("description", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDetailBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar
                .setupWithNavController(findNavController())

            descriptionTextView
                .text = description

            titleText
                .text = beerName
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}