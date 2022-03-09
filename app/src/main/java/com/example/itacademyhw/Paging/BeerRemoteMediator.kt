package com.example.itacademyhw.Paging

import androidx.paging.*
import androidx.room.withTransaction
import com.example.data.db.BeerDatabase
import com.example.data.model.BeerDataDB
import com.example.data.model.RemoteKeys
import com.example.domain.model.Beer
import com.example.domain.useCases.GetBeersUseCase

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val beersUseCase: GetBeersUseCase,
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
            var beerList =  beersUseCase
                .invoke(nextPage, loadSize)
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

                beerDAO.insertAll(beerList.getOrDefault(emptyList()).map {
                    BeerDataDB(id = it.id,
                        name = it.name,
                        firstBrewed = it.firstBrewed,
                        description = it.description,
                        imageUrl = it.imageUrl,
                        brewTips = it.brewTips)
                 })
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