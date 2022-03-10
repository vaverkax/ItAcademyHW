package com.example.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.mediator.BeerRemoteMediator
import com.example.data.api.BeerAPI
import com.example.data.db.BeerDatabase
import com.example.domain.model.Beer
import kotlinx.coroutines.flow.Flow

class BeerRepositoryImplementation(
    private val beerApi: BeerAPI,
    private val database: BeerDatabase) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPagingData(query: String): Flow<PagingData<Beer>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = PAGE_SIZE,
            prefetchDistance = 15,
            enablePlaceholders = false
        ),
        remoteMediator = BeerRemoteMediator(beerApi, database, query),
        pagingSourceFactory = { database.beerDao().pagingSource() }
    ).flow

    companion object {
        private const val PAGE_SIZE = 30
    }
}