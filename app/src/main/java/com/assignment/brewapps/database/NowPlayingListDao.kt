package com.assignment.brewapps.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.assignment.brewapps.pojo.Movies


@Dao
interface NowPlayingListDao {

    @Query("select * from now_playing order by release_date")
    fun getMovieListFromDB(): LiveData<List<Movies>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoDb(list: List<Movies>)

    @Query("delete from now_playing")
    suspend fun deleteAllData()

    @Delete
    suspend fun delete(movies: Movies)

    @Query("Select * from now_playing where original_title LIKE '%' || :search || '%' order by release_date")
     fun searchForList(search:String):LiveData<List<Movies>>
}