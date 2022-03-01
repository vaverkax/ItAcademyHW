package com.example.itacademyhw.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.itacademyhw.ViewHolders.BeerViewHolder
import com.example.itacademyhw.databinding.BeerItemBinding
import com.example.itacademyhw.model.BeerData

class BeerAdapter(
    context: Context
): PagingDataAdapter<BeerData, BeerViewHolder>(DIFF_CALLBACK) {

    private val layoutInflater = LayoutInflater.from(context)
    private val itemClickedCallback: () -> Unit = {
        //success callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        return BeerViewHolder(
                binding = BeerItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                ),
            callback = itemClickedCallback
            )
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is BeerViewHolder -> {
                holder.bind(item ?: BeerData(0,"","","","", emptyList(),""))
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<BeerData>() {
            override fun areItemsTheSame(oldItem: BeerData, newItem: BeerData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BeerData, newItem: BeerData): Boolean = oldItem == newItem
        }
    }
}