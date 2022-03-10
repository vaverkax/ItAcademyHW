package com.example.data.mediator

import androidx.paging.*
import androidx.room.withTransaction
import com.example.data.api.BeerAPI
import com.example.data.db.BeerDatabase
import com.example.data.mapper.toDatabaseModel
import com.example.data.mapper.toDomainModel
import com.example.data.model.RemoteKeys
import com.example.domain.model.Beer

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val beerApi: BeerAPI,
    private val database: BeerDatabase,
    private val query: String
): RemoteMediator<Int, Beer>() {

    private val beerDAO = database.beerDao()
    private val remoteKeysDAO = database.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Beer>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.APPEND -> {
                    getClosestRemoteKey(state)?.nextKey
                }
                LoadType.PREPEND -> {
                    getFirstRemoteKey(state)?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.REFRESH -> {
                    getLastRemoteKey(state)?.nextKey
                }
            }
            val nextPage = loadKey ?: FIRST_PAGE
            val loadSize = state.config.pageSize.coerceAtLeast(ITEMS_COUNT)
            var beerList =  runCatching {
                beerApi.getBeers(nextPage, loadSize)
            }.map { beerList -> beerList.map { it.toDomainModel() } }
            if (query != "") {
                beerList = beerList
                    .map { list ->
                        list.filter { it.name.lowercase().contains(query) }
                    }
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    beerDAO.deleteAll()
                    remoteKeysDAO.deleteAll()
                }

                val prevKey = if (nextPage != FIRST_PAGE) nextPage - 1 else null
                val nextKey = if (beerList.getOrDefault(emptyList()).size == loadSize) nextPage + 1 else null
                val keys = beerList.map {
                    it.map {  beer ->
                        RemoteKeys(beerId = beer.id, prevKey, nextKey)
                    }
                }

                beerDAO.insertAll(beerList.getOrDefault(emptyList()).map { it.toDatabaseModel() })
                remoteKeysDAO.insertAll(keys.getOrDefault(emptyList()))
            }

            MediatorResult.Success(
                endOfPaginationReached = beerList.getOrDefault(emptyList()).isEmpty()
            )

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Beer>): RemoteKeys? = database.withTransaction {
        state.lastItemOrNull()?.let {
            remoteKeysDAO.findBeerById(it.id)
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Beer>): RemoteKeys? = database.withTransaction {
        state.firstItemOrNull()?.let {
            remoteKeysDAO.findBeerById(it.id)
        }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, Beer>): RemoteKeys? = database.withTransaction {
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let {
                remoteKeysDAO.findBeerById(it.id)
            }
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val ITEMS_COUNT = 30
    }
}