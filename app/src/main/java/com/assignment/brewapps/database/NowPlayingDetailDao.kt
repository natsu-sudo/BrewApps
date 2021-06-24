package com.assignment.brewapps.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.assignment.brewapps.pojo.Movies

@Dao
interface NowPlayingDetailDao {

    @Query("Select * from now_playing where id ==:id ")
    fun getMoviesDetail(id:Long): LiveData<Movies>

    @Query("UPDATE now_playing SET link_to_video =:links where id==:id")
    fun updateLink(links:String,id: Long)
}