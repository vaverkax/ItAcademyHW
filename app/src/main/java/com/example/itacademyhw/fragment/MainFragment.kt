package com.example.itacademyhw.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.itacademyhw.MainInterface
import com.example.itacademyhw.provider.ServiceProvider
import com.example.itacademyhw.databinding.FragmentMainBinding
import com.example.itacademyhw.presenter.MainPresenter

class MainFragment: Fragment(), MainInterface {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = requireNotNull(_binding) {
        "View was destroyed"
    }

    private val mainPresenter: MainPresenter by lazy { MainPresenter(ServiceProvider.provideDataStore()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainPresenter.onAttachView(this)
        binding.buttonTap.setOnClickListener {
            mainPresenter.onViewClick()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        mainPresenter.destroy()
        super.onDestroy()
    }

    override fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun showItem(items: List<String>) {
        binding.textView.text = ""
        items.forEach {
            binding.textView.append(it)
            binding.textView.append("\n")
        }
    }
}