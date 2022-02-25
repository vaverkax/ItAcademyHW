package com.example.itacademyhw.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.navigation.findNavController
import com.example.itacademyhw.Extensions.applyInsetsWithAppBar
import com.example.itacademyhw.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {
    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = requireNotNull(_binding) {
        "View was destroyed"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return SettingsFragmentBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            appBar.applyInsetsWithAppBar { view, insets ->
                view.updatePadding(left = insets.left, right = insets.right, top = insets.top)
                insets
            }

            darkModeTextView.setOnClickListener {
                it.findNavController().navigate(SettingsFragmentDirections.actionSettingsFragment3ToModeFragment())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}