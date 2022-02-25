package com.example.itacademyhw.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.itacademyhw.R
import com.example.itacademyhw.databinding.FragmentContainerBinding

class ContainerFragment : Fragment() {
    private var _binding: FragmentContainerBinding? = null
    private val binding get() = requireNotNull(_binding) {
        "View was destroyed"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentContainerBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val navController = childFragmentManager.findFragmentById(R.id.mainContainerView) as NavHostFragment
            navView.setupWithNavController(navController.navController)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}