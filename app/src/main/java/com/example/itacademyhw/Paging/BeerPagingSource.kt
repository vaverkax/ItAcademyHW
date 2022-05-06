package com.example.itacademyhw.Paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.domain.model.Beer
import com.example.domain.useCases.GetBeersUseCase

class BeerPagingSource (
    private val beersUseCase: GetBeersUseCase,
    private val query: String
        ): PagingSource<Int, Beer>() {

    override fun getRefreshKey(state: PagingState<Int, Beer>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Beer> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE
            val loadSize = params.loadSize.coerceAtLeast(ITEMS_COUNT)
            var beerList =  beersUseCase
                .invoke(nextPage, loadSize)
            if (query != "") {
                beerList = beerList
                    .map { list ->
                        list.filter { it.name.lowercase().contains(query) }
                }
            }
            LoadResult.Page(
                data = beerList.getOrDefault(emptyList()),
                prevKey = if (nextPage != FIRST_PAGE ) nextPage - 1 else null,
                nextKey = if (beerList.getOrDefault(emptyList()).size == loadSize) nextPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val ITEMS_COUNT = 30
    }
}