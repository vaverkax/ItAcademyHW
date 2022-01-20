package com.example.itacademyhw.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.itacademyhw.PageAdapter
import com.example.itacademyhw.databinding.FragmentContainerBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ContainerFragment: Fragment() {
    private var _binding: FragmentContainerBinding? = null
    private val binding get() = requireNotNull(_binding) {
        "View was destroyed"
    }
    private var selectedIndex: Int = 0

    companion object {
        private const val FIRST_SECTION = "Inspect DB"
        private const val SECOND_SECTION = "Insert row"
        private const val SELECTED_INDEX_KEY = "selectedIndex"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewPager.adapter = PageAdapter(this@ContainerFragment)

            TabLayoutMediator(tabBar,viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> FIRST_SECTION
                    1 -> SECOND_SECTION
                    else -> {error("Unsupported position $position") }
                }
            }.attach()
            if (savedInstanceState != null) {
                tabBar.getTabAt(selectedIndex)?.select()
            }
            tabBar.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        selectedIndex = tab.position
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_INDEX_KEY, selectedIndex)
    }
}