package com.example.itacademyhw.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.data.repository.BeerRepositoryImplementation
import kotlinx.coroutines.flow.*

class BeerViewModel(
    private val beerRepositoryImplementation: BeerRepositoryImplementation,
    ) : ViewModel() {

    private val queryFlow = MutableStateFlow("")

    @ExperimentalPagingApi
    val pagingFlow = queryFlow.debounce(1000).flatMapLatest { query ->
        beerRepositoryImplementation.getPagingData(query)
    }
        .cachedIn(viewModelScope)

    fun searchByName(name: String) {
        queryFlow.tryEmit(name)
    }
}
