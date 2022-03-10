package com.example.itacademyhw.viewHolders

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itacademyhw.databinding.BeerItemBinding
import com.example.domain.model.Beer
import com.example.itacademyhw.fragments.ListFragmentDirections

class BeerViewHolder(
    private val binding: BeerItemBinding,
    private val callback: () -> Unit): RecyclerView.ViewHolder(binding.root) {

    fun bind(beer: Beer) {
        with(binding) {
            itemView.setOnClickListener{
                it.findNavController().navigate(
                    ListFragmentDirections.toDetails(
                    beer.name, beer.description, beer.imageUrl
                ))
                callback()
            }
            image.load(beer.imageUrl)
            textName.text = beer.name
        }
    }
}