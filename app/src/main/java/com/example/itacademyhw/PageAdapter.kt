package com.example.itacademyhw

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.itacademyhw.fragment.EditFragment
import com.example.itacademyhw.fragment.TableFragment

class PageAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return TABS_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TableFragment()
            1 -> EditFragment()
            else -> error("Unsupported position $position")
        }
    }

    companion object {
        private const val TABS_COUNT = 2
    }
}