package com.example.itacademyhw.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.Beer
import com.example.itacademyhw.ViewHolders.BeerViewHolder
import com.example.itacademyhw.databinding.BeerItemBinding

class BeerAdapter(
    context: Context
): PagingDataAdapter<Beer, BeerViewHolder>(DIFF_CALLBACK) {

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
        getItem(position)?.let(holder::bind)
    }

    companion object {

        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Beer>() {
            override fun areItemsTheSame(oldItem: Beer, newItem: Beer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Beer, newItem: Beer): Boolean = oldItem == newItem
        }
    }
}