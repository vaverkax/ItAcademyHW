package com.example.itacademyhw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.itacademyhw.networking.BeerAPI
import com.example.itacademyhw.paging.BeerPagingSource
import kotlinx.coroutines.flow.*

class BeerViewModel(private val beerApi: BeerAPI) : ViewModel() {

    private val queryFlow = MutableStateFlow("")
    val pagingFlow = queryFlow.flatMapLatest { query ->
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = 15,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BeerPagingSource(beerApi, query) }
        ).flow
    }
        .cachedIn(viewModelScope)

    fun searchByName(name: String) {
        queryFlow.tryEmit(name)
    }

    companion object {
        private const val PAGE_SIZE = 30

    }
}
