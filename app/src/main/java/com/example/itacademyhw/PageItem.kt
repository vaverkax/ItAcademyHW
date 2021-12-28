package com.example.itacademyhw

import com.example.itacademyhw.model.BeerData

sealed class PageItem {
    data class Content(val beer: BeerData): PageItem()
    object Loading: PageItem()
}