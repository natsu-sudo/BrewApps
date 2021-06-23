package com.assignment.brewapps.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.assignment.brewapps.pojo.Movies

@Dao
interface NowPlayingDetailDao {

    @Query("Select * from now_playing where id ==:id ")
    fun getMoviesDetail(id:Long): LiveData<Movies>
}