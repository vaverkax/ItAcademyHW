package com.example.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.data.model.BeerDataDB
import com.example.domain.model.Beer

@Dao
interface BeerDAO {
    @Query("select * from beerDataDb")
    fun pagingSource(): PagingSource<Int, Beer>

    @Query("select * from beerdatadb")
    suspend fun getAll(): List<Beer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(beers: List<BeerDataDB>)

    @Delete
    suspend fun delete(beer: BeerDataDB)

    @Query("delete from beerdatadb")
    suspend fun deleteAll()
}