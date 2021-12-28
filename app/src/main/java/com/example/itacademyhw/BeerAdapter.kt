package com.example.itacademyhw

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.itacademyhw.databinding.BeerItemBinding
import com.example.itacademyhw.databinding.ItemLoadingBinding

class BeerAdapter: ListAdapter<PageItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private inner class LoadingViewHolder(private val binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_BEER_DATA -> BeerViewHolder(
                binding = BeerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> {
                LoadingViewHolder(
                    ItemLoadingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PageItem.Loading -> VIEW_TYPE_LOADING
            is PageItem.Content -> VIEW_TYPE_BEER_DATA
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is BeerViewHolder -> {
                holder.bind(item as PageItem.Content)
                holder.itemView.setOnClickListener{
                    it.findNavController().navigate(ListFragmentDirections.toDetails(
                        item.beer.name, item.beer.description
                    ))
                }
            }
        }
    }

    companion object {
        const val VIEW_TYPE_BEER_DATA = 0
        const val VIEW_TYPE_LOADING = 1

        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<PageItem>() {
            override fun areItemsTheSame(oldItem: PageItem, newItem: PageItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PageItem, newItem: PageItem): Boolean = oldItem == newItem
        }
    }
}