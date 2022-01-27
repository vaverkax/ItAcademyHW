package com.example.itacademyhw.ViewHolders

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.itacademyhw.databinding.BeerItemBinding
import com.example.itacademyhw.fragment.ListFragmentDirections
import com.example.itacademyhw.model.BeerData

class BeerViewHolder(
    private val binding: BeerItemBinding,
    val callback: () -> Unit): RecyclerView.ViewHolder(binding.root) {

    fun bind(beer: BeerData) {
        with(binding) {
            itemView.setOnClickListener{
                it.findNavController().navigate(ListFragmentDirections.toDetails(
                    beer.name, beer.description
                ))
                callback()
            }
            image.load(beer.imageUrl)
            textName.text = beer.name
        }
    }
}