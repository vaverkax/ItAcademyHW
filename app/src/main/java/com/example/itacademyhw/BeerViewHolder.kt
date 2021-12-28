package com.example.itacademyhw

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itacademyhw.databinding.BeerItemBinding

class BeerViewHolder(private val binding: BeerItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(beer: PageItem.Content) {
        with(binding) {
            image.load(beer.beer.imageUrl)
            textName.text = beer.beer.name
        }
    }
}