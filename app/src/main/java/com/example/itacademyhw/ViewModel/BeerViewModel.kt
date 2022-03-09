package com.example.itacademyhw.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.data.db.BeerDatabase
import com.example.domain.useCases.GetBeersUseCase
import com.example.itacademyhw.Paging.BeerRemoteMediator
import kotlinx.coroutines.flow.*

class BeerViewModel(
    private val beersUseCase: GetBeersUseCase,
    private val beerDatabase: BeerDatabase
    ) : ViewModel() {

    private val queryFlow = MutableStateFlow("")

    @ExperimentalPagingApi
    val pagingFlow = queryFlow.debounce(1000).flatMapLatest { query ->
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = 15,
                enablePlaceholders = false
            ),
            remoteMediator = BeerRemoteMediator(beersUseCase, beerDatabase, query),
            pagingSourceFactory = { beerDatabase.beerDao().pagingSource() }
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
