package com.example.itacademyhw.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.domain.useCases.GetBeersUseCase
import com.example.itacademyhw.Paging.BeerPagingSource
import kotlinx.coroutines.flow.*

class BeerViewModel(private val beersUseCase: GetBeersUseCase) : ViewModel() {

    private val queryFlow = MutableStateFlow("")

    val pagingFlow = queryFlow.debounce(1000).flatMapLatest { query ->
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = 15,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BeerPagingSource(beersUseCase, query) }
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
