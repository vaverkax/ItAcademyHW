package com.example.itacademyhw.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.itacademyhw.ViewHolders.LoadingItemViewHolder
import com.example.itacademyhw.databinding.ItemLoadingBinding

class StateAdapter : LoadStateAdapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (loadState) {
            is LoadState.Loading -> LoadingItemViewHolder(
                binding = ItemLoadingBinding.inflate(inflater, parent, false)
            )
            is LoadState.NotLoading -> error("Not supported")
            is LoadState.Error -> error("Not supported")
        }
    }
}
