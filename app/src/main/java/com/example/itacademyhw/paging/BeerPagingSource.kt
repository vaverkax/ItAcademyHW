package com.example.itacademyhw.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.itacademyhw.model.BeerData
import com.example.itacademyhw.networking.BeerAPI

class BeerPagingSource (
    private val beerApi: BeerAPI,
    private val query: String
        ): PagingSource<Int, BeerData>() {

    override fun getRefreshKey(state: PagingState<Int, BeerData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BeerData> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE
            val loadSize = params.loadSize.coerceAtLeast(ITEMS_COUNT)
            var beerList =  beerApi
                .getBeers(nextPage, loadSize)
            if (query != "") {
                beerList = beerList.filter { it.name.contains(query) }
            }
            LoadResult.Page(
                data = beerList,
                prevKey = if (nextPage != FIRST_PAGE ) nextPage - 1 else null,
                nextKey = if (beerList.size == loadSize) nextPage + 1 else null
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